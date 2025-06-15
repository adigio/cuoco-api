package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeResponseGeminiModel {
    private String id;
    private String name;
    private String preparationTime;
    private String image;
    private String subtitle;
    private String description;
    private List<IngredientResponseGeminiModel> ingredients;
    private String instructions;

    public Recipe toDomain() {
        return Recipe.builder()
                .name(name)
                .preparationTime(preparationTime)
                .image(image)
                .subtitle(subtitle)
                .description(description)
                .ingredients(
                        ingredients
                                .stream()
                                .map(IngredientResponseGeminiModel::toDomain)
                                .toList()
                )
                .instructions(instructions)
                .build();
    }
}
