package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllDietaryNeedsHibernateRepository;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.factory.hibernate.DietaryNeedHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllDietaryNeedsDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllDietaryNeedsHibernateRepository hibernateRepository;

    @InjectMocks
    private GetAllDietaryNeedsDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_THEN_return_all_dietary_needs() {
        List<DietaryNeedHibernateModel> mockList = List.of(
                DietaryNeedHibernateModelFactory.create(1, "Vegan"),
                DietaryNeedHibernateModelFactory.create(2, "Gluten-Free")
        );

        when(hibernateRepository.findAll()).thenReturn(mockList);

        List<DietaryNeed> result = adapter.execute();

        assertEquals(2, result.size());
        assertEquals("Vegan", result.get(0).getDescription());
        assertEquals("Gluten-Free", result.get(1).getDescription());

        verify(hibernateRepository).findAll();
    }
}