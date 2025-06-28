package com.cuoco.application.usecase;

import com.cuoco.application.port.in.CreateUserRecipeCommand.Command;
import com.cuoco.application.port.out.CreateUserRecipeRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.UserRecipeExistsByUserIdAndRecipeIdRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRecipeUseCaseTest {

    private CreateUserRecipeRepository createUserRecipeRepository;
    private UserRecipeExistsByUserIdAndRecipeIdRepository userRecipeExistsByUserIdAndRecipeIdRepository;
    private GetRecipeByIdRepository getRecipeByIdRepository;

    private CreateUserRecipeUseCase useCase;

    @BeforeEach
    public void setUp() {
        createUserRecipeRepository = mock(CreateUserRecipeRepository.class);
        userRecipeExistsByUserIdAndRecipeIdRepository = mock(UserRecipeExistsByUserIdAndRecipeIdRepository.class);
        getRecipeByIdRepository = mock(GetRecipeByIdRepository.class);

        useCase = new CreateUserRecipeUseCase(createUserRecipeRepository, userRecipeExistsByUserIdAndRecipeIdRepository, getRecipeByIdRepository);
    }

    @Test
    public void shouldReturnTrueIfRecipeAlreadySaved() {
        // Arrange
        User user = new User();
        user.setName("testUser");
        Long recipeId = 1L;
        Command command = new Command(user, recipeId);

        Recipe recipe = new Recipe(); // rellenar si tiene más campos
        when(getRecipeByIdRepository.execute(recipeId)).thenReturn(recipe);
        when(userRecipeExistsByUserIdAndRecipeIdRepository.execute(any(UserRecipe.class))).thenReturn(true);

        // Act
        Boolean result = useCase.execute(command);

        // Assert
        assertTrue(result);
        verify(createUserRecipeRepository, never()).execute(any());
    }

    @Test
    public void shouldSaveRecipeIfNotExistsAndReturnTrue() {
        // Arrange
        User user = new User();
        user.setName("testUser");
        Long recipeId = 1L;
        Command command = new Command(user, recipeId);

        Recipe recipe = new Recipe(); // rellenar si tiene más campos
        when(getRecipeByIdRepository.execute(recipeId)).thenReturn(recipe);
        when(userRecipeExistsByUserIdAndRecipeIdRepository.execute(any(UserRecipe.class))).thenReturn(false);

        // Act
        Boolean result = useCase.execute(command);

        // Assert
        assertTrue(result);
        verify(createUserRecipeRepository).execute(any(UserRecipe.class));
    }

    @Test
    public void shouldReturnFalseIfSaveFails() {
        // Arrange
        User user = new User();
        user.setName("testUser");
        Long recipeId = 1L;
        Command command = new Command(user, recipeId);

        Recipe recipe = new Recipe(); // rellenar si tiene más campos
        when(getRecipeByIdRepository.execute(recipeId)).thenReturn(recipe);
        when(userRecipeExistsByUserIdAndRecipeIdRepository.execute(any(UserRecipe.class))).thenReturn(false);
        doThrow(new RuntimeException("Error")).when(createUserRecipeRepository).execute(any(UserRecipe.class));

        // Act
        Boolean result = useCase.execute(command);

        // Assert
        assertFalse(result);
    }
}