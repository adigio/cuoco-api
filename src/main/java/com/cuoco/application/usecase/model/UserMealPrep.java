package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMealPrep {
    private User user;
    private MealPrep mealPrep;
}
