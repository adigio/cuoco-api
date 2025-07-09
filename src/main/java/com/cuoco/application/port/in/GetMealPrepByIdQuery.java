package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.MealPrep;

public interface GetMealPrepByIdQuery {
    MealPrep execute(Long id);
}
