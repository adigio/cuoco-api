package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.Step;

public class RecipeImageFactory {

    public static Step createMainRecipeImage() {
        return Step.builder()
                .imageType("MAIN")
                .imagePath("src/main/resources/imagenes/recetas/test-recipe/test-recipe-main.jpg")
                .stepNumber(null)
                .stepDescription(null)
                .imageUrl("https://example.com/main-image.jpg")
                .imageData("fake-main-image-data".getBytes())
                .build();
    }

    public static Step createStepRecipeImage() {
        return createStepRecipeImageWithNumber(1);
    }

    public static Step createStepRecipeImageWithNumber(Integer stepNumber) {
        return Step.builder()
                .imageType("STEP")
                .stepNumber(stepNumber)
                .stepDescription("Step " + stepNumber + " description")
                .imagePath(String.format("src/main/resources/imagenes/pasos/test-recipe/test-recipe-step-%d.jpg", stepNumber))
                .imageUrl(String.format("https://example.com/step-%d-image.jpg", stepNumber))
                .imageData(("fake-step-" + stepNumber + "-image-data").getBytes())
                .build();
    }

    public static Step create() {
        return createMainRecipeImage();
    }
} 