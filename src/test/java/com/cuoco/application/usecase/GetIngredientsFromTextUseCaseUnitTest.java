package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.port.out.GetIngredientsFromTextRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetIngredientsFromTextUseCase - Simple TDD")
class GetIngredientsFromTextUseCaseUnitTest {

    @Mock
    private GetIngredientsFromTextRepository repository;

    private GetIngredientsFromTextUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetIngredientsFromTextUseCase(repository);
    }

    @Test
    @DisplayName("Test 1: Should parse simple comma-separated text")
    void test1_shouldParseSimpleCommaSeparatedText() {
        // Given - SIMPLE: solo split por comas
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command("tomate, cebolla, ajo", "manual");

        List<Ingredient> expected = Arrays.asList(
                new Ingredient("tomate", "text", false),
                new Ingredient("cebolla", "text", false),
                new Ingredient("ajo", "text", false)
        );

        when(repository.execute("tomate, cebolla, ajo")).thenReturn(expected);

        // When
        List<Ingredient> result = useCase.execute(command);

        // Then
        assertEquals(3, result.size());
        assertEquals("tomate", result.get(0).getName());
        assertEquals("text", result.get(0).getSource());
        assertFalse(result.get(0).isConfirmed());

        verify(repository, times(1)).execute("tomate, cebolla, ajo");
    }

    @Test
    @DisplayName("Test 2: Should handle empty text")
    void test2_shouldHandleEmptyText() {
        // Given
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command("", "manual");

        when(repository.execute("")).thenReturn(Arrays.asList());

        // When
        List<Ingredient> result = useCase.execute(command);

        // Then
        assertTrue(result.isEmpty());
        verify(repository, times(1)).execute("");
    }

    @Test
    @DisplayName("Test 3: Should handle single ingredient")
    void test3_shouldHandleSingleIngredient() {
        // Given
        GetIngredientsFromTextCommand.Command command =
                new GetIngredientsFromTextCommand.Command("tomate", "manual");

        List<Ingredient> expected = Arrays.asList(
                new Ingredient("tomate", "text", false)
        );

        when(repository.execute("tomate")).thenReturn(expected);

        // When
        List<Ingredient> result = useCase.execute(command);

        // Then
        assertEquals(1, result.size());
        assertEquals("tomate", result.get(0).getName());
        verify(repository, times(1)).execute("tomate");
    }
}