package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Unit;

public class IngredientFactory {

    public static Ingredient create() {
        return Ingredient.builder()
                .name("Ingredient 1")
                .quantity(1.0)
                .unit(Unit.builder()
                        .id(1)
                        .description("Gram")
                        .symbol("gr")
                        .build()
                )
                .optional(true)
                .source("text")
                .confirmed(false)
                .build();
    }

    public static Ingredient create(String name, Double quantity, String unitSymbol) {
        return Ingredient.builder()
                .name(name != null ? name : "Ingredient 1")
                .quantity(quantity != null ? quantity : 1.0)
                .unit(Unit.builder()
                        .id(1)
                        .description("Gram")
                        .symbol(unitSymbol != null ? unitSymbol : "gr")
                        .build()
                )
                .optional(true)
                .source("text")
                .confirmed(false)
                .build();
    }
}
