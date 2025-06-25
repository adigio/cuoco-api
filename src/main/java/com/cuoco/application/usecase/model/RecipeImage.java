package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeImage {
    private Long id;
    private String imageType;
    private String imageName;
    private String imagePath;
    private Integer stepNumber;
    private String stepDescription;
    private String imageUrl;
    private byte[] imageData;
} 