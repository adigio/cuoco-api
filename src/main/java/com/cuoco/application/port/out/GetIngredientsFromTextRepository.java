package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;

public interface GetIngredientsFromTextRepository {
    List<Ingredient> execute(String text);
}