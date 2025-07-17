package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.MealPrep;

public interface GetMealPrepByIdRepository {
    MealPrep execute(Long id);
}
