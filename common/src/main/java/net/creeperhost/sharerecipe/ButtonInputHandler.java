package net.creeperhost.sharerecipe;

import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.InputType;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record ButtonInputHandler(Button button) implements IUserInputHandler {
    @Override
    public @NotNull Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
        InputType inputType = input.getInputType();
        if(inputType == InputType.SIMULATE) {
            return button.isMouseOver(input.getMouseX(), input.getMouseY()) ? Optional.of(this) : Optional.empty();
        }
        boolean b = button.mouseClicked(input.getMouseX(), input.getMouseY(), input.getModifiers());
        if (b && button instanceof IconButton) {
            return Optional.of(this);
        }
        return Optional.empty();
    }
}