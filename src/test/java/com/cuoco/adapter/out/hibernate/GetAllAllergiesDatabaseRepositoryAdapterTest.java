package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllAllergiesHibernateRepositoryAdapter;
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

class GetAllAllergiesDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllAllergiesHibernateRepositoryAdapter getAllAllergiesHibernateRepository;

    @InjectMocks
    private GetAllAllergiesDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_THEN_return_all_allergies() {
        List<AllergyHibernateModel> mockAllergies = List.of(
                AllergyHibernateModelFactory.create(1, "Mani"),
                AllergyHibernateModelFactory.create(2, "Gluten")
        );

        when(getAllAllergiesHibernateRepository.findAll()).thenReturn(mockAllergies);

        List<Allergy> result = adapter.execute();

        assertEquals(2, result.size());
        assertEquals("Mani", result.get(0).getDescription());
        assertEquals("Gluten", result.get(1).getDescription());

        verify(getAllAllergiesHibernateRepository).findAll();
    }
}
