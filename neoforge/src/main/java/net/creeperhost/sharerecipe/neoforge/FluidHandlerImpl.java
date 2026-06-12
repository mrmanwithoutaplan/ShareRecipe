package net.creeperhost.sharerecipe.neoforge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.neoforge.NeoForgeTypes;

import java.util.Optional;

public class FluidHandlerImpl {
    public static FluidStack handleFluid(ITypedIngredient<?> fluid) {
        if (fluid.getType() == NeoForgeTypes.FLUID_STACK) {
            Optional<net.neoforged.neoforge.fluids.FluidStack> ingredient = fluid.getIngredient(NeoForgeTypes.FLUID_STACK);
            if (ingredient.isPresent()) return FluidStackHooksForge.fromForge(ingredient.get());
        }
        return null;
    }
}
