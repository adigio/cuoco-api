package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.ExistsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExistsUserRecipeByUserIdAndRecipeIdRepositoryAdapterTest {

    private ExistsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter existRepo;
    private ExistsUserRecipeDatabaseRepositoryAdapter adapter;

    @BeforeEach
    public void setUp() {
        existRepo = mock(ExistsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter.class);
        adapter = new ExistsUserRecipeDatabaseRepositoryAdapter(existRepo);
    }

    @Test
    public void shouldReturnTrueWhenRecipeExistsForUser() {
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setId(2L);

        UserRecipe userRecipe = new UserRecipe();
        userRecipe.setUser(user);
        userRecipe.setRecipe(recipe);

        when(existRepo.existsByUserIdAndRecipeId(1L, 2L)).thenReturn(true);

        boolean result = adapter.execute(userRecipe);

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenRecipeDoesNotExistForUser() {
        User user = new User();
        user.setId(3L);

        Recipe recipe = new Recipe();
        recipe.setId(4L);

        UserRecipe userRecipe = new UserRecipe();
        userRecipe.setUser(user);
        userRecipe.setRecipe(recipe);

        when(existRepo.existsByUserIdAndRecipeId(3L, 4L)).thenReturn(false);

        boolean result = adapter.execute(userRecipe);

        assertFalse(result);
    }
}
