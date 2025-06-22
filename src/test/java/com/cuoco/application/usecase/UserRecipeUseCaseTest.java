package com.cuoco.application.usecase;

import com.cuoco.application.port.in.SaveUserRecipeCommand.Command;
import com.cuoco.application.port.out.FavRecipeRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.SavedRecipeExistByUsernameRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserRecipeUseCaseTest {

    private FavRecipeRepository favRecipeRepository;
    private SavedRecipeExistByUsernameRepository savedRecipeExistByUsernameRepository;
    private GetRecipeByIdRepository getRecipeByIdRepository;

    private SaveUserRecipeUseCase useCase;

    @BeforeEach
    public void setUp() {
        favRecipeRepository = mock(FavRecipeRepository.class);
        savedRecipeExistByUsernameRepository = mock(SavedRecipeExistByUsernameRepository.class);
        getRecipeByIdRepository = mock(GetRecipeByIdRepository.class);

        useCase = new SaveUserRecipeUseCase(favRecipeRepository, savedRecipeExistByUsernameRepository, getRecipeByIdRepository);
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
        when(savedRecipeExistByUsernameRepository.execute(any(UserRecipe.class))).thenReturn(true);

        // Act
        Boolean result = useCase.execute(command);

        // Assert
        assertTrue(result);
        verify(favRecipeRepository, never()).execute(any());
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
        when(savedRecipeExistByUsernameRepository.execute(any(UserRecipe.class))).thenReturn(false);

        // Act
        Boolean result = useCase.execute(command);

        // Assert
        assertTrue(result);
        verify(favRecipeRepository).execute(any(UserRecipe.class));
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
        when(savedRecipeExistByUsernameRepository.execute(any(UserRecipe.class))).thenReturn(false);
        doThrow(new RuntimeException("Error")).when(favRecipeRepository).execute(any(UserRecipe.class));

        // Act
        Boolean result = useCase.execute(command);

        // Assert
        assertFalse(result);
    }
}