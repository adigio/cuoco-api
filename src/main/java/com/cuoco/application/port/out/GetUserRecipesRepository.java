package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserRecipe;

import java.util.List;

public interface GetUserRecipesRepository {
    List<UserRecipe> execute(long userId);
}
