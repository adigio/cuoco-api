package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAllDietaryNeedsUseCaseTest {

    private GetAllDietaryNeedsRepository getAllDietaryNeedsRepository;

    private GetAllDietaryNeedsUseCase useCase;

    @BeforeEach
    void setup() {
        getAllDietaryNeedsRepository = mock(GetAllDietaryNeedsRepository.class);
        useCase = new GetAllDietaryNeedsUseCase(getAllDietaryNeedsRepository);
    }

    @Test
    void GIVEN_no_params_WHEN_execute_THEN_repository_called_once() {
        when(getAllDietaryNeedsRepository.execute()).thenReturn(List.of());

        useCase.execute();

        verify(getAllDietaryNeedsRepository, times(1)).execute();
    }
}
