package com.cuoco.factory.gemini;

import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.application.usecase.model.Unit;

import java.util.List;

public class IngredientResponseGeminiModelFactory {

    public static IngredientResponseGeminiModel create() {
        return IngredientResponseGeminiModel.builder()
                .name("Test Ingredient")
                .quantity(1.0)
                .unit(Unit.builder().id(1).description("Cup").symbol("cup").build())
                .optional(false)
                .build();
    }

    public static IngredientResponseGeminiModel create(String name) {
        return IngredientResponseGeminiModel.builder()
                .name(name)
                .quantity(1.0)
                .unit(Unit.builder().id(1).description("Cup").symbol("cup").build())
                .optional(false)
                .build();
    }
}
