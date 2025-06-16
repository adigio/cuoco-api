package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllPlansRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAllPlansUseCaseTest {

    private GetAllPlansRepository getAllPlansRepository;

    private GetAllAllPlansUseCase useCase;

    @BeforeEach
    void setup() {
        getAllPlansRepository = mock(GetAllPlansRepository.class);
        useCase = new GetAllAllPlansUseCase(getAllPlansRepository);
    }

    @Test
    void GIVEN_no_params_WHEN_execute_THEN_repository_called_once() {
        when(getAllPlansRepository.execute()).thenReturn(List.of());

        useCase.execute();

        verify(getAllPlansRepository, times(1)).execute();
    }
}
