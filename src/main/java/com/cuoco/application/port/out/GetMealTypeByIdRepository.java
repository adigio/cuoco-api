package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.MealType;

public interface GetMealTypeByIdRepository {
    MealType execute(Integer id);
}
