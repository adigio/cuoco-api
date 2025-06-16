package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GetIngredientsFromAudioAsyncRepository {
    CompletableFuture<List<Ingredient>> execute(String audioBase64, String format, String language);
}
