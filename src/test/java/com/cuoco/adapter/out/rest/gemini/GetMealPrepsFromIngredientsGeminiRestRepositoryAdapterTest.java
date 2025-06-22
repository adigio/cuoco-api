package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.rest.gemini.model.MealPrepResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.factory.domain.MealPrepFactory;
import com.cuoco.factory.gemini.GeminiResponseModelFactory;
import com.cuoco.factory.gemini.MealPrepResponseGeminiModelFactory;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMealPrepsFromIngredientsGeminiRestRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GetMealPrepsFromIngredientsGeminiRestRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(adapter, "url", "https://gemini.api");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(adapter, "temperature", 0.7);
        ReflectionTestUtils.setField(adapter, "BASIC_PROMPT", "Generar mealprep para {{INGREDIENTS}}.");
        ReflectionTestUtils.setField(adapter, "FILTERS_PROMPT", " Filtros: nivel {{COOK_LEVEL}}, dieta {{DIET}}, cantidad {{QUANTITY}}, tipos {{FOOD_TYPES}}, congelar {{FREEZE}}.");
    }

    @Test
    void GIVEN_valid_ingredients_WHEN_execute_THEN_return_mealPrep() throws Exception {
        // Arrange
        MealPrep mealPrep = MealPrepFactory.create();

        MealPrepResponseGeminiModel mealPrepResponseModel = MealPrepResponseGeminiModelFactory.create();
        String responseJson = new ObjectMapper().writeValueAsString(List.of(mealPrepResponseModel));
        GeminiResponseModel geminiResponseModel = GeminiResponseModelFactory.create(responseJson);

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponseModel);

        // Act
        List<MealPrep> result = adapter.execute(mealPrep);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mealPrepResponseModel.getName(), result.get(0).getName());
    }

}
