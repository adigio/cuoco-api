package com.cuoco.domain.port.repository;

import java.util.List;

public interface GetRecipesFromIngredientsRepository {
    String execute(List<String> ingredients);
}
