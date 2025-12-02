package net.creeperhost.sharerecipe;

import java.util.List;

public class RecipeData
{
    String recipe_category;
    List<String> inputs;
    List<String> outputs;

    public RecipeData(String recipe_category, List<String> inputs, List<String> outputs) {
        this.recipe_category = recipe_category;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public String getRecipe_category() {
        return recipe_category;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public List<String> getOutputs() {
        return outputs;
    }
}
