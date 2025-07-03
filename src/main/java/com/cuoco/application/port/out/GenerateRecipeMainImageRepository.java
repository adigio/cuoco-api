package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;

public interface GenerateRecipeMainImageRepository {
    boolean execute(Recipe recipe);
}
