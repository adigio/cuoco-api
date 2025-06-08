package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.DietaryNeed;

import java.util.List;

public interface FindDietaryNeedsByDescriptionRepository {
    List<DietaryNeed> execute(List<String> descriptions);
}
