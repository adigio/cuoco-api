package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserDietaryNeeds;

public interface CreateUserDietaryRepository {
    UserDietaryNeeds execute(UserDietaryNeeds UserDietaryNeeds);
}
