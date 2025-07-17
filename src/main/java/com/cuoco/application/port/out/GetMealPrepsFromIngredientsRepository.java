package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.MealPrep;

import java.util.List;

public interface GetMealPrepsFromIngredientsRepository {
    List<MealPrep> execute(MealPrep mealPrep);
}
