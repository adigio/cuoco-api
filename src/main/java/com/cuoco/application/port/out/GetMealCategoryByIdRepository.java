package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.MealCategory;
import com.cuoco.application.usecase.model.MealType;

public interface GetMealCategoryByIdRepository {
    MealCategory execute(Integer id);
}
