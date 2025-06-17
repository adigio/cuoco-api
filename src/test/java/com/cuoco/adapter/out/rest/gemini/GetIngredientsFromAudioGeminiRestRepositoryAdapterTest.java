package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.factory.gemini.GeminiResponseModelFactory;
import com.cuoco.factory.gemini.IngredientResponseGeminiModelFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetIngredientsFromAudioGeminiRestRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GetIngredientsFromAudioGeminiRestRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(adapter, "url", "https://gemini.api");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(adapter, "temperature", 0.7);
        ReflectionTestUtils.setField(adapter, "VOICE_PROMPT", "Recognize ingredients from audio: %s");
    }

    @Test
    void GIVEN_valid_audio_WHEN_execute_THEN_return_ingredients() throws Exception {
        IngredientResponseGeminiModel ingredientResponse = IngredientResponseGeminiModelFactory.create("Tomate");
        List<Ingredient> expectedIngredients = List.of(ingredientResponse.toDomain());

        String responseJson = new ObjectMapper().writeValueAsString(List.of(ingredientResponse));
        GeminiResponseModel responseModel = GeminiResponseModelFactory.create(responseJson);

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(responseModel);

        List<Ingredient> result = adapter.execute("audioBase64", "mp3", "en");

        assertNotNull(result);
        assertEquals(expectedIngredients.size(), result.size());
        assertEquals(expectedIngredients.get(0).getName(), result.get(0).getName());
    }

    @Test
    void GIVEN_invalid_json_WHEN_execute_THEN_throw_UnprocessableException() {
        GeminiResponseModel responseModel = GeminiResponseModelFactory.create("INVALID JSON");

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(responseModel);

        UnprocessableException exception = assertThrows(UnprocessableException.class, () ->
                adapter.execute("audioBase64", "mp3", "en"));

        assertTrue(exception.getDescription().contains("Error processing voice"));
    }

    @Test
    void GIVEN_restTemplate_throws_WHEN_execute_THEN_throw_UnprocessableException() {
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenThrow(new RuntimeException("Timeout"));

        UnprocessableException exception = assertThrows(UnprocessableException.class, () ->
                adapter.execute("audioBase64", "mp3", "en"));

        assertTrue(exception.getDescription().contains("Error processing voice"));
    }
}
