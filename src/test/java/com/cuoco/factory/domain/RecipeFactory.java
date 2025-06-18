package com.cuoco.factory.domain;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.application.usecase.model.Unit;

import java.util.List;

public class RecipeFactory {

    public static Recipe create() {
        return Recipe.builder()
                .id(1L)
                .name("RECIPE")
                .subtitle("RECIPE SUBTITLE")
                .description("RECIPE DESCRIPTION")
                .image("http://image.com")
                .instructions("INSTRUCTIONS")
                .preparationTime("PREPARATION_TIME")
                .cookLevel(CookLevel.builder()
                        .id(1)
                        .description("Bajo")
                        .build()
                )
                .ingredients(List.of(
                        Ingredient.builder()
                                .name("Tomate")
                                .source("image")
                                .confirmed(true)
                                .quantity(2.0)
                                .unit(Unit.builder().id(1).description("Unidad").symbol("ud").build())
                                .build(),
                        Ingredient.builder()
                                .name("Lechuga")
                                .source("voice")
                                .confirmed(true)
                                .quantity(1.0)
                                .unit(Unit.builder().id(1).description("Unidad").symbol("ud").build())
                                .build(),
                        Ingredient.builder()
                                .name("Cebolla")
                                .source("text")
                                .confirmed(false)
                                .quantity(0.5)
                                .unit(Unit.builder().id(1).description("Kilogramo").symbol("kg").build())
                                .build()
                ))
                .filters(RecipeFilter.builder()
                        .enable(false)
                        .maxRecipes(3)
                        .build())
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

    public static Recipe createWithFilters() {
        Recipe recipe = create();

        RecipeFilter filters = RecipeFilter.builder()
                .enable(true)
                .diet("diet")
                .types(List.of("desayuno", "cena"))
                .time("30 min")
                .quantity(2)
                .difficulty(CookLevel.builder().id(1).description("bajo").build())
                .maxRecipes(3)
                .build();
        recipe.setFilters(filters);

        return recipe;
    }
}
