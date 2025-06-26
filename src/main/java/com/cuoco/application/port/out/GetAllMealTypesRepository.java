package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.MealType;

import java.util.List;

public interface GetAllMealTypesRepository {
    List<MealType> execute();
}
