package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.DietaryNeed;

import java.util.List;

public interface GetDietaryNeedsByIdRepository {
    List<DietaryNeed> execute(List<Integer> dietaryNeedIds);
}
