package net.creeperhost.sharerecipe.mixin;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RecipeSlot.class)
public interface RecipeSlotAccessor {
    @Invoker("getTooltip")
    void shareRecipe$getTooltip(ITooltipBuilder tooltip, ITypedIngredient<?> typedIngredient);
}