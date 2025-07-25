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
public class MealPrepFilter {
    private Boolean freeze;
    private Integer servings;
    private PreparationTime preparationTime;
    private CookLevel cookLevel;
    private Diet diet;
    private List<MealType> mealTypes;
    private List<Allergy> allergies;
    private List<DietaryNeed> dietaryNeeds;
}
