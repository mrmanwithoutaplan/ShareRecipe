package net.creeperhost.sharerecipe.mixin;

import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.elements.GuiIconToggleButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIconToggleButton.class)
public interface GuiIconToggleButtonMixin {
    @Accessor(value = "area", remap = false)
    ImmutableRect2i sharerecipe$getArea();
}
