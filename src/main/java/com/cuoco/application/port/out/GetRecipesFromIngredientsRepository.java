package com.cuoco.application.port.out;

import java.util.List;

public interface GetRecipesFromIngredientsRepository {
    String execute(List<String> ingredients);
}
