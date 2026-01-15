package net.creeperhost.sharerecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class NerfedGuiGraphics extends GuiGraphics {
    public record StyleChange(Style style, int start) {}
    public int stringCount = 0;
    public HashMap<String, List<StyleChange>> capturedStrings = new HashMap<>();
    public NerfedGuiGraphics(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    @Override
    public int drawString(Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k, boolean bl) {
        stringCount++;
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
        capturedStrings.put(builder.toString(), styleChanges);
        return 0;
    }

    @Override
    public int drawString(Font font, @Nullable String string, int i, int j, int k, boolean bl) {
        return 0;
    }

    @Override
    public void renderItem(ItemStack itemStack, int i, int j) {
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
}