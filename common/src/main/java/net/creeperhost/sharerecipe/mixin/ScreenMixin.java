package net.creeperhost.sharerecipe.mixin;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenMixin {
    @Accessor("renderables")
    List<Renderable> sharerecipe$getRenderables();
}
