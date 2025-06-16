package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllDietsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAllDietsUseCaseTest {

    private GetAllDietsRepository getAllDietsRepository;

    private GetAllAllDietsUseCase useCase;

    @BeforeEach
    void setup() {
        getAllDietsRepository = mock(GetAllDietsRepository.class);
        useCase = new GetAllAllDietsUseCase(getAllDietsRepository);
    }

    @Test
    void GIVEN_no_params_WHEN_execute_THEN_repository_called_once() {
        when(getAllDietsRepository.execute()).thenReturn(List.of());

        useCase.execute();

        verify(getAllDietsRepository, times(1)).execute();
    }
}
