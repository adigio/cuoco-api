package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;

public interface GetIngredientsFromAudioRepository {
    List<Ingredient> execute(String audioBase64, String format, String language);

}