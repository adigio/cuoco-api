package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Recipe {
    private Long id;
    private String name;
    private String image;
    private String subtitle;
    private String description;
    private String instructions;
    private String preparationTime;
    private CookLevel cookLevel;
    private List<Ingredient> ingredients;
    private RecipeFilter filters;
}
