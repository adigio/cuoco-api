package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetMealPrepByIdRepository;
import com.cuoco.application.usecase.model.MealPrep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetMealPrepByIdUseCaseTest {

    private GetMealPrepByIdRepository getMealPrepByIdRepository;
    private GetMealPrepByIdUseCase useCase;

    @BeforeEach
    void setup() {
        getMealPrepByIdRepository = mock(GetMealPrepByIdRepository.class);
        useCase = new GetMealPrepByIdUseCase(getMealPrepByIdRepository);
    }

    @Test
    void GIVEN_id_WHEN_execute_THEN_return_meal_prep() {
        MealPrep mealPrep = MealPrep.builder().id(1L).build();

        when(getMealPrepByIdRepository.execute(1L)).thenReturn(mealPrep);
        MealPrep result = useCase.execute(1L);

        assertEquals(mealPrep, result);
        verify(getMealPrepByIdRepository, times(1)).execute(1L);
    }
} 