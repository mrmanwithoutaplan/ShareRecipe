package net.creeperhost.sharerecipe.mixin;

import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.library.gui.widgets.ScrollGridRecipeWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(ScrollGridRecipeWidget.class)
public interface ScrollGridRecipeWidgetAccessor {
    @Accessor(value = "slots", remap = false)
    List<IRecipeSlotDrawable> sharerecipe$getSlots();
    @Accessor(value = "columns", remap = false)
    int sharerecipe$getColumns();
}
