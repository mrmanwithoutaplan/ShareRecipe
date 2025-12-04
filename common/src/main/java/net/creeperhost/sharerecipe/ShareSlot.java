package net.creeperhost.sharerecipe;

import net.minecraft.client.renderer.Rect2i;

import java.util.List;

public record ShareSlot(int num, Rect2i position, List<ShareIngredient> ingredients) {
}
