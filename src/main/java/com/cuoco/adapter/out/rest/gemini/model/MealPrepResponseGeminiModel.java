package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.MealPrep;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MealPrepResponseGeminiModel {
    private String title;
    private String estimatedCookingTime;
    private Integer servings;
    private Boolean freeze;
    private List<Long> recipeIds;
    private List<StepResponseGeminiModel> steps;
    private List<IngredientResponseGeminiModel> ingredients;

    public MealPrep toDomain() {
        return MealPrep.builder()
                .title(title)
                .estimatedCookingTime(estimatedCookingTime)
                .servings(servings)
                .freeze(freeze)
                .steps(steps.stream().map(StepResponseGeminiModel::toDomain).toList())
                .ingredients(ingredients.stream().map(IngredientResponseGeminiModel::toDomain).toList())
                .build();
    }
}
