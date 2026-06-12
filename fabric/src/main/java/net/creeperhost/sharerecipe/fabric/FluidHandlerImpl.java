package net.creeperhost.sharerecipe.fabric;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.creeperhost.sharerecipe.ShareIngredient;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import java.util.Optional;

public class FluidHandlerImpl {
    public static FluidStack handleFluid(ITypedIngredient<?> fluid) {
        if (fluid.getType() == FabricTypes.FLUID_STACK) {
            Optional<IJeiFluidIngredient> fluidIngredient = fluid.getIngredient(FabricTypes.FLUID_STACK);
            if (fluidIngredient.isPresent()) {
                IJeiFluidIngredient iJeiFluidIngredient = fluidIngredient.get();
                FluidVariant fluidVariant = iJeiFluidIngredient.getFluidVariant();
                return FluidStackHooksFabric.fromFabric(fluidVariant, iJeiFluidIngredient.getAmount() / 81); // why...
            }
        }
        return null;
    }
}