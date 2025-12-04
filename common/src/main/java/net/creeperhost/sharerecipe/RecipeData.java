package net.creeperhost.sharerecipe;

import java.util.List;

public class RecipeData
{
    String recipe_category;
    List<List<ShareIngredient>> inputs;
    List<ShareIngredient> outputs;

    public RecipeData(String recipe_category, List<List<ShareIngredient>> inputs, List<ShareIngredient> outputs) {
        this.recipe_category = recipe_category;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public String getRecipe_category() {
        return recipe_category;
    }

    public List<List<ShareIngredient>> getInputs() {
        return inputs;
    }

    public List<ShareIngredient> getOutputs() {
        return outputs;
    }
}
