package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllAllergiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllAllergiesUseCaseTest {

    private GetAllAllergiesRepository getAllAllergiesRepository;

    private GetAllAllergiesUseCase useCase;

    @BeforeEach
    void setup() {
        getAllAllergiesRepository = mock(GetAllAllergiesRepository.class);
        useCase = new GetAllAllergiesUseCase(getAllAllergiesRepository);
    }

    @Test
    void GIVEN_no_params_WHEN_execute_THEN_repository_called_once() {
        when(getAllAllergiesRepository.execute()).thenReturn(List.of());

        useCase.execute();

        verify(getAllAllergiesRepository, times(1)).execute();
    }
}