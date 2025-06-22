package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
