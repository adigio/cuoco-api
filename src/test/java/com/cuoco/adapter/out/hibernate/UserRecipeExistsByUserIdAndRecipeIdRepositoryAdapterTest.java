package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRecipeExistsByUserIdAndRecipeIdRepositoryAdapterTest {

    private UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter existRepo;
    private UserRecipeExistsByUserIdAndRecipeIdRepositoryAdapter adapter;

    @BeforeEach
    public void setUp() {
        existRepo = mock(UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter.class);
        adapter = new UserRecipeExistsByUserIdAndRecipeIdRepositoryAdapter(existRepo);
    }

    @Test
    public void shouldReturnTrueWhenRecipeExistsForUser() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setId(2L);

        UserRecipe userRecipe = new UserRecipe();
        userRecipe.setUser(user);
        userRecipe.setRecipe(recipe);
        userRecipe.setFavorite(false);

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

        UserRecipe userRecipe = new UserRecipe();
        userRecipe.setUser(user);
        userRecipe.setRecipe(recipe);
        userRecipe.setFavorite(false);

        when(existRepo.existsByUserIdAndRecipeId(3L, 4L)).thenReturn(false);

        // Act
        boolean result = adapter.execute(userRecipe);

        // Assert
        assertFalse(result);
    }
}
