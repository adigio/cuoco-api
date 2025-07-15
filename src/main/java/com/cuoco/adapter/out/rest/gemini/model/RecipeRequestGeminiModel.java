package com.cuoco.adapter.out.rest.gemini.model;

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
public class RecipeRequestGeminiModel {

    private Long id;
    private String name;
    private String subtitle;
    private String description;

    private String preparationTime;
    private String cookLevelName;
    private String dietName;
    private List<String> mealTypesNames;
    private List<String> allergiesNames;
    private List<String> dietaryNeedsNames;
    private List<String> ingredientNames;
}
