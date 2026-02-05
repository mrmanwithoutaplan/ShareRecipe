package net.creeperhost.sharerecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IAdvancedRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class ShareJeiPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ShareRecipe.MOD_ID, "jei");
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        registration.addRecipeButtonFactory(new JeiShareButtonFactory());
    }
}
