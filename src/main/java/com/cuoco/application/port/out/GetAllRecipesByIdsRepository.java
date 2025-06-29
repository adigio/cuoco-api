package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Recipe;

import java.util.List;

public interface GetAllRecipesByIdsRepository {
    List<Recipe> execute(List<Long> ids);
}
