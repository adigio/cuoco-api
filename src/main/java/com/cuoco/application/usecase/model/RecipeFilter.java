package com.cuoco.application.usecase.model;

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
public class RecipeFilter {

    private PreparationTime preparationTime;
    private Integer servings;
    private CookLevel cookLevel;
    private List<MealType> types;
    private List<MealCategory> categories;
    private Boolean useProfilePreferences;

    private Integer maxRecipes;
    private Boolean enable;
}
