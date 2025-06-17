package com.cuoco.adapter.out.database;

import com.cuoco.adapter.out.hibernate.GetAllCookLevelsDatabaseRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllCookLevelsHibernateRepository;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.factory.hibernate.CookLevelHibernateModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllCookLevelsDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllCookLevelsHibernateRepository getAllCookLevelsHibernateRepository;

    @InjectMocks
    private GetAllCookLevelsDatabaseRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void WHEN_execute_THEN_return_all_cook_levels() {
        List<CookLevelHibernateModel> mockCookLevels = List.of(
                CookLevelHibernateModelFactory.create(1, "Beginner"),
                CookLevelHibernateModelFactory.create(2, "Advanced")
        );

        when(getAllCookLevelsHibernateRepository.findAll()).thenReturn(mockCookLevels);

        List<CookLevel> result = adapter.execute();

        assertEquals(2, result.size());
        assertEquals("Beginner", result.get(0).getDescription());
        assertEquals("Advanced", result.get(1).getDescription());

        verify(getAllCookLevelsHibernateRepository).findAll();
    }
}
