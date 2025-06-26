package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.utils.ImageConstants;
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
public class RecipeResponseGeminiModel {
    private String id;
    private String name;
    private String subtitle;
    private String description;
    private String instructions;
    private String image;
    private PreparationTimeResponseGeminiModel preparationTime;
    private CookLevelResponseGeminiModel cookLevel;
    private DietResponseGeminiModel diet;
    private List<MealTypeResponseGeminiModel> mealTypes;
    private List<AllergyResponseGeminiModel> allergies;
    private List<DietaryNeedResponseGeminiModel> dietaryNeeds;
    private List<IngredientResponseGeminiModel> ingredients;

    public Recipe toDomain() {
        return Recipe.builder()
                .name(name)
                .subtitle(subtitle)
                .description(description)
                .instructions(instructions)
                .image(ImageConstants.MAIN_IMAGE_NAME.getValue())
                .preparationTime(preparationTime.toDomain())
                .cookLevel(cookLevel.toDomain())
                .diet(diet != null ? diet.toDomain() : null)
                .mealTypes(mealTypes != null ? mealTypes.stream().map(MealTypeResponseGeminiModel::toDomain).toList() : List.of())
                .allergies(allergies != null ? allergies.stream().map(AllergyResponseGeminiModel::toDomain).toList() : List.of())
                .dietaryNeeds(dietaryNeeds != null ? dietaryNeeds.stream().map(DietaryNeedResponseGeminiModel::toDomain).toList() : List.of())
                .ingredients(ingredients.stream().map(IngredientResponseGeminiModel::toDomain).toList())
                .build();
    }
}
