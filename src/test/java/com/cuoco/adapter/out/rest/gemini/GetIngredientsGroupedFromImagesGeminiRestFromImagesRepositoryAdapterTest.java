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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class
)
class GetIngredientsGroupedFromImagesGeminiRestFromImagesRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GetIngredientsGroupedFromImagesGeminiRestFromImagesRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(adapter, "url", "https://gemini.api");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(adapter, "temperature", 0.7);
        ReflectionTestUtils.setField(adapter, "PROMPT", "Recognize ingredients from this image:");
    }

    @Test
    void GIVEN_valid_images_WHEN_execute_THEN_return_grouped_ingredients() throws Exception {
        IngredientResponseGeminiModel ingredientResponse = IngredientResponseGeminiModelFactory.create("Cheese");
        List<Ingredient> expected = List.of(ingredientResponse.toDomain());

        String jsonResponse = new ObjectMapper().writeValueAsString(List.of(ingredientResponse));
        GeminiResponseModel geminiResponseModel = GeminiResponseModelFactory.create(jsonResponse);

        File image1 = FileModelFactory.create("image1.png", "image/png", "base64data1");
        File image2 = FileModelFactory.create("image2.jpg", "image/jpeg", "base64data2");

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponseModel);

        Map<String, List<Ingredient>> result = adapter.execute(List.of(image1, image2));

        assertEquals(2, result.size());
        assertTrue(result.containsKey("image1.png"));
        assertTrue(result.containsKey("image2.jpg"));

        for (List<Ingredient> ingredients : result.values()) {
            assertEquals(1, ingredients.size());
            assertEquals("Cheese", ingredients.get(0).getName());
            assertEquals("image", ingredients.get(0).getSource());
        }
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
