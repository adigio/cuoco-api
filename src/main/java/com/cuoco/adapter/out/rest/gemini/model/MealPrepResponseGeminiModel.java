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
    private String id;
    private String name;
    private String subtitle;
    private List<String> recipes;
    private List<InstructionResponseGeminiModel> instructions;
    private String preparationTime;
    private CookLevelResponseGeminiModel cookLevel;
    private List<IngredientResponseGeminiModel> ingredients;

    public MealPrep toDomain() {
        return MealPrep.builder()
                .name(name)
                .subtitle(subtitle)
                .recipes(recipes)
                .instructions(instructions.stream().map(InstructionResponseGeminiModel::toDomain).toList())
                .preparationTime(preparationTime)
                .ingredients(ingredients.stream().map(IngredientResponseGeminiModel::toDomain).toList())
                .cookLevel(cookLevel.toDomain())
                .build();
    }
}
