package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.rest.gemini.model.RecipeResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
import com.cuoco.factory.gemini.GeminiResponseModelFactory;
import com.cuoco.factory.gemini.RecipeResponseGeminiModelFactory;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRecipesFromIngredientsGeminiRestRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GetRecipesFromIngredientsGeminiRestRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(adapter, "url", "https://gemini.api");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(adapter, "temperature", 0.7);
        ReflectionTestUtils.setField(adapter, "BASIC_PROMPT", "Generar recetas para {{INGREDIENTS}}, máximo {{MAX_RECIPES}}.");
        ReflectionTestUtils.setField(adapter, "FILTERS_PROMPT", " Filtros: nivel {{COOK_LEVEL}}, tiempo {{COOK_TIME}}, tipos {{FOOD_TYPES}}, dieta {{DIET}}, cantidad {{QUANTITY}}.");
    }

    @Test
    void GIVEN_valid_ingredients_WHEN_execute_THEN_return_recipes() throws Exception {
        Recipe recipe = RecipeFactory.createWithFilters();

        RecipeResponseGeminiModel recipeResponseModel = RecipeResponseGeminiModelFactory.create();
        String responseJson = new ObjectMapper().writeValueAsString(List.of(recipeResponseModel));
        GeminiResponseModel geminiResponseModel = GeminiResponseModelFactory.create(responseJson);

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponseModel);

        List<Recipe> result = adapter.execute(recipe);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recipeResponseModel.getName(), result.get(0).getName());
    }

    @Test
    void GIVEN_null_response_WHEN_execute_THEN_throw_UnprocessableException() {
        Recipe recipe = RecipeFactory.create();

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(null);

        NotAvailableException ex = assertThrows(NotAvailableException.class, () -> adapter.execute(recipe));
        assertEquals(ErrorDescription.NOT_AVAILABLE.getValue(), ex.getDescription());
    }

    @Test
    void GIVEN_invalid_json_WHEN_execute_THEN_throw_NotAvailableException() {
        Recipe recipe = RecipeFactory.create();

        GeminiResponseModel geminiResponseModel = GeminiResponseModelFactory.create("INVALID JSON");

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponseModel);

        assertThrows(NotAvailableException.class, () -> adapter.execute(recipe));
    }
}

