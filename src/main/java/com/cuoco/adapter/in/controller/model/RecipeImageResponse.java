package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.RecipeImage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeImageResponse {
    private String imageType;
    private String imagePath;
    private Integer stepNumber;
    private String stepDescription;
    private String imageUrl;

    public static RecipeImageResponse fromDomain(RecipeImage recipeImage) {
        if (recipeImage == null) {
            return null;
        }
        
        return RecipeImageResponse.builder()
                .imageType(recipeImage.getImageType())
                .imagePath(recipeImage.getImagePath())
                .stepNumber(recipeImage.getStepNumber())
                .stepDescription(recipeImage.getStepDescription())
                .imageUrl(recipeImage.getImageUrl())
                .build();
    }
} 