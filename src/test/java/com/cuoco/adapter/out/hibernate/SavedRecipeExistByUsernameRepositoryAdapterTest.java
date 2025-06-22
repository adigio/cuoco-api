package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.ExistUserRecipesHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class SavedRecipeExistByUsernameRepositoryAdapterTest {

    private ExistUserRecipesHibernateRepositoryAdapter existRepo;
    private SavedRecipeExistByUsernameRepositoryAdapter adapter;

    @BeforeEach
    public void setUp() {
        existRepo = mock(ExistUserRecipesHibernateRepositoryAdapter.class);
        adapter = new SavedRecipeExistByUsernameRepositoryAdapter(existRepo);
    }

    @Test
    public void shouldReturnTrueWhenRecipeExistsForUser() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setId(2L);

        UserRecipe userRecipe = new UserRecipe(user, recipe, false);

        when(existRepo.existsByUserIdAndRecipeId(1L, 2L)).thenReturn(true);

        // Act
        boolean result = adapter.execute(userRecipe);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenRecipeDoesNotExistForUser() {
        // Arrange
        User user = new User();
        user.setId(3L);

        Recipe recipe = new Recipe();
        recipe.setId(4L);

        UserRecipe userRecipe = new UserRecipe(user, recipe, false);

        when(existRepo.existsByUserIdAndRecipeId(3L, 4L)).thenReturn(false);

        // Act
        boolean result = adapter.execute(userRecipe);

        // Assert
        assertFalse(result);
    }
}
