package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.RecipeFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetRecipesFromIngredientsUseCase - 5 Unit Tests")
class GetRecipesFromIngredientsUseCaseUnitTest {

    @Mock
    private GetRecipesFromIngredientsRepository repository;

    private GetRecipesFromIngredientsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetRecipesFromIngredientsUseCase(repository);
    }

    @Test
    @DisplayName("Test 1: Should return recipes when valid ingredients provided")
    void test1_shouldReturnRecipesWhenValidIngredientsProvided() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("tomate", "manual", true),
                new Ingredient("cebolla", "manual", true)
        );
        RecipeFilter filter = new RecipeFilter("30 min", "Fácil", Arrays.asList("Italiana"), "Vegetariana", 4);
        GetRecipesFromIngredientsCommand.Command command =
                new GetRecipesFromIngredientsCommand.Command(filter, ingredients);

        String expectedRecipes = "Recipe 1: Pasta italiana\nRecipe 2: Ensalada de tomate";
        when(repository.execute(ingredients)).thenReturn(expectedRecipes);

        // When
        String result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(expectedRecipes, result);
        verify(repository, times(1)).execute(ingredients);
    }

    @Test
    @DisplayName("Test 2: Should handle empty ingredients list")
    void test2_shouldHandleEmptyIngredientsList() {
        // Given
        List<Ingredient> emptyIngredients = Collections.emptyList();
        RecipeFilter filter = new RecipeFilter("15 min", "Fácil", Arrays.asList("Rápida"), null, 2);
        GetRecipesFromIngredientsCommand.Command command =
                new GetRecipesFromIngredientsCommand.Command(filter, emptyIngredients);

        String expectedMessage = "No se pueden generar recetas sin ingredientes";
        when(repository.execute(emptyIngredients)).thenReturn(expectedMessage);

        // When
        String result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(expectedMessage, result);
        verify(repository, times(1)).execute(emptyIngredients);
    }

    @Test
    @DisplayName("Test 3: Should propagate repository exceptions")
    void test3_shouldPropagateRepositoryExceptions() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("ajo", "voice", false)
        );
        GetRecipesFromIngredientsCommand.Command command =
                new GetRecipesFromIngredientsCommand.Command(null, ingredients);

        RuntimeException expectedException = new RuntimeException("API connection failed");
        when(repository.execute(ingredients)).thenThrow(expectedException);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            useCase.execute(command);
        });

        assertEquals("API connection failed", exception.getMessage());
        verify(repository, times(1)).execute(ingredients);
    }

    @Test
    @DisplayName("Test 4: Should handle null filter correctly")
    void test4_shouldHandleNullFilterCorrectly() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("arroz", "camera", true),
                new Ingredient("pollo", "text", true)
        );
        GetRecipesFromIngredientsCommand.Command command =
                new GetRecipesFromIngredientsCommand.Command(null, ingredients);

        String expectedRecipes = "Recipe without specific filters";
        when(repository.execute(ingredients)).thenReturn(expectedRecipes);

        // When
        String result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(expectedRecipes, result);
        verify(repository, times(1)).execute(ingredients);
    }

    @Test
    @DisplayName("Test 5: Should handle mixed ingredient sources and confirmation status")
    void test5_shouldHandleMixedIngredientSourcesAndConfirmationStatus() {
        // Given
        List<Ingredient> mixedIngredients = Arrays.asList(
                new Ingredient("tomate", "camera", true),
                new Ingredient("cebolla", "voice", false),
                new Ingredient("ajo", "text", true),
                new Ingredient("aceite", "manual", false)
        );
        RecipeFilter complexFilter = new RecipeFilter("45 min", "Intermedio",
                Arrays.asList("Mediterránea", "Saludable"), "Omnívora", 6);
        GetRecipesFromIngredientsCommand.Command command =
                new GetRecipesFromIngredientsCommand.Command(complexFilter, mixedIngredients);

        String expectedRecipes = "Complex recipe with mixed ingredients";
        when(repository.execute(mixedIngredients)).thenReturn(expectedRecipes);

        // When
        String result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(expectedRecipes, result);
        verify(repository, times(1)).execute(argThat(ingredientList -> {
            assertEquals(4, ingredientList.size());
            assertEquals("tomate", ingredientList.get(0).getName());
            assertEquals("camera", ingredientList.get(0).getSource());
            assertTrue(ingredientList.get(0).isConfirmed());
            return true;
        }));
    }
}