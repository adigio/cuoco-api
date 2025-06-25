package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MealPrep {
    private Long id;
    private String name;
    private String image;
    private String subtitle;
    private List<String> recipes;
    private List<Instruction> instructions;
    private PreparationTime preparationTime;
    private CookLevel cookLevel;
    private Diet diet;
    private List<MealType> mealTypes;
    private List<Allergy> allergies;
    private List<DietaryNeed> dietaryNeeds;
    private List<Ingredient> ingredients;
    
    private MealPrepFilter filters;
}
