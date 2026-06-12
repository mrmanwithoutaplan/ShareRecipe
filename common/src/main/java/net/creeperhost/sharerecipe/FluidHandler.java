package net.creeperhost.sharerecipe;

import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import mezz.jei.api.ingredients.ITypedIngredient;

public class FluidHandler {
    @ExpectPlatform
    public static FluidStack handleFluid(ITypedIngredient<?> fluid) {
        throw new AssertionError();
    }
}
