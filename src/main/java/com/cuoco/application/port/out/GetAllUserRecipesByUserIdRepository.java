package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserRecipe;

import java.util.List;

public interface GetAllUserRecipesByUserIdRepository {
    List<UserRecipe> execute(Long userId);
}
