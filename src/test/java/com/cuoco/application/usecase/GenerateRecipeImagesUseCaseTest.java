package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GenerateRecipeImagesCommand;
import com.cuoco.application.port.out.GenerateRecipeImagesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeImage;
import com.cuoco.factory.domain.RecipeFactory;
import com.cuoco.factory.domain.RecipeImageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateRecipeImagesUseCaseTest {

    @Mock
    private GenerateRecipeImagesRepository generateRecipeImagesRepository;

    private GenerateRecipeImagesUseCase generateRecipeImagesUseCase;

    @BeforeEach
    void setUp() {
        generateRecipeImagesUseCase = new GenerateRecipeImagesUseCase(generateRecipeImagesRepository);
    }

    @Test
    void execute_whenValidRecipe_thenReturnGeneratedImages() {
        Recipe recipe = RecipeFactory.create();
        List<RecipeImage> expectedImages = List.of(
                RecipeImageFactory.createMainRecipeImage(),
                RecipeImageFactory.createStepRecipeImage()
        );

        GenerateRecipeImagesCommand.Command command = GenerateRecipeImagesCommand.Command.builder()
                .recipe(recipe)
                .build();

        when(generateRecipeImagesRepository.execute(any(Recipe.class)))
                .thenReturn(expectedImages);

        List<RecipeImage> result = generateRecipeImagesUseCase.execute(command);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(generateRecipeImagesRepository).execute(recipe);
    }

    @Test
    void execute_whenRepositoryReturnsNull_thenReturnEmptyList() {
        Recipe recipe = RecipeFactory.create();
        GenerateRecipeImagesCommand.Command command = GenerateRecipeImagesCommand.Command.builder()
                .recipe(recipe)
                .build();

        when(generateRecipeImagesRepository.execute(any(Recipe.class)))
                .thenReturn(null);

        List<RecipeImage> result = generateRecipeImagesUseCase.execute(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(generateRecipeImagesRepository).execute(recipe);
    }

    @Test
    void execute_whenRepositoryThrowsException_thenReturnEmptyList() {
        Recipe recipe = RecipeFactory.create();
        GenerateRecipeImagesCommand.Command command = GenerateRecipeImagesCommand.Command.builder()
                .recipe(recipe)
                .build();

        when(generateRecipeImagesRepository.execute(any(Recipe.class)))
                .thenThrow(new RuntimeException("Test exception"));

        List<RecipeImage> result = generateRecipeImagesUseCase.execute(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(generateRecipeImagesRepository).execute(recipe);
    }

    @Test
    void execute_whenRepositoryReturnsEmptyList_thenReturnEmptyList() {
        Recipe recipe = RecipeFactory.create();
        GenerateRecipeImagesCommand.Command command = GenerateRecipeImagesCommand.Command.builder()
                .recipe(recipe)
                .build();

        when(generateRecipeImagesRepository.execute(any(Recipe.class)))
                .thenReturn(List.of());

        List<RecipeImage> result = generateRecipeImagesUseCase.execute(command);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(generateRecipeImagesRepository).execute(recipe);
    }
} 