package net.creeperhost.sharerecipe;

import java.util.List;

public class RecipeData
{
    String recipeCategory;
    List<ShareSlot> inputs;
    List<ShareSlot> outputs;
    Background background;

    public RecipeData(String recipe_category, List<ShareSlot> inputs, List<ShareSlot> outputs, Background background) {
        this.recipeCategory = recipe_category;
        this.inputs = inputs;
        this.outputs = outputs;
        this.background = background;

    }
}
