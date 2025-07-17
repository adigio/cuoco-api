package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.Step;

public class RecipeImageFactory {

    public static Step createMainImage() {
        return Step.builder()
                .id(1L)
                .title("Main Recipe Image")
                .number(1)
                .description("Main image for the recipe")
                .time("5 minutes")
                .imageName("test-recipe-main.jpg")
                .build();
    }

    public static Step createStepImage(Integer stepNumber) {
        return Step.builder()
                .id((long) stepNumber)
                .title("Step " + stepNumber + " Image")
                .number(stepNumber)
                .description("Image for step " + stepNumber)
                .time("2 minutes")
                .imageName("step-" + stepNumber + ".jpg")
                .build();
    }

    public static Step createMainRecipeImage() {
        return createMainImage();
    }

    public static Step createStepRecipeImage() {
        return createStepImage(1);
    }
} 