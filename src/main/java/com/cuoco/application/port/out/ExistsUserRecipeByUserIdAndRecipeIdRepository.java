package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.UserRecipe;

public interface ExistsUserRecipeByUserIdAndRecipeIdRepository {
    boolean execute(UserRecipe userRecipe);
}
