package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.application.port.out.GetIngredientsFromAudioRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.factory.domain.IngredientFactory;
import com.cuoco.shared.model.ErrorDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetIngredientsFromAudioAsyncGeminiRestRepositoryAdapterTest {

    private GetIngredientsFromAudioRepository getIngredientsFromAudioRepository;

    private GetIngredientsFromAudioAsyncGeminiRestRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        getIngredientsFromAudioRepository = mock(GetIngredientsFromAudioRepository.class);
        adapter = new GetIngredientsFromAudioAsyncGeminiRestRepositoryAdapter(getIngredientsFromAudioRepository);
    }

    @Test
    void GIVEN_valid_audio_WHEN_execute_THEN_return_ingredients_async() throws Exception {
        String audioBase64 = "base64-audio";
        String format = "mp3";
        String language = "en";

        List<Ingredient> expectedIngredients = List.of(
                IngredientFactory.create("Tomato", 1.0, "ud"),
                IngredientFactory.create("Onion", 2.0, "ud")
        );

        when(getIngredientsFromAudioRepository.execute(audioBase64, format, language))
                .thenReturn(expectedIngredients);

        CompletableFuture<List<Ingredient>> future = adapter.execute(audioBase64, format, language);

        assertNotNull(future);
        List<Ingredient> result = future.get();

        assertEquals(expectedIngredients, result);
        verify(getIngredientsFromAudioRepository).execute(audioBase64, format, language);
    }

    @Test
    void GIVEN_repository_throws_exception_WHEN_execute_THEN_throw_UnprocessableException() {
        String audioBase64 = "invalid-audio";
        String format = "mp3";
        String language = "en";

        when(getIngredientsFromAudioRepository.execute(audioBase64, format, language))
                .thenThrow(new RuntimeException("Failed to process audio"));

        CompletableFuture<List<Ingredient>> future = adapter.execute(audioBase64, format, language);

        ExecutionException ex = assertThrows(ExecutionException.class, future::get);

        assertTrue(ex.getCause() instanceof UnprocessableException);
        assertEquals(ErrorDescription.AUDIO_FILE_PROCESSING_ERROR.getValue(),
                ((UnprocessableException) ex.getCause()).getDescription());

        verify(getIngredientsFromAudioRepository).execute(audioBase64, format, language);
    }
}
