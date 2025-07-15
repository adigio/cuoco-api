package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllPreparationTimesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllPreparationTimesUseCaseTest {

    private GetAllPreparationTimesRepository getAllPreparationTimesRepository;
    private GetAllPreparationTimesUseCase useCase;

    @BeforeEach
    void setup() {
        getAllPreparationTimesRepository = mock(GetAllPreparationTimesRepository.class);
        useCase = new GetAllPreparationTimesUseCase(getAllPreparationTimesRepository);
    }

    @Test
    void GIVEN_no_params_WHEN_execute_THEN_repository_called_once() {
        when(getAllPreparationTimesRepository.execute()).thenReturn(List.of());
        useCase.execute();
        verify(getAllPreparationTimesRepository, times(1)).execute();
    }
} 