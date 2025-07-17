package com.cuoco.factory.gemini;

import com.cuoco.adapter.out.rest.gemini.model.MealPrepResponseGeminiModel;

import java.util.List;

public class MealPrepResponseGeminiModelFactory {

    public static MealPrepResponseGeminiModel create() {
        return MealPrepResponseGeminiModel.builder()
                .title("Test Meal Prep")
                .estimatedCookingTime("30 minutes")
                .servings(4)
                .freeze(true)
                .recipeIds(List.of(1L))
                .build();
    }
} 