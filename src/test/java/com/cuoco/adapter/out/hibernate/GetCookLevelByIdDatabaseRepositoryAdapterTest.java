package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetCookLevelByIdHibernateRepository;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.factory.hibernate.CookLevelHibernateModelFactory;
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

class GetCookLevelByIdDatabaseRepositoryAdapterTest {

    @Mock
    private GetCookLevelByIdHibernateRepository hibernateRepository;

    @InjectMocks
    private GetCookLevelByIdDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_with_existing_id_THEN_return_cook_level() {
        Integer id = 1;
        CookLevelHibernateModel model = CookLevelHibernateModelFactory.create(id, "Beginner");

        when(hibernateRepository.findById(id)).thenReturn(Optional.of(model));

        CookLevel result = adapter.execute(id);

        assertEquals(id, result.getId());
        assertEquals("Beginner", result.getDescription());

        verify(hibernateRepository).findById(id);
    }

    @Test
    void WHEN_execute_with_non_existing_id_THEN_throw_exception() {
        Integer id = 99;

        when(hibernateRepository.findById(id)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> adapter.execute(id));

        assertEquals(ErrorDescription.COOK_LEVEL_NOT_EXISTS.getValue(), exception.getDescription());

        verify(hibernateRepository).findById(id);
    }
}
