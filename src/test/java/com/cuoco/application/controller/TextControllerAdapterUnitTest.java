package com.cuoco.application.controller;

import com.cuoco.adapter.in.controller.TextControllerAdapter;
import com.cuoco.adapter.in.controller.model.TextRequest;
import com.cuoco.adapter.in.controller.model.IngredientsResponse;
import com.cuoco.adapter.in.controller.model.IngredientsResponseMapper;
import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TextControllerAdapter - TDD")
class TextControllerAdapterUnitTest {

    @Mock
    private GetIngredientsFromTextCommand getIngredientsFromTextCommand;

    @Mock
    private IngredientsResponseMapper ingredientsResponseMapper;

    private TextControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new TextControllerAdapter(getIngredientsFromTextCommand, ingredientsResponseMapper);
    }

    @Test
    @DisplayName("Test 1: Should process text successfully")
    void test1_shouldProcessTextSuccessfully() {
        // Given
        TextRequest request = new TextRequest();
        request.setText("tomate, cebolla, ajo");
        request.setSource("manual");

        List<Ingredient> domainIngredients = Arrays.asList(
                new Ingredient("tomate", "text", false),
                new Ingredient("cebolla", "text", false),
                new Ingredient("ajo", "text", false)
        );

        IngredientsResponse expectedResponse = new IngredientsResponse(domainIngredients);

        when(getIngredientsFromTextCommand.execute(any(GetIngredientsFromTextCommand.Command.class)))
                .thenReturn(domainIngredients);
        when(ingredientsResponseMapper.toResponse(domainIngredients)).thenReturn(expectedResponse);

        // When
        ResponseEntity<?> response = controller.processText(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(getIngredientsFromTextCommand, times(1)).execute(argThat(command -> {
            assertEquals("tomate, cebolla, ajo", command.getText());
            assertEquals("manual", command.getSource());
            return true;
        }));

        // Verify Adapter Layer mapping
        verify(ingredientsResponseMapper, times(1)).toResponse(domainIngredients);
    }

    @Test
    @DisplayName("Test 2: Should handle empty text")
    void test2_shouldHandleEmptyText() {
        // Given
        TextRequest request = new TextRequest();
        request.setText("");
        request.setSource("manual");

        when(getIngredientsFromTextCommand.execute(any())).thenReturn(Collections.emptyList());
        when(ingredientsResponseMapper.toResponse(Collections.emptyList())).thenReturn(new IngredientsResponse(Collections.emptyList()));

        // When
        ResponseEntity<?> response = controller.processText(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(getIngredientsFromTextCommand, times(1)).execute(any());
    }

    @Test
    @DisplayName("Test 3: Should handle text processing exceptions")
    void test3_shouldHandleTextProcessingExceptions() {
        // Given
        TextRequest request = new TextRequest();
        request.setText("invalid%%%text");
        request.setSource("manual");

        when(getIngredientsFromTextCommand.execute(any()))
                .thenThrow(new RuntimeException("Text processing failed"));

        // When
        ResponseEntity<?> response = controller.processText(request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error al procesar el texto"));
        verify(getIngredientsFromTextCommand, times(1)).execute(any());
    }
}