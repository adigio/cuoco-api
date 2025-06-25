package com.cuoco.application.usecase;

import com.cuoco.application.exception.RecipeGenerationException;
import com.cuoco.application.port.in.FindOrGenerateRecipeCommand;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.FindRecipeByNameRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindOrGenerateRecipeUseCaseTest {

    @Mock
    private FindRecipeByNameRepository findRecipeByNameRepository;

    @Mock
    private GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;

    @Mock
    private CreateRecipeRepository createRecipeRepository;

    private FindOrGenerateRecipeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindOrGenerateRecipeUseCase(
                findRecipeByNameRepository,
                getRecipesFromIngredientsCommand,
                createRecipeRepository
        );
    }

    @Test
    void GIVEN_existing_recipe_name_WHEN_execute_THEN_return_recipe_from_database() {
        // Given
        String recipeName = "Pasta Bolognesa";
        Recipe existingRecipe = RecipeFactory.create();
        existingRecipe.setName(recipeName);

        when(findRecipeByNameRepository.execute(recipeName)).thenReturn(Optional.of(existingRecipe));

        FindOrGenerateRecipeCommand.Command command = FindOrGenerateRecipeCommand.Command.builder()
                .recipeName(recipeName)
                .build();

        // When
        Recipe result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(recipeName, result.getName());
        assertEquals(existingRecipe.getId(), result.getId());
        
        verify(findRecipeByNameRepository).execute(recipeName);
        verify(getRecipesFromIngredientsCommand, never()).execute(any());
        verify(createRecipeRepository, never()).execute(any());
    }

    @Test
    void GIVEN_non_existing_recipe_name_WHEN_execute_THEN_generate_and_save_new_recipe() {
        // Given
        String recipeName = "Pizza Margherita";
        Recipe generatedRecipe = RecipeFactory.create();
        Recipe savedRecipe = RecipeFactory.create();
        savedRecipe.setName(recipeName);

        when(findRecipeByNameRepository.execute(recipeName)).thenReturn(Optional.empty());
        when(getRecipesFromIngredientsCommand.execute(any())).thenReturn(List.of(generatedRecipe));
        when(createRecipeRepository.execute(any())).thenReturn(savedRecipe);

        FindOrGenerateRecipeCommand.Command command = FindOrGenerateRecipeCommand.Command.builder()
                .recipeName(recipeName)
                .build();

        // When
        Recipe result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(recipeName, result.getName());
        
        verify(findRecipeByNameRepository).execute(recipeName);
        verify(getRecipesFromIngredientsCommand).execute(any());
        verify(createRecipeRepository).execute(any());
    }

    @Test
    void GIVEN_non_existing_recipe_and_generation_fails_WHEN_execute_THEN_throw_exception() {
        // Given
        String recipeName = "Impossible Recipe";

        when(findRecipeByNameRepository.execute(recipeName)).thenReturn(Optional.empty());
        when(getRecipesFromIngredientsCommand.execute(any())).thenReturn(List.of());

        FindOrGenerateRecipeCommand.Command command = FindOrGenerateRecipeCommand.Command.builder()
                .recipeName(recipeName)
                .build();

        // When & Then
        RecipeGenerationException exception = assertThrows(RecipeGenerationException.class, () -> {
            useCase.execute(command);
        });

        assertEquals("Could not generate recipe for: " + recipeName, exception.getDescription());
        
        verify(findRecipeByNameRepository).execute(recipeName);
        verify(getRecipesFromIngredientsCommand).execute(any());
        verify(createRecipeRepository, never()).execute(any());
    }

    @Test
    void GIVEN_recipe_name_with_whitespace_WHEN_execute_THEN_search_with_trimmed_name() {
        // Given
        String recipeNameWithSpaces = "  Lasagna Bolognesa  ";
        String trimmedName = recipeNameWithSpaces.trim();
        Recipe existingRecipe = RecipeFactory.create();
        existingRecipe.setName(trimmedName);

        when(findRecipeByNameRepository.execute(recipeNameWithSpaces)).thenReturn(Optional.of(existingRecipe));

        FindOrGenerateRecipeCommand.Command command = FindOrGenerateRecipeCommand.Command.builder()
                .recipeName(recipeNameWithSpaces)
                .build();

        // When
        Recipe result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(trimmedName, result.getName());
        
        verify(findRecipeByNameRepository).execute(recipeNameWithSpaces);
    }
} 