package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAllCookLevelsUseCaseTest {

    private GetAllCookLevelsRepository getAllCookLevelsRepository;

    private GetAllAllCookLevelsUseCase useCase;

    @BeforeEach
    void setup() {
        getAllCookLevelsRepository = mock(GetAllCookLevelsRepository.class);
        useCase = new GetAllAllCookLevelsUseCase(getAllCookLevelsRepository);
    }

    @Test
    void GIVEN_no_params_WHEN_execute_THEN_repository_called_once() {
        when(getAllCookLevelsRepository.execute()).thenReturn(List.of());

        useCase.execute();

        verify(getAllCookLevelsRepository, times(1)).execute();
    }
}