package com.cuoco.adapter.in.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRecipeCalendarRequest {
    private Integer dayId;
    private List<RecipeCalendarRequest> recipes;
}
