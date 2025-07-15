package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.UnitResponse;
import com.cuoco.application.port.in.GetAllUnitsQuery;
import com.cuoco.application.usecase.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitControllerAdapterTest {

    @Mock
    private GetAllUnitsQuery getAllUnitsQuery;

    private UnitControllerAdapter unitControllerAdapter;

    @BeforeEach
    void setUp() {
        unitControllerAdapter = new UnitControllerAdapter(getAllUnitsQuery);
    }

    @Test
    void shouldGetAllUnitsSuccessfully() {
        List<Unit> units = List.of(
                Unit.builder().id(1).description("Cup").symbol("cup").build(),
                Unit.builder().id(2).description("Gram").symbol("g").build()
        );
        when(getAllUnitsQuery.execute()).thenReturn(units);

        ResponseEntity<List<UnitResponse>> response = unitControllerAdapter.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(getAllUnitsQuery, times(1)).execute();
    }
} 