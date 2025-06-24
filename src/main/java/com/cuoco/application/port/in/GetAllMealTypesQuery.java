package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.MealType;

import java.util.List;

public interface GetAllMealTypesQuery {
    List<MealType> execute();
}
