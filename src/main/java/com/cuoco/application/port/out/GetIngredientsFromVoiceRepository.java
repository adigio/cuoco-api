package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GetIngredientsFromVoiceRepository {


    List<Ingredient> processVoice(String audioBase64, String format, String language);


    CompletableFuture<List<Ingredient>> processVoiceAsync(String audioBase64, String format, String language);
}