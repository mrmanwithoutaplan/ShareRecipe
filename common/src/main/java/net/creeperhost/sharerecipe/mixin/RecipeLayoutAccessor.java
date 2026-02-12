package net.creeperhost.sharerecipe.mixin;

import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.common.gui.elements.DrawableResource;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.library.gui.recipes.RecipeLayout;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RecipeLayout.class)
public interface RecipeLayoutAccessor {
    @Accessor(value = "allWidgets", remap = false)
    List<IRecipeWidget> sharerecipe$getAllWidgets();

    @Accessor(value = "slottedWidgets", remap = false)
    List<IRecipeWidget> sharerecipe$getSlottedWidgets();

    @Accessor(value = "area", remap = false)
    ImmutableRect2i sharerecipe$getArea();

    @Accessor(value = "area", remap = false)
    void sharerecipe$setArea(ImmutableRect2i input);
}
