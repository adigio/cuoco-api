package com.cuoco.adapter.in.controller.model;

import java.util.List;

public class RecipeRequest {

    private List<IngredientRequest> ingredients;
    private FilterRequest filters;

    public List<IngredientRequest> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientRequest> ingredients) {
        this.ingredients = ingredients;
    }

    public FilterRequest getFilters() {
        return filters;
    }

    public void setFilters(FilterRequest filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "RecipeRequest{" +
                "ingredients=" + ingredients +
                ", filters=" + filters +
                '}';
    }
}
