package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.DeleteUserMealPrepsHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.UserMealPrep;
import com.cuoco.factory.domain.MealPrepFactory;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
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
        // Given
        Long userId = 1L;
        Long mealPrepId = 1L;

        // When
        deleteUserMealPrepDatabaseRepositoryAdapter.execute(userId, mealPrepId);

        // Then
        verify(deleteUserMealPrepsHibernateRepositoryAdapter).deleteAllByUserIdAndMealPrepId(userId, mealPrepId);
    }
} 