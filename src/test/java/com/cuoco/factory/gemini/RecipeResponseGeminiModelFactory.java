package com.cuoco.factory.gemini;

import com.cuoco.adapter.out.rest.gemini.model.CookLevelResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.RecipeResponseGeminiModel;

import java.util.List;

public class RecipeResponseGeminiModelFactory {

    public static RecipeResponseGeminiModel create() {
        return RecipeResponseGeminiModel.builder()
                .name("Recipe name")
                .preparationTime("20 min")
                .image("some-image-url")
                .subtitle("Recipe subtitle")
                .description("Recipe descirption")
                .ingredients(List.of(
                        IngredientResponseGeminiModel.builder().name("Ingredient 1").quantity(2.0).unit("unit").build(),
                        IngredientResponseGeminiModel.builder().name("Ingredient 2").quantity(1.0).unit("unit").build()
                ))
                .cookLevel(CookLevelResponseGeminiModel.builder().id(1).description("bajo").build())
                .instructions("Instructions")
                .build();
    }
}
