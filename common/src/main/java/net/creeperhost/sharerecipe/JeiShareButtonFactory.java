package net.creeperhost.sharerecipe;

import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.buttons.IIconButtonController;
import mezz.jei.api.recipe.advanced.IRecipeButtonControllerFactory;
import org.jetbrains.annotations.Nullable;

public class JeiShareButtonFactory implements IRecipeButtonControllerFactory {
    @Override
    public @Nullable <T> IIconButtonController createButtonController(IRecipeLayoutDrawable<T> iRecipeLayoutDrawable) {
        return new ShareButtonController(iRecipeLayoutDrawable);
    }
}
