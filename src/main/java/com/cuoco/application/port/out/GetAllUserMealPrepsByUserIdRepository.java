package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserMealPrep;

import java.util.List;

public interface GetAllUserMealPrepsByUserIdRepository {
    List<UserMealPrep> execute(Long userId);
}
