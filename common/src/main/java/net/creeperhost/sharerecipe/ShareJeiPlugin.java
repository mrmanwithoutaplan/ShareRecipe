package net.creeperhost.sharerecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IAdvancedRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class ShareJeiPlugin implements IModPlugin {
    private static ShareJeiPlugin INSTANCE;
    IJeiHelpers helper;

    public static ShareJeiPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ShareRecipe.MOD_ID, "jei");
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        INSTANCE = this;
        helper = registration.getJeiHelpers();
        registration.addRecipeButtonFactory(new JeiShareButtonFactory());
    }
}
