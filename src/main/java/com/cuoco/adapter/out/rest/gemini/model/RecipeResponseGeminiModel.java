package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.Recipe;
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
        // Process dynamic image URL
        String processedImageUrl = image;
        if (image != null && image.contains("{recipe_name_sanitized}")) {
            String sanitizedName = sanitizeRecipeName(name);
            processedImageUrl = image.replace("{recipe_name_sanitized}", sanitizedName);
        }
        
        return Recipe.builder()
                .name(name)
                .subtitle(subtitle)
                .description(description)
                .instructions(instructions)
                .image(processedImageUrl)
                .preparationTime(preparationTime.toDomain())
                .cookLevel(cookLevel.toDomain())
                .diet(diet != null ? diet.toDomain() : null)
                .mealTypes(mealTypes != null ? mealTypes.stream().map(MealTypeResponseGeminiModel::toDomain).toList() : List.of())
                .allergies(allergies != null ? allergies.stream().map(AllergyResponseGeminiModel::toDomain).toList() : List.of())
                .dietaryNeeds(dietaryNeeds != null ? dietaryNeeds.stream().map(DietaryNeedResponseGeminiModel::toDomain).toList() : List.of())
                .ingredients(ingredients.stream().map(IngredientResponseGeminiModel::toDomain).toList())
                .build();
    }
    
    private String sanitizeRecipeName(String recipeName) {
        if (recipeName == null) return "recipe";
        return recipeName.replaceAll("[^a-zA-Z0-9\\s]", "")
                         .replaceAll("\\s+", "_")
                         .toLowerCase();
    }
}
