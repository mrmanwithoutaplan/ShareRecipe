package net.creeperhost.sharerecipe.fabric;

import net.creeperhost.sharerecipe.ShareRecipe;
import net.fabricmc.api.ModInitializer;

public final class ShareRecipeFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ShareRecipe.init();
    }
}
