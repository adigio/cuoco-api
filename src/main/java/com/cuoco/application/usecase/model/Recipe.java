package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Recipe {
    private Long id;
    private String name;
    private String subtitle;
    private String description;
    private String instructions;
    private String image;
    private PreparationTime preparationTime;
    private MealType type;
    private CookLevel cookLevel;
    private List<MealCategory> categories;
    private List<Ingredient> ingredients;

    private RecipeFilter filters;
    private RecipeConfiguration configuration;
}
