package com.cuoco.factory.gemini;

import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;

public class IngredientResponseGeminiModelFactory {

    public static IngredientResponseGeminiModel create(String name) {
        return IngredientResponseGeminiModel.builder()
                .name(name != null ? name : "Ingredient Name")
                .quantity(1.0)
                .optional(false)
                .unit("unit")
                .build();
    }
}
