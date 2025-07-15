package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.DeleteUserRecipeHibernateRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserRecipeDatabaseRepositoryAdapterTest {

    @Mock
    private DeleteUserRecipeHibernateRepositoryAdapter deleteUserRecipeHibernateRepositoryAdapter;

    private DeleteUserRecipeDatabaseRepositoryAdapter deleteUserRecipeDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        deleteUserRecipeDatabaseRepositoryAdapter = new DeleteUserRecipeDatabaseRepositoryAdapter(
                deleteUserRecipeHibernateRepositoryAdapter
        );
    }

    @Test
    void shouldDeleteUserRecipeSuccessfully() {
        Long userId = 1L;
        Long recipeId = 1L;

        deleteUserRecipeDatabaseRepositoryAdapter.execute(userId, recipeId);

        verify(deleteUserRecipeHibernateRepositoryAdapter).deleteAllByUserIdAndRecipeId(userId, recipeId);
    }
} 