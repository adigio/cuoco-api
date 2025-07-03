package com.cuoco.adapter.in.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeCalendarRequest {
    private Long recipeId;
    private Integer mealTypeId;
}
