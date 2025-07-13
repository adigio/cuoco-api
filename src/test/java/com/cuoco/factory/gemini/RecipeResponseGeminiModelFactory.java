package com.cuoco.factory.gemini;

import com.cuoco.adapter.out.rest.gemini.model.RecipeResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.StepResponseGeminiModel;
import com.cuoco.application.usecase.model.Unit;

import java.util.List;

public class RecipeResponseGeminiModelFactory {

    public static RecipeResponseGeminiModel create() {
        return RecipeResponseGeminiModel.builder()
                .id("1")
                .name("Test Recipe")
                .subtitle("Test Subtitle")
                .description("Test Description")
                .ingredients(List.of(IngredientResponseGeminiModel.builder()
                        .name("Test Ingredient")
                        .quantity(1.0)
                        .unit(Unit.builder().id(1).description("Cup").symbol("cup").build())
                        .optional(false)
                        .build()))
                .steps(List.of(StepResponseGeminiModel.builder()
                        .title("Test Step")
                        .description("Test Step Description")
                        .time("10 minutes")
                        .build()))
                .build();
    }
}
