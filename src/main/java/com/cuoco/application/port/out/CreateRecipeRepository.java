package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;

public interface CreateRecipeRepository {
    Recipe execute(Recipe recipe);
}
