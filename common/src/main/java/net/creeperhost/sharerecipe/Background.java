package net.creeperhost.sharerecipe;

import net.minecraft.resources.ResourceLocation;

public record Background(ResourceLocation resourceLocation, int textureWidth, int textureHeight, int u, int v, int width, int height) {
}
