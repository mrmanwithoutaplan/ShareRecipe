package net.creeperhost.sharerecipe.mixin;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.library.gui.widgets.ScrollBoxRecipeWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ScrollBoxRecipeWidget.class)
public interface ScrollBoxRecipeWidgetAccessor {
    @Invoker(value = "getVisibleAmount", remap = false)
    int shareRecipe$getVisibleAmount();

    @Invoker(value = "getHiddenAmount", remap = false)
    int shareRecipe$getHiddenAmount();

    @Accessor(value = "contents", remap = false)
    IDrawable shareRecipe$getContents();
}
