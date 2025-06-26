package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;

public interface GetRecipeByIdRepository {

    Recipe execute(long id);

}
