package com.cuoco.application.controller;

import com.cuoco.adapter.in.controller.RecipeControllerAdapter;
import com.cuoco.adapter.out.rest.model.gemini.GeminiResponseMapper;
import com.cuoco.adapter.in.controller.model.FilterRequest;
import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerAdapterUnitTest {

    @Mock
    private GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;

    @Mock
    private GeminiResponseMapper geminiResponseMapper;

    private RecipeControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new RecipeControllerAdapter(getRecipesFromIngredientsCommand, geminiResponseMapper);
    }

    @Test
    void test1_shouldReturnRecipesWhenValidRequest() {
        // Given
        RecipeRequest request = createValidRecipeRequest();
        String geminiResponse = "[{\"id\":\"1\",\"name\":\"Test Recipe\"}]";
        Object parsedResponse = Arrays.asList("parsed recipe");

        when(getRecipesFromIngredientsCommand.execute(any(GetRecipesFromIngredientsCommand.Command.class)))
                .thenReturn(geminiResponse);
        when(geminiResponseMapper.parseToJson(anyString())).thenReturn(parsedResponse);

        // When
        ResponseEntity<?> response = controller.generate(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parsedResponse, response.getBody());
        verify(getRecipesFromIngredientsCommand).execute(any(GetRecipesFromIngredientsCommand.Command.class));
        verify(geminiResponseMapper).parseToJson(geminiResponse);
    }

    @Test
    void test2_shouldReturnErrorWhenCommandFails() {
        // Given
        RecipeRequest request = createValidRecipeRequest();
        when(getRecipesFromIngredientsCommand.execute(any(GetRecipesFromIngredientsCommand.Command.class)))
                .thenThrow(new RuntimeException("Command failed"));

        // When
        ResponseEntity<?> response = controller.generate(request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error al generar la receta"));
    }

    @Test
    void test3_shouldHandleRequestWithoutFilters() {
        // Given
        RecipeRequest request = new RecipeRequest();
        IngredientRequest ingredient = new IngredientRequest("tomate");
        ingredient.setSource("manual");
        ingredient.setConfirmed(true);
        request.setIngredients(Arrays.asList(ingredient));
        // No filters set

        String geminiResponse = "[]";
        when(getRecipesFromIngredientsCommand.execute(any(GetRecipesFromIngredientsCommand.Command.class)))
                .thenReturn(geminiResponse);
        when(geminiResponseMapper.parseToJson(anyString())).thenReturn(Arrays.asList());

        // When
        ResponseEntity<?> response = controller.generate(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(getRecipesFromIngredientsCommand).execute(argThat(command ->
                command.getFilters() == null && command.getIngredients().size() == 1
        ));
    }

    @Test
    void test4_shouldMapRequestToCommandCorrectly() {
        // Given
        RecipeRequest request = createComplexRecipeRequest();
        when(getRecipesFromIngredientsCommand.execute(any(GetRecipesFromIngredientsCommand.Command.class)))
                .thenReturn("[]");
        when(geminiResponseMapper.parseToJson(anyString())).thenReturn(Arrays.asList());

        // When
        controller.generate(request);

        // Then
        verify(getRecipesFromIngredientsCommand).execute(argThat(command -> {
            return command.getFilters() != null &&
                    command.getFilters().getTime().equals("corto") &&
                    command.getIngredients().size() == 2;
        }));
    }

    @Test
    void test5_shouldHandleMapperFailure() {
        // Given
        RecipeRequest request = createValidRecipeRequest();
        String geminiResponse = "invalid json";

        when(getRecipesFromIngredientsCommand.execute(any(GetRecipesFromIngredientsCommand.Command.class)))
                .thenReturn(geminiResponse);
        when(geminiResponseMapper.parseToJson(anyString())).thenReturn(geminiResponse); // Fallback to string

        // When
        ResponseEntity<?> response = controller.generate(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(geminiResponse, response.getBody());
    }

    private RecipeRequest createValidRecipeRequest() {
        RecipeRequest request = new RecipeRequest();
        IngredientRequest ingredient = new IngredientRequest("tomate");
        ingredient.setSource("imagen");
        ingredient.setConfirmed(true);
        request.setIngredients(Arrays.asList(ingredient));
        return request;
    }

    private RecipeRequest createComplexRecipeRequest() {
        RecipeRequest request = new RecipeRequest();

        IngredientRequest ingredient1 = new IngredientRequest("pasta");
        ingredient1.setSource("manual");
        ingredient1.setConfirmed(true);

        IngredientRequest ingredient2 = new IngredientRequest("queso");
        ingredient2.setSource("imagen");
        ingredient2.setConfirmed(false);

        request.setIngredients(Arrays.asList(ingredient1, ingredient2));

        FilterRequest filter = new FilterRequest();
        filter.setTime("corto");
        filter.setDifficulty("facil");
        filter.setQuantity(4);
        filter.setPeople(4);
        request.setFilters(filter);

        return request;
    }
}