package net.creeperhost.sharerecipe.mixin;

import mezz.jei.common.util.TickTimer;
import net.creeperhost.sharerecipe.ShareRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TickTimer.class)
public class TickTimerMixin {

    @Inject(method = "Lmezz/jei/common/util/TickTimer;getValue(JJIIZ)I", at = @At("RETURN"), remap = false, cancellable = true)
    private static void getValue(long startTime, long currentTime, int maxValue, int msPerCycle, boolean countDown, CallbackInfoReturnable<Integer> cir) {
        if (ShareRecipe.fakeZero.get()) {
            int returnValue = 0;
            if (countDown) returnValue = maxValue;
            cir.setReturnValue(returnValue);
        }
    }
}