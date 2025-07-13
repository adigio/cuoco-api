package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllPreparationTimesQuery;
import com.cuoco.application.usecase.model.PreparationTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreparationTimeControllerAdapterTest {

    @Mock
    private GetAllPreparationTimesQuery getAllPreparationTimesQuery;

    private PreparationTimeControllerAdapter preparationTimeControllerAdapter;

    @BeforeEach
    void setUp() {
        preparationTimeControllerAdapter = new PreparationTimeControllerAdapter(getAllPreparationTimesQuery);
    }

    @Test
    void shouldGetAllPreparationTimesSuccessfully() {
        // Given
        List<PreparationTime> preparationTimes = List.of(
                PreparationTime.builder().id(1).description("15 minutes").build(),
                PreparationTime.builder().id(2).description("30 minutes").build()
        );
        when(getAllPreparationTimesQuery.execute()).thenReturn(preparationTimes);

        // When
        ResponseEntity<List<ParametricResponse>> response = preparationTimeControllerAdapter.getAllPreparationTimes();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(getAllPreparationTimesQuery, times(1)).execute();
    }
} 