package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromVoiceCommand;
import com.cuoco.application.port.out.GetIngredientsFromVoiceRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetIngredientsFromVoiceUseCaseUnitTest {

    @Mock
    private GetIngredientsFromVoiceRepository getIngredientsFromVoiceRepository;

    private GetIngredientsFromVoiceUseCase getIngredientsFromVoiceUseCase;

    @BeforeEach
    void setUp() {
        getIngredientsFromVoiceUseCase = new GetIngredientsFromVoiceUseCase(getIngredientsFromVoiceRepository);
    }

    @Test
    void test1_execute_shouldProcessVoiceAndReturnIngredients() {
        // Given
        String audioBase64 = "base64AudioData";
        String format = "mp3";
        String language = "es-ES";
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        List<Ingredient> expectedIngredients = List.of(
                new Ingredient("tomate", "voz", false),
                new Ingredient("cebolla", "voz", false),
                new Ingredient("ajo", "voz", false)
        );

        when(getIngredientsFromVoiceRepository.processVoice(audioBase64, format, language))
                .thenReturn(expectedIngredients);

        // When
        List<Ingredient> result = getIngredientsFromVoiceUseCase.execute(command);

        // Then
        assertEquals(expectedIngredients, result);
        assertEquals(3, result.size());
        assertEquals("tomate", result.get(0).getName());
        assertEquals("voz", result.get(0).getSource());
        assertFalse(result.get(0).isConfirmed());

        verify(getIngredientsFromVoiceRepository).processVoice(audioBase64, format, language);
    }

    @Test
    void test2_execute_shouldHandleEmptyIngredientsResponse() {
        // Given
        String audioBase64 = "base64AudioData";
        String format = "wav";
        String language = "es-ES";
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        List<Ingredient> emptyIngredients = List.of();

        when(getIngredientsFromVoiceRepository.processVoice(audioBase64, format, language))
                .thenReturn(emptyIngredients);

        // When
        List<Ingredient> result = getIngredientsFromVoiceUseCase.execute(command);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(getIngredientsFromVoiceRepository).processVoice(audioBase64, format, language);
    }

    @Test
    void test3_executeAsync_shouldProcessVoiceAsynchronously() {
        // Given
        String audioBase64 = "base64AudioData";
        String format = "ogg";
        String language = "en-US";
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        List<Ingredient> expectedIngredients = List.of(
                new Ingredient("tomato", "voz", false),
                new Ingredient("onion", "voz", false)
        );

        CompletableFuture<List<Ingredient>> futureIngredients = CompletableFuture.completedFuture(expectedIngredients);

        when(getIngredientsFromVoiceRepository.processVoiceAsync(audioBase64, format, language))
                .thenReturn(futureIngredients);

        // When
        CompletableFuture<List<Ingredient>> result = getIngredientsFromVoiceUseCase.executeAsync(command);

        // Then
        assertNotNull(result);
        assertTrue(result.isDone());
        assertEquals(expectedIngredients, result.join());

        verify(getIngredientsFromVoiceRepository).processVoiceAsync(audioBase64, format, language);
    }

    @Test
    void test4_execute_shouldHandleDifferentAudioFormats() {
        // Given
        String audioBase64 = "base64AudioData";
        String format = "flac";
        String language = "es-AR";
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        List<Ingredient> expectedIngredients = List.of(
                new Ingredient("palta", "voz", false),
                new Ingredient("choclo", "voz", false)
        );

        when(getIngredientsFromVoiceRepository.processVoice(audioBase64, format, language))
                .thenReturn(expectedIngredients);

        // When
        List<Ingredient> result = getIngredientsFromVoiceUseCase.execute(command);

        // Then
        assertEquals(expectedIngredients, result);
        assertEquals("palta", result.get(0).getName());
        assertEquals("choclo", result.get(1).getName());

        verify(getIngredientsFromVoiceRepository).processVoice(audioBase64, format, language);
    }

    @Test
    void test5_executeAsync_shouldHandleRepositoryException() {
        // Given
        String audioBase64 = "base64AudioData";
        String format = "mp3";
        String language = "es-ES";
        GetIngredientsFromVoiceCommand.Command command =
                new GetIngredientsFromVoiceCommand.Command(audioBase64, format, language);

        CompletableFuture<List<Ingredient>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Repository error"));

        when(getIngredientsFromVoiceRepository.processVoiceAsync(audioBase64, format, language))
                .thenReturn(failedFuture);

        // When
        CompletableFuture<List<Ingredient>> result = getIngredientsFromVoiceUseCase.executeAsync(command);

        // Then
        assertNotNull(result);
        assertTrue(result.isCompletedExceptionally());

        verify(getIngredientsFromVoiceRepository).processVoiceAsync(audioBase64, format, language);
    }
}