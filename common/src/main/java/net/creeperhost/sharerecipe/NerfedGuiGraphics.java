package net.creeperhost.sharerecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class NerfedGuiGraphics extends GuiGraphics {
    public record StyleChange(Style style, int start) {}
    public record CapturedString(String string, int x, int y, int colour, boolean renderShadow, List<StyleChange> styleChanges) {}
    public List<CapturedString> capturedStrings = new ArrayList<>();
    public int scale;
    public NerfedGuiGraphics(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource, int scale) {
        super(minecraft, bufferSource);
        this.scale = scale;
    }

    @Override
    public int drawString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k, boolean bl) {
//        super.drawString(font, formattedCharSequence, i, j, k, bl);
        Vector3f translation = new Vector3f();
        this.pose().last().pose().getTranslation(translation);

        capturedStrings.add(getCapturedString(font, formattedCharSequence, i * scale, j * scale, k, bl, (int) translation.x(), (int) translation.y()));
        return 0;
    }

    @Override
    public int drawString(Font font, @Nullable String string, int i, int j, int k, boolean bl) {
//        super.drawString(font, string, i, j, k, bl);
        Vector3f translation = new Vector3f();
        this.pose().last().pose().getTranslation(translation);
        capturedStrings.add(new CapturedString(string, (int) ((i * scale) + translation.x()), (int) ((j * scale) + translation.y()), k, bl, new ArrayList<>()));
        return 0;
    }

    @Override
    public void renderItem(ItemStack itemStack, int i  , int j) {
    }

    @Override     
    public void renderItem(ItemStack itemStack, int i, int j, int k) {
    }

    @Override
    public void renderItem(ItemStack itemStack, int i, int j, int k, int l) {
    }

    @Override
    public void renderFakeItem(ItemStack itemStack, int i, int j, int k) {
    }

    @Override
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, int i, int j, int k) {
    }

    @Override
    public void renderItemDecorations(Font font, ItemStack itemStack, int i, int j, @Nullable String string) {
    }

    public static CapturedString getCapturedString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k, boolean bl, int translationX, int translationY) {
        StringBuilder builder = new StringBuilder();
        AtomicReference<Style> lastStyle = new AtomicReference<>();
        List<StyleChange> styleChanges = new ArrayList<>();
        formattedCharSequence.accept((i1, style, j1) -> {
            if (style != null && !style.equals(lastStyle.get())) {
                lastStyle.set(style);
                styleChanges.add(new StyleChange(style, i1));
            }
            char blah = (char) j1;
            builder.append(blah);
            return true;
        });
        return new CapturedString(builder.toString(), translationX + i , translationY + j, k, bl, styleChanges);
    }

    public static List<CapturedString> getCapturedStrings(FormattedText formattedText) {
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> split = font.split(formattedText, Integer.MAX_VALUE);
        List<CapturedString> capturedStrings = new ArrayList<>();
        for (FormattedCharSequence formattedCharSequence : split) {
            capturedStrings.add(getCapturedString(font, formattedCharSequence, 0, 0, 0xFFFFFFFF, false, 0, 0));
        }
        return capturedStrings;
    }
}