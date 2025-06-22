package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeMealCategory {
    private Recipe recipe;
    private MealCategory category;
}