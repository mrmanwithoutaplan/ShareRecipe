package net.creeperhost.sharerecipe;

import java.util.List;

public class RecipeData
{
    String recipe_category;
    List<ShareSlot> inputs;
    List<ShareSlot> outputs;
    Background background;

    public RecipeData(String recipe_category, List<ShareSlot> inputs, List<ShareSlot> outputs, Background background) {
        this.recipe_category = recipe_category;
        this.inputs = inputs;
        this.outputs = outputs;
        this.background = background;
    }

    public String getRecipe_category() {
        return recipe_category;
    }

    public List<ShareSlot> getInputs() {
        return inputs;
    }

    public List<ShareSlot> getOutputs() {
        return outputs;
    }
}
