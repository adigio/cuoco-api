package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetDietaryNeedsByIdHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.factory.hibernate.DietaryNeedHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetDietaryNeedsByIdDatabaseRepositoryAdapterTest {

    @Mock
    private GetDietaryNeedsByIdHibernateRepositoryAdapter hibernateRepository;

    @InjectMocks
    private GetDietaryNeedsByIdDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_with_valid_ids_THEN_return_dietary_needs() {
        List<Integer> ids = List.of(1, 2);
        List<DietaryNeedHibernateModel> models = List.of(
                DietaryNeedHibernateModelFactory.create(1, "Vegetariano"),
                DietaryNeedHibernateModelFactory.create(2, "Vegano")
        );

        when(hibernateRepository.findByIdIn(ids)).thenReturn(models);

        List<DietaryNeed> result = adapter.execute(ids);

        assertEquals(2, result.size());
        assertEquals("Vegetariano", result.get(0).getDescription());
        assertEquals("Vegano", result.get(1).getDescription());

        verify(hibernateRepository).findByIdIn(ids);
    }

    @Test
    void WHEN_execute_with_empty_list_THEN_return_empty_list() {
        List<Integer> ids = List.of();
        List<DietaryNeedHibernateModel> models = List.of();

        when(hibernateRepository.findByIdIn(ids)).thenReturn(models);

        List<DietaryNeed> result = adapter.execute(ids);

        assertTrue(result.isEmpty());
        verify(hibernateRepository).findByIdIn(ids);
    }
}
