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
    private String image;
    private String subtitle;
    private String description;
    private String instructions;
    private PreparationTimeResponseGeminiModel preparationTime;
    private CookLevelResponseGeminiModel cookLevel;
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
                .image(processedImageUrl)
                .subtitle(subtitle)
                .description(description)
                .instructions(instructions)
                .preparationTime(preparationTime.toDomain())
                .cookLevel(cookLevel.toDomain())
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
