package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.rest.gemini.model.IngredientResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.factory.domain.FileModelFactory;
import com.cuoco.factory.gemini.GeminiResponseModelFactory;
import com.cuoco.factory.gemini.IngredientResponseGeminiModelFactory;
import com.cuoco.shared.model.ErrorDescription;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetIngredientsFromImageGeminiRestImageRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GetIngredientsFromImageGeminiRestImageRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(adapter, "url", "https://gemini.api");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(adapter, "temperature", 0.7);
        ReflectionTestUtils.setField(adapter, "PROMPT", "Recognize ingredients from this image:");
    }

    @Test
    void GIVEN_valid_image_WHEN_execute_THEN_return_ingredients() throws Exception {
        IngredientResponseGeminiModel ingredientResponse = IngredientResponseGeminiModelFactory.create("Tomate");

        String jsonResponse = new ObjectMapper().writeValueAsString(List.of(ingredientResponse));
        GeminiResponseModel geminiResponseModel = GeminiResponseModelFactory.create(jsonResponse);

        File image = FileModelFactory.create();

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponseModel);

        List<Ingredient> result = adapter.execute(List.of(image));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tomate", result.get(0).getName());
        assertEquals("image", result.get(0).getSource());
    }

    @Test
    void GIVEN_invalid_json_WHEN_execute_THEN_throw_NotAvailableException() {
        GeminiResponseModel geminiResponseModel = GeminiResponseModelFactory.create("INVALID_JSON");

        File image = FileModelFactory.create();

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponseModel);

        NotAvailableException ex = assertThrows(NotAvailableException.class, () ->
                adapter.execute(List.of(image)));

        assertEquals(ErrorDescription.NOT_AVAILABLE.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_restTemplate_throws_WHEN_execute_THEN_throw_NotAvailableException() {
        File image = FileModelFactory.create();

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenThrow(new RuntimeException("Connection error"));

        NotAvailableException ex = assertThrows(NotAvailableException.class, () ->
                adapter.execute(List.of(image)));

        assertEquals(ErrorDescription.NOT_AVAILABLE.getValue(), ex.getDescription());
    }
}
