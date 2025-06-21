package com.cuoco.factory.domain;

import com.cuoco.application.usecase.model.RecipeImage;

public class RecipeImageFactory {

    public static RecipeImage createMainRecipeImage() {
        return RecipeImage.builder()
                .imageType("MAIN")
                .imagePath("src/main/resources/imagenes/recetas/test-recipe/test-recipe-main.jpg")
                .stepNumber(null)
                .stepDescription(null)
                .imageUrl("https://example.com/main-image.jpg")
                .imageData("fake-main-image-data".getBytes())
                .build();
    }

    public static RecipeImage createStepRecipeImage() {
        return createStepRecipeImageWithNumber(1);
    }

    public static RecipeImage createStepRecipeImageWithNumber(Integer stepNumber) {
        return RecipeImage.builder()
                .imageType("STEP")
                .stepNumber(stepNumber)
                .stepDescription("Step " + stepNumber + " description")
                .imagePath(String.format("src/main/resources/imagenes/pasos/test-recipe/test-recipe-step-%d.jpg", stepNumber))
                .imageUrl(String.format("https://example.com/step-%d-image.jpg", stepNumber))
                .imageData(("fake-step-" + stepNumber + "-image-data").getBytes())
                .build();
    }

    public static RecipeImage createStepImage(Integer stepNumber, String stepDescription) {
        return RecipeImage.builder()
                .imageType("STEP")
                .stepNumber(stepNumber)
                .stepDescription(stepDescription)
                .imagePath(String.format("src/main/resources/imagenes/pasos/test-recipe/test-recipe-step-%d.jpg", stepNumber))
                .imageUrl(String.format("https://example.com/step-%d-image.jpg", stepNumber))
                .imageData("fake-step-image-data".getBytes())
                .build();
    }

    // Legacy methods for backward compatibility
    public static RecipeImage createMainImage() {
        return createMainRecipeImage();
    }

    public static RecipeImage create() {
        return createMainRecipeImage();
    }
} 