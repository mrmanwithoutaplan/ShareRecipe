package net.creeperhost.sharerecipe.mixin;

import mezz.jei.common.gui.elements.DrawableResource;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawableResource.class)
public interface DrawableResourceAccessor {
    @Accessor(value = "resourceLocation", remap = false)
    ResourceLocation sharerecipe$getResourceLocation();
    @Accessor(value = "textureWidth", remap = false)
    int sharerecipe$getTextureWidth();
    @Accessor(value = "textureHeight", remap = false)
    int sharerecipe$getTextureHeight();
    @Accessor(value = "u", remap = false)
    int sharerecipe$getU();
    @Accessor(value = "v", remap = false)
    int sharerecipe$getV();
    @Accessor(value = "width", remap = false)
    int sharerecipe$getWidth();
    @Accessor(value = "height", remap = false)
    int sharerecipe$getHeight();
}
