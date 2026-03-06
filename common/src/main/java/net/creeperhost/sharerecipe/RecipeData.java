package net.creeperhost.sharerecipe;

import java.util.List;

public class RecipeData
{
    String recipeCategory;
    List<ShareSlot> slots;
    Background background;
    Modpack modpack;

    public RecipeData(String recipe_category, List<ShareSlot> slots, Background background, Modpack modpack) {
        this.recipeCategory = recipe_category;
        this.slots = slots;
        this.background = background;
        this.modpack = modpack;
    }

    public record Modpack(String id, String provider) {}
}
