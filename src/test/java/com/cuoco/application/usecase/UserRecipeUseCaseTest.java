package com.cuoco.application.usecase;

import com.cuoco.adapter.exception.ConflictException;
import com.cuoco.application.port.in.CreateUserRecipeCommand;
import com.cuoco.application.port.out.CreateUserRecipeRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.ExistsUserRecipeByUserIdAndRecipeIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import com.cuoco.factory.domain.RecipeFactory;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRecipeUseCaseTest {

    private UserDomainService userDomainService;
    private CreateUserRecipeRepository createUserRecipeRepository;
    private ExistsUserRecipeByUserIdAndRecipeIdRepository existsUserRecipeByUserIdAndRecipeIdRepository;
    private GetRecipeByIdRepository getRecipeByIdRepository;

    private CreateUserRecipeUseCase useCase;

    @BeforeEach
    public void setUp() {
        userDomainService = mock(UserDomainService.class);
        createUserRecipeRepository = mock(CreateUserRecipeRepository.class);
        existsUserRecipeByUserIdAndRecipeIdRepository = mock(ExistsUserRecipeByUserIdAndRecipeIdRepository.class);
        getRecipeByIdRepository = mock(GetRecipeByIdRepository.class);

        useCase = new CreateUserRecipeUseCase(
                userDomainService,
                createUserRecipeRepository,
                existsUserRecipeByUserIdAndRecipeIdRepository,
                getRecipeByIdRepository
        );
    }

    @Test
    public void shouldSaveRecipeIfNotExists() {
        // Arrange
        User user = UserFactory.create();
        Long recipeId = 1L;
        Recipe recipe = RecipeFactory.create();
        CreateUserRecipeCommand.Command command = CreateUserRecipeCommand.Command.builder()
                .recipeId(recipeId)
                .build();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        when(getRecipeByIdRepository.execute(recipeId)).thenReturn(recipe);
        when(existsUserRecipeByUserIdAndRecipeIdRepository.execute(any(UserRecipe.class))).thenReturn(false);
        doNothing().when(createUserRecipeRepository).execute(any(UserRecipe.class));

        // Act
        useCase.execute(command);

        // Assert
        verify(createUserRecipeRepository).execute(any(UserRecipe.class));
    }

    @Test
    public void shouldThrowConflictExceptionIfRecipeAlreadySaved() {
        // Arrange
        User user = UserFactory.create();
        Long recipeId = 1L;
        Recipe recipe = RecipeFactory.create();
        CreateUserRecipeCommand.Command command = CreateUserRecipeCommand.Command.builder()
                .recipeId(recipeId)
                .build();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        when(getRecipeByIdRepository.execute(recipeId)).thenReturn(recipe);
        when(existsUserRecipeByUserIdAndRecipeIdRepository.execute(any(UserRecipe.class))).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () -> useCase.execute(command));
        verify(createUserRecipeRepository, never()).execute(any());
    }

    @Test
    public void shouldThrowExceptionIfSaveFails() {
        // Arrange
        User user = UserFactory.create();
        Long recipeId = 1L;
        Recipe recipe = RecipeFactory.create();
        CreateUserRecipeCommand.Command command = CreateUserRecipeCommand.Command.builder()
                .recipeId(recipeId)
                .build();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        when(getRecipeByIdRepository.execute(recipeId)).thenReturn(recipe);
        when(existsUserRecipeByUserIdAndRecipeIdRepository.execute(any(UserRecipe.class))).thenReturn(false);
        doThrow(new RuntimeException("Error")).when(createUserRecipeRepository).execute(any(UserRecipe.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> useCase.execute(command));
    }
}