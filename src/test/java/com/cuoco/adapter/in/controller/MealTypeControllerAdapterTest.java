package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.ParametricResponse;
import com.cuoco.application.port.in.GetAllMealTypesQuery;
import com.cuoco.application.usecase.model.MealType;
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
class MealTypeControllerAdapterTest {

    @Mock
    private GetAllMealTypesQuery getAllMealTypesQuery;

    private MealTypeControllerAdapter mealTypeControllerAdapter;

    @BeforeEach
    void setUp() {
        mealTypeControllerAdapter = new MealTypeControllerAdapter(getAllMealTypesQuery);
    }

    @Test
    void shouldGetAllMealTypesSuccessfully() {
        // Given
        List<MealType> mealTypes = List.of(
                MealType.builder().id(1).description("Breakfast").build(),
                MealType.builder().id(2).description("Lunch").build()
        );
        when(getAllMealTypesQuery.execute()).thenReturn(mealTypes);

        // When
        ResponseEntity<List<ParametricResponse>> response = mealTypeControllerAdapter.getAll();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(getAllMealTypesQuery, times(1)).execute();
    }
} 