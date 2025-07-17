package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetDietByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.factory.hibernate.DietHibernateModelFactory;
import com.cuoco.shared.model.ErrorDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetDietByIdDatabaseRepositoryAdapterTest {

    @Mock
    private GetDietByIdHibernateRepositoryAdapter hibernateRepository;

    @InjectMocks
    private GetDietByIdDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_with_existing_id_THEN_return_diet() {
        Integer id = 1;
        DietHibernateModel model = DietHibernateModelFactory.create(id, "Keto");

        when(hibernateRepository.findById(id)).thenReturn(Optional.of(model));

        Diet result = adapter.execute(id);

        assertEquals("Keto", result.getDescription());
        assertEquals(id, result.getId());
        verify(hibernateRepository).findById(id);
    }

    @Test
    void WHEN_execute_with_non_existing_id_THEN_throw_BadRequestException() {
        Integer id = 99;

        when(hibernateRepository.findById(id)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> adapter.execute(id));
        assertEquals(ErrorDescription.DIET_NOT_EXISTS.getValue(), ex.getDescription());

        verify(hibernateRepository).findById(id);
    }
}
