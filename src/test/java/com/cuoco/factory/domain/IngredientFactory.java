package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.Ingredient;

public class IngredientFactory {

    public static Ingredient create(String name) {
        return Ingredient.builder()
                .name(name != null ? name : "Ingredient 1")
                .quantity(1.0)
                .unit("grams")
                .optional(true)
                .source("text")
                .confirmed(false)
                .build();
    }
}
