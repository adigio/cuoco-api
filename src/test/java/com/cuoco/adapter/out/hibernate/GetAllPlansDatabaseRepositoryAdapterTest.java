package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PlanHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllPlansHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.factory.hibernate.PlanHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllPlansDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllPlansHibernateRepositoryAdapter hibernateRepository;

    @InjectMocks
    private GetAllPlansDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_THEN_return_all_plans() {
        List<PlanHibernateModel> mockList = List.of(
                PlanHibernateModelFactory.create(1, "Basic"),
                PlanHibernateModelFactory.create(2, "Premium")
        );

        when(hibernateRepository.findAll()).thenReturn(mockList);

        List<Plan> result = adapter.execute();

        assertEquals(2, result.size());
        assertEquals("Basic", result.get(0).getDescription());
        assertEquals("Premium", result.get(1).getDescription());

        verify(hibernateRepository).findAll();
    }
}