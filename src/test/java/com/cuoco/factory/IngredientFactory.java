package com.cuoco.factory;

import com.cuoco.application.usecase.model.Ingredient;

public class IngredientFactory {

    public static Ingredient create() {
        return Ingredient.builder()
                .name("Ingredient 1")
                .quantity(1.0)
                .unit("grams")
                .optional(true)
                .source("text")
                .confirmed(false)
                .build();
    }
}
