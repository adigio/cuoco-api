package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetMealPrepByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.factory.hibernate.MealPrepHibernateModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMealPrepByIdDatabaseRepositoryAdapterTest {

    @Mock
    private GetMealPrepByIdHibernateRepositoryAdapter getMealPrepByIdHibernateRepositoryAdapter;

    @InjectMocks
    private GetMealPrepByIdDatabaseRepositoryAdapter getMealPrepByIdDatabaseRepositoryAdapter;

    @Test
    void shouldGetMealPrepByIdSuccessfully() {
        // Given
        Long mealPrepId = 1L;
        MealPrepHibernateModel mealPrepHibernateModel = MealPrepHibernateModelFactory.create();
        when(getMealPrepByIdHibernateRepositoryAdapter.findById(mealPrepId))
                .thenReturn(Optional.of(mealPrepHibernateModel));

        // When
        MealPrep result = getMealPrepByIdDatabaseRepositoryAdapter.execute(mealPrepId);

        // Then
        assertNotNull(result);
        assertEquals(mealPrepHibernateModel.getId(), result.getId());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenMealPrepNotFound() {
        // Given
        Long mealPrepId = 999L;
        when(getMealPrepByIdHibernateRepositoryAdapter.findById(mealPrepId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(BadRequestException.class, () -> {
            getMealPrepByIdDatabaseRepositoryAdapter.execute(mealPrepId);
        });
    }
} 