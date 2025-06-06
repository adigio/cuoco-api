package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.DietaryNeeds;

public interface CreateUserDietaryRepository {
    DietaryNeeds execute(DietaryNeeds DietaryNeeds);
}
