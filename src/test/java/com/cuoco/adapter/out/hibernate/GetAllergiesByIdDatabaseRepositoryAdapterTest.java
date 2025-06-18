package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllergiesByIdHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.factory.hibernate.AllergyHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllergiesByIdDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllergiesByIdHibernateRepositoryAdapter hibernateRepository;

    @InjectMocks
    private GetAllergiesByIdDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_THEN_return_allergies_by_ids() {
        List<Integer> ids = List.of(1, 2);

        List<AllergyHibernateModel> mockList = List.of(
                AllergyHibernateModelFactory.create(1, "Mani"),
                AllergyHibernateModelFactory.create(2, "Chocolate")
        );

        when(hibernateRepository.findByIdIn(ids)).thenReturn(mockList);

        List<Allergy> result = adapter.execute(ids);

        assertEquals(2, result.size());
        assertEquals("Mani", result.get(0).getDescription());
        assertEquals("Chocolate", result.get(1).getDescription());

        verify(hibernateRepository).findByIdIn(ids);
    }
}
