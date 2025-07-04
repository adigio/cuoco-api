package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserMealPrep;

public interface ExistsUserMealPrepRepository {
    boolean execute(UserMealPrep userMealPrep);
}
