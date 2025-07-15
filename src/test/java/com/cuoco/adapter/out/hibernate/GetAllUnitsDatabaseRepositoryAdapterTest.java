package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllUnitsHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllUnitsDatabaseRepositoryAdapterTest {

    @Mock
    private GetAllUnitsHibernateRepositoryAdapter getAllUnitsHibernateRepositoryAdapter;

    private GetAllUnitsDatabaseRepositoryAdapter getAllUnitsDatabaseRepositoryAdapter;

    @BeforeEach
    void setUp() {
        getAllUnitsDatabaseRepositoryAdapter = new GetAllUnitsDatabaseRepositoryAdapter(
                getAllUnitsHibernateRepositoryAdapter
        );
    }

    @Test
    void shouldGetAllUnitsSuccessfully() {
        List<UnitHibernateModel> expectedUnits = List.of(
                UnitHibernateModel.builder().id(1).description("Cup").symbol("cup").build(),
                UnitHibernateModel.builder().id(2).description("Gram").symbol("g").build()
        );
        when(getAllUnitsHibernateRepositoryAdapter.findAll()).thenReturn(expectedUnits);

        List<Unit> result = getAllUnitsDatabaseRepositoryAdapter.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cup", result.get(0).getDescription());
        assertEquals("cup", result.get(0).getSymbol());
        assertEquals("Gram", result.get(1).getDescription());
        assertEquals("g", result.get(1).getSymbol());
    }
} 