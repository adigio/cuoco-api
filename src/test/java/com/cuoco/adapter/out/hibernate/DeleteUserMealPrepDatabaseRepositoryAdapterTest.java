package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.DeleteUserMealPrepsHibernateRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserMealPrepDatabaseRepositoryAdapterTest {

    @Mock
    private DeleteUserMealPrepsHibernateRepositoryAdapter deleteUserMealPrepsHibernateRepositoryAdapter;

    private DeleteUserMealPrepDatabaseRepositoryAdapter deleteUserMealPrepDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        deleteUserMealPrepDatabaseRepositoryAdapter = new DeleteUserMealPrepDatabaseRepositoryAdapter(
                deleteUserMealPrepsHibernateRepositoryAdapter
        );
    }

    @Test
    void shouldDeleteUserMealPrepSuccessfully() {
        Long userId = 1L;
        Long mealPrepId = 1L;

        deleteUserMealPrepDatabaseRepositoryAdapter.execute(userId, mealPrepId);

        verify(deleteUserMealPrepsHibernateRepositoryAdapter).deleteAllByUserIdAndMealPrepId(userId, mealPrepId);
    }
} 