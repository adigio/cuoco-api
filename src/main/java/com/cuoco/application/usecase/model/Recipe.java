package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private Long id;
    private String name;
    private String subtitle;
    private String description;
    private Boolean favorite;
    private List<Step> steps;
    private String image;
    private PreparationTime preparationTime;
    private CookLevel cookLevel;
    private Diet diet;
    private List<MealType> mealTypes;
    private List<Allergy> allergies;
    private List<DietaryNeed> dietaryNeeds;
    private List<Ingredient> ingredients;

    private List<Step> images;

    private Filters filters;
    private RecipeConfiguration configuration;
}
