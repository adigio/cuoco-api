package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.IngredientRequest;
import com.cuoco.adapter.in.controller.model.MealPrepRequest;
import com.cuoco.adapter.in.controller.model.MealPrepFilterRequest;
import com.cuoco.adapter.in.controller.model.MealPrepResponse;
import com.cuoco.application.port.in.GetMealPrepByIdQuery;
import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.factory.domain.MealPrepFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealPrepControllerAdapterTest {

    @Mock
    private GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand;

    @Mock
    private GetMealPrepByIdQuery getMealPrepByIdQuery;

    private MealPrepControllerAdapter mealPrepControllerAdapter;

    @BeforeEach
    void setUp() {
        mealPrepControllerAdapter = new MealPrepControllerAdapter(
                getMealPrepFromIngredientsCommand,
                getMealPrepByIdQuery
        );
    }

    @Test
    void shouldGenerateMealPrepsSuccessfully() {
        // Given
        MealPrepRequest request = MealPrepRequest.builder()
                .ingredients(List.of(IngredientRequest.builder().name("Tomato").build()))
                .filters(new MealPrepFilterRequest() {{
                    setFreeze(true);
                    setServings(4);
                }})
                .build();

        List<MealPrep> mealPreps = List.of(MealPrepFactory.create());
        when(getMealPrepFromIngredientsCommand.execute(any(GetMealPrepFromIngredientsCommand.Command.class)))
                .thenReturn(mealPreps);

        // When
        ResponseEntity<List<MealPrepResponse>> response = mealPrepControllerAdapter.generate(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(getMealPrepFromIngredientsCommand, times(1)).execute(any(GetMealPrepFromIngredientsCommand.Command.class));
    }

    @Test
    void shouldGetMealPrepByIdSuccessfully() {
        // Given
        Long mealPrepId = 1L;
        MealPrep mealPrep = MealPrepFactory.create();
        when(getMealPrepByIdQuery.execute(mealPrepId)).thenReturn(mealPrep);

        // When
        ResponseEntity<MealPrepResponse> response = mealPrepControllerAdapter.getById(mealPrepId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mealPrep.getId(), response.getBody().getId());
        verify(getMealPrepByIdQuery, times(1)).execute(mealPrepId);
    }
} 