package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.MealPrep;

import java.util.List;

public interface GetAllMealPrepsByIdsRepository {
    List<MealPrep> execute(List<Long> ids);
}
