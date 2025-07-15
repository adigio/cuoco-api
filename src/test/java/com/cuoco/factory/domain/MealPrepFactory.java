package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.Step;

import java.util.List;

public class MealPrepFactory {

    public static MealPrep create() {
        return MealPrep.builder()
                .id(1L)
                .title("Test Meal Prep")
                .estimatedCookingTime("30 minutes")
                .servings(4)
                .freeze(true)
                .steps(List.of(Step.builder()
                        .id(1L)
                        .title("Test Step")
                        .number(1)
                        .description("Test Step Description")
                        .time("10 minutes")
                        .build()))
                .recipes(List.of(RecipeFactory.create()))
                .ingredients(List.of(IngredientFactory.create()))
                .build();
    }
} 