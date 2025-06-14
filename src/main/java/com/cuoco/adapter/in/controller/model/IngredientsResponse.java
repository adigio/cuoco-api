package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.Ingredient;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import java.util.Map;

public class IngredientsResponse {
    private List<Ingredient> ingredients;
    private Map<String, List<String>> ingredientsByImage;

    public IngredientsResponse(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientsResponse(Map<String, List<String>> ingredientsByImage) {
        this.ingredientsByImage = ingredientsByImage;
    }

    @JsonValue
    public Object getResponse() {
        if (ingredientsByImage != null) {
            return ingredientsByImage;
        }
        return ingredients.stream()
                .map(Ingredient::getName)
                .toList();
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, List<String>> getIngredientsByImage() {
        return ingredientsByImage;
    }

    public void setIngredientsByImage(Map<String, List<String>> ingredientsByImage) {
        this.ingredientsByImage = ingredientsByImage;
    }
}