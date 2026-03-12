package net.creeperhost.sharerecipe;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringDecomposer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class NerfedGuiGraphics extends GuiGraphics {
    public record StringWithStyle(String string, Style style) {

    }
    public record CapturedString(List<StringWithStyle> strings, int x, int y, float width, int colour, boolean renderShadow) {
    }
    public List<CapturedString> capturedStrings = new ArrayList<>();
    public int scale;
    public NerfedGuiGraphics(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource, int scale) {
        super(minecraft, bufferSource);
        this.scale = scale;
    }

    @Override
    public int drawString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k, boolean bl) {
        Vector3f translation = new Vector3f();
        Vector3f scaleVector = new Vector3f();
        this.pose().last().pose().getTranslation(translation);
        this.pose().last().pose().getScale(scaleVector);
        float width = font.width(formattedCharSequence) * scaleVector.y();
        capturedStrings.add(getCapturedString(font, formattedCharSequence, i * scale, j * scale, width, k, bl, (int) translation.x(), (int) translation.y()));
        return 0;
    }

    @Override
    public int drawString(Font font, @Nullable String string, int i, int j, int k, boolean bl) {
        Vector3f translation = new Vector3f();
        Vector3f scaleVector = new Vector3f();
        this.pose().last().pose().getScale(scaleVector);
        float width = font.width(string) * scaleVector.y();
        this.pose().last().pose().getTranslation(translation);
        StringBuilder builder = new StringBuilder();
        AtomicReference<Style> lastStyle = new AtomicReference<>(Style.EMPTY);
        List<StringWithStyle> stringStyles = new ArrayList<>();
        StringDecomposer.iterateFormatted(string != null ? string : "", Style.EMPTY, ((i1, style, j1) -> {
            char blah = (char) j1;
            if (!lastStyle.get().equals(style)) {
                if (style == null) style = Style.EMPTY;
                if (!builder.isEmpty()) stringStyles.add(new StringWithStyle(builder.toString(), lastStyle.get()));
                builder.delete(0, builder.length());
                lastStyle.set(style);
            }
            builder.append(blah);
            return true;
        }));
        if (!builder.isEmpty()) stringStyles.add(new StringWithStyle(builder.toString(), lastStyle.get()));
        capturedStrings.add(new CapturedString(stringStyles, (int) ((i * scale) + translation.x()), (int) ((j * scale) + translation.y()), width, k, bl));
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

    public static CapturedString getCapturedString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, float width, int k, boolean bl, int translationX, int translationY) {
        StringBuilder builder = new StringBuilder();
        AtomicReference<Style> lastStyle = new AtomicReference<>(Style.EMPTY);
        List<StringWithStyle> stringStyles = new ArrayList<>();
        formattedCharSequence.accept((i1, style, j1) -> {
            char blah = (char) j1;
            if (!lastStyle.get().equals(style)) {
                if (style == null) style = Style.EMPTY;
                if (!builder.isEmpty()) stringStyles.add(new StringWithStyle(builder.toString(), lastStyle.get()));
                builder.delete(0, builder.length());
                lastStyle.set(style);
            }
            builder.append(blah);
            return true;
        });
        if (!builder.isEmpty()) stringStyles.add(new StringWithStyle(builder.toString(), lastStyle.get()));
        if (width == 0) width = font.width(formattedCharSequence);
        return new CapturedString(stringStyles, translationX + i , translationY + j, width, k, bl);
    }

    public static List<CapturedString> getCapturedStrings(FormattedText formattedText) {
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> split = font.split(formattedText, Integer.MAX_VALUE);
        List<CapturedString> capturedStrings = new ArrayList<>();
        for (FormattedCharSequence formattedCharSequence : split) {
            capturedStrings.add(getCapturedString(font, formattedCharSequence, 0, 0, 0, 0xFFFFFFFF, true, 0, 0));
        }
        return capturedStrings;
    }
}