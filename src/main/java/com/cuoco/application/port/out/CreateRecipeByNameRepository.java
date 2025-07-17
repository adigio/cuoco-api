package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Recipe;

public interface CreateRecipeByNameRepository {
    Recipe execute(String name, ParametricData parametricData);
}
