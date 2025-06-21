package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.UserRecipe;

public interface GetRecipeByIdRepository {

    Recipe execute(long id);

}
