package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetPlanByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.factory.hibernate.PlanHibernateModelFactory;
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

class GetPlanByIdDatabaseRepositoryAdapterTest {

    @Mock
    private GetPlanByIdHibernateRepositoryAdapter hibernateRepository;

    @InjectMocks
    private GetPlanByIdDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_with_existing_id_THEN_return_plan() {
        Integer id = 1;
        PlanHibernateModel model = PlanHibernateModelFactory.create(id, "Pro");

        when(hibernateRepository.findById(id)).thenReturn(Optional.of(model));

        Plan result = adapter.execute(id);

        assertEquals("Pro", result.getDescription());
        assertEquals(id, result.getId());
        verify(hibernateRepository).findById(id);
    }

    @Test
    void WHEN_execute_with_non_existing_id_THEN_throw_BadRequestException() {
        Integer id = 99;

        when(hibernateRepository.findById(id)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> adapter.execute(id));
        assertEquals(ErrorDescription.PLAN_NOT_EXISTS.getValue(), ex.getDescription());

        verify(hibernateRepository).findById(id);
    }
}
