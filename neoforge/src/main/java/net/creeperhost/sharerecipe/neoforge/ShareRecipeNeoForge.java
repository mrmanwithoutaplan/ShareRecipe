package net.creeperhost.sharerecipe.neoforge;

import net.creeperhost.sharerecipe.ShareRecipe;
import net.neoforged.fml.common.Mod;

@Mod(ShareRecipe.MOD_ID)
public final class ShareRecipeNeoForge {
    public ShareRecipeNeoForge() {
        // Run our common setup.
        ShareRecipe.init();
    }
}
