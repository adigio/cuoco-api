package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllMealTypesHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.MealType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllMealTypesDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllMealTypesHibernateRepositoryAdapter getAllMealTypesHibernateRepositoryAdapter;

    private GetAllMealTypesDatabaseRepositoryAdapter getAllMealTypesDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        getAllMealTypesDatabaseRepositoryAdapter = new GetAllMealTypesDatabaseRepositoryAdapter(
                getAllMealTypesHibernateRepositoryAdapter
        );
    }

    @Test
    void shouldGetAllMealTypesSuccessfully() {
        // Given
        List<MealTypeHibernateModel> expectedMealTypes = List.of(
                MealTypeHibernateModel.builder().id(1).description("Breakfast").build(),
                MealTypeHibernateModel.builder().id(2).description("Lunch").build()
        );
        when(getAllMealTypesHibernateRepositoryAdapter.findAll()).thenReturn(expectedMealTypes);

        // When
        List<MealType> result = getAllMealTypesDatabaseRepositoryAdapter.execute();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Breakfast", result.get(0).getDescription());
        assertEquals("Lunch", result.get(1).getDescription());
    }
} 