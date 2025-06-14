package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.DietaryNeed;

import java.util.List;

public interface GetAllDietaryNeedsQuery {
    List<DietaryNeed> execute();
}
