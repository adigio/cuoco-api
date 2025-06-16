package com.cuoco.factory;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;

import java.util.List;

public class RecipeFactory {

    public static Recipe create() {
        return Recipe.builder()
                .id("1")
                .name("RECIPE")
                .subtitle("RECIPE SUBTITLE")
                .description("RECIPE DESCRIPTION")
                .image("http://image.com")
                .instructions("INSTRUCTIONS")
                .preparationTime("PREPARATION_TIME")
                .ingredients(List.of(
                        Ingredient.builder().name("Tomate").source("image").confirmed(true).quantity(2.0).unit("unidad").build(),
                        Ingredient.builder().name("Lechuga").source("voice").confirmed(true).quantity(1.0).unit("unidad").build(),
                        Ingredient.builder().name("Cebolla").source("text").confirmed(false).quantity(0.5).unit("kg").build()
                ))
                .build();
    }

    public static RecipeRequest getRecipeRequest() {
        Recipe recipe = create();

        return RecipeRequest.builder()
                .ingredients(recipe.getIngredients().stream().map(ingredient ->
                        IngredientRequest.builder()
                                .name(ingredient.getName())
                                .source(ingredient.getSource())
                                .confirmed(ingredient.isConfirmed())
                                .build())
                        .toList())
                .build();
    }
}
