package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllDietsHibernateRepository;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.factory.hibernate.DietHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllDietsDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllDietsHibernateRepository hibernateRepository;

    @InjectMocks
    private GetAllDietsDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_THEN_return_all_diets() {
        List<DietHibernateModel> mockList = List.of(
                DietHibernateModelFactory.create(1, "Keto"),
                DietHibernateModelFactory.create(2, "Paleo")
        );

        when(hibernateRepository.findAll()).thenReturn(mockList);

        List<Diet> result = adapter.execute();

        assertEquals(2, result.size());
        assertEquals("Keto", result.get(0).getDescription());
        assertEquals("Paleo", result.get(1).getDescription());

        verify(hibernateRepository).findAll();
    }
}