package net.creeperhost.sharerecipe.mixin;

import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.common.gui.elements.DrawableResource;
import mezz.jei.library.gui.recipes.RecipeLayout;
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
}
