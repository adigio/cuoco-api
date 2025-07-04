package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.MealPrep;

import java.util.List;

public interface GetAllUserMealPrepsQuery {
    List<MealPrep> execute();
}
