package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MealPrep {

    private Long id;
    private String title;
    private User user;
    private String estimatedCookingTime;
    private Integer servings;
    private Boolean freeze;
    private List<Step> steps;

    private List<Ingredient> ingredients;
    private List<Recipe> recipes;

    private Filters filters;
    private MealPrepConfiguration configuration;

}
