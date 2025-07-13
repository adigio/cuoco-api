package com.cuoco.application.usecase;

import com.cuoco.application.exception.RecipeGenerationException;
import com.cuoco.application.port.in.FindOrCreateRecipeCommand;
import com.cuoco.application.port.out.CreateRecipeByNameRepository;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.FindRecipeByNameRepository;
import com.cuoco.application.usecase.domainservice.RecipeDomainService;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindOrGenerateRecipeUseCaseTest {

    @Mock
    private CreateRecipeByNameRepository createRecipeByNameRepository;

    @Mock
    private FindRecipeByNameRepository findRecipeByNameRepository;

    @Mock
    private CreateRecipeRepository createRecipeRepository;

    @Mock
    private RecipeDomainService recipeDomainService;

    private FindOrCreateRecipeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindOrCreateRecipeUseCase(
                createRecipeByNameRepository,
                findRecipeByNameRepository,
                createRecipeRepository,
                recipeDomainService
        );
    }

    @Test
    void GIVEN_existing_recipe_name_WHEN_execute_THEN_return_recipe_from_database() {
        // Given
        String recipeName = "Pasta Bolognesa";
        Recipe existingRecipe = RecipeFactory.create();
        existingRecipe.setName(recipeName);

        when(findRecipeByNameRepository.execute(recipeName)).thenReturn(existingRecipe);

        FindOrCreateRecipeCommand.Command command = FindOrCreateRecipeCommand.Command.builder()
                .recipeName(recipeName)
                .build();

        // When
        Recipe result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(recipeName, result.getName());
        assertEquals(existingRecipe.getId(), result.getId());

        verify(findRecipeByNameRepository).execute(recipeName);
        verify(createRecipeByNameRepository, never()).execute(any(), any());
        verify(createRecipeRepository, never()).execute(any());
    }

    @Test
    void GIVEN_non_existing_recipe_name_WHEN_execute_THEN_generate_and_save_new_recipe() {
        // Given
        String recipeName = "Pizza Margherita";
        Recipe generatedRecipe = RecipeFactory.create();
        Recipe savedRecipe = RecipeFactory.create();
        savedRecipe.setName(recipeName);

        when(findRecipeByNameRepository.execute(recipeName)).thenReturn(null);
        when(createRecipeByNameRepository.execute(any(), any())).thenReturn(generatedRecipe);
        when(createRecipeRepository.execute(any())).thenReturn(savedRecipe);

        FindOrCreateRecipeCommand.Command command = FindOrCreateRecipeCommand.Command.builder()
                .recipeName(recipeName)
                .build();

        // When
        Recipe result = useCase.execute(command);

        // Then
        assertNotNull(result);
        assertEquals(recipeName, result.getName());

        verify(findRecipeByNameRepository).execute(recipeName);
        verify(createRecipeByNameRepository).execute(any(), any());
        verify(createRecipeRepository).execute(any());
    }

    @Test
    void GIVEN_non_existing_recipe_and_generation_fails_WHEN_execute_THEN_throw_exception() {
        // Given
        String recipeName = "Impossible Recipe";

        when(findRecipeByNameRepository.execute(recipeName)).thenReturn(null);
        when(createRecipeByNameRepository.execute(any(), any())).thenReturn(null);

        FindOrCreateRecipeCommand.Command command = FindOrCreateRecipeCommand.Command.builder()
                .recipeName(recipeName)
                .build();

        // When & Then
        RecipeGenerationException exception = assertThrows(RecipeGenerationException.class, () -> useCase.execute(command));

        assertEquals("Could not generate recipe for: " + recipeName, exception.getDescription());

        verify(findRecipeByNameRepository).execute(recipeName);
        verify(createRecipeByNameRepository).execute(any(), any());
        verify(createRecipeRepository, never()).execute(any());
    }

    @Test
    void GIVEN_recipe_name_with_whitespace_WHEN_execute_THEN_search_with_trimmed_name() {
        // Given
        String recipeNameWithSpaces = "  Lasagna Bolognesa  ";
        String trimmedName = recipeNameWithSpaces.trim();
        Recipe existingRecipe = RecipeFactory.create();
        existingRecipe.setName(trimmedName);

        when(findRecipeByNameRepository.execute(recipeNameWithSpaces)).thenReturn(existingRecipe);

        FindOrCreateRecipeCommand.Command command = FindOrCreateRecipeCommand.Command.builder()
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