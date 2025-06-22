package com.cuoco.adapter.in.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeResponse {
    private Long id;
    private String name;
    private String subtitle;
    private String description;
    private String image;
    private String instructions;
    private ParametricResponse preparationTime;
    private ParametricResponse type;
    private ParametricResponse cookLevel;
    private List<ParametricResponse> categories;
    private List<IngredientResponse> ingredients;

    private List<RecipeImageResponse> generatedImages;
}