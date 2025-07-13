package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.CreateUserRecipeHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import com.cuoco.factory.domain.RecipeFactory;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateUserRecipeDatabaseRepositoryAdapterTest {

    @Mock
    private CreateUserRecipeHibernateRepositoryAdapter createUserRecipeHibernateRepositoryAdapter;

    @InjectMocks
    private CreateUserRecipeDatabaseRepositoryAdapter repository;

    @Test
    public void shouldCallSaveWithCorrectModel() {
        User user = UserFactory.create();
        Recipe recipe = RecipeFactory.create();
        UserRecipe userRecipe = UserRecipe.builder()
                .user(user)
                .recipe(recipe)
                .build();

        doNothing().when(createUserRecipeHibernateRepositoryAdapter).save(any());

        repository.execute(userRecipe);

        verify(createUserRecipeHibernateRepositoryAdapter).save(any());
    }
}
