package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserMealPrep;

public interface CreateUserMealPrepRepository {
    void execute(UserMealPrep userMealPrep);
}
