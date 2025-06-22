package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.FavRecipeRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.SaveUserRecipeHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FavRecipeRepositoryAdapterTest {

    private SaveUserRecipeHibernateRepositoryAdapter saveAdapter;
    private FavRecipeRepositoryAdapter repository;

    @BeforeEach
    public void setUp() {
        saveAdapter = mock(SaveUserRecipeHibernateRepositoryAdapter.class);
        repository = new FavRecipeRepositoryAdapter(saveAdapter);
    }

    @Test
    public void shouldCallSaveWithCorrectModel() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setId(2L);

        UserRecipe userRecipe = new UserRecipe(user, recipe, true);

        // Act
        Boolean result = repository.execute(userRecipe);

        // Assert
        assertNull(result); // porque el método devuelve null por ahora
        verify(saveAdapter, times(1)).save(argThat(model ->
                model.getUser().getId().equals(1L) &&
                        model.getRecipe().getId().equals(2L) &&
                        model.getFavorite().equals(true)
        ));
    }
}
