package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllPlansQuery;
import com.cuoco.application.usecase.model.Plan;
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
class PlanControllerAdapterTest {

    @Mock
    private GetAllPlansQuery getAllPlansQuery;

    private PlanControllerAdapter planControllerAdapter;

    @BeforeEach
    void setUp() {
        planControllerAdapter = new PlanControllerAdapter(getAllPlansQuery);
    }

    @Test
    void shouldGetAllPlansSuccessfully() {
        List<Plan> plans = List.of(
                Plan.builder().id(1).description("Basic Plan").build(),
                Plan.builder().id(2).description("Premium Plan").build()
        );
        when(getAllPlansQuery.execute()).thenReturn(plans);

        ResponseEntity<List<ParametricResponse>> response = planControllerAdapter.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(getAllPlansQuery, times(1)).execute();
    }
}
