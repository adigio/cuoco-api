package com.cuoco.application.usecase;

import com.cuoco.application.exception.ConflictException;
import com.cuoco.application.port.in.CreateUserMealPrepCommand;
import com.cuoco.application.port.out.CreateUserMealPrepRepository;
import com.cuoco.application.port.out.ExistsUserMealPrepRepository;
import com.cuoco.application.port.out.GetMealPrepByIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserMealPrep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateUserMealPrepUseCaseTest {

    private UserDomainService userDomainService;
    private CreateUserMealPrepRepository createUserMealPrepRepository;
    private ExistsUserMealPrepRepository existsUserMealPrepRepository;
    private GetMealPrepByIdRepository getMealPrepByIdRepository;
    private CreateUserMealPrepUseCase useCase;

    @BeforeEach
    void setup() {
        userDomainService = mock(UserDomainService.class);
        createUserMealPrepRepository = mock(CreateUserMealPrepRepository.class);
        existsUserMealPrepRepository = mock(ExistsUserMealPrepRepository.class);
        getMealPrepByIdRepository = mock(GetMealPrepByIdRepository.class);
        
        useCase = new CreateUserMealPrepUseCase(userDomainService, createUserMealPrepRepository, existsUserMealPrepRepository, getMealPrepByIdRepository);
    }

    @Test
    void GIVEN_not_existing_WHEN_execute_THEN_save_user_meal_prep() {
        User user = User.builder().id(1L).build();
        MealPrep mealPrep = MealPrep.builder().id(2L).build();
        CreateUserMealPrepCommand.Command command = CreateUserMealPrepCommand.Command.builder().id(2L).build();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        when(getMealPrepByIdRepository.execute(2L)).thenReturn(mealPrep);
        when(existsUserMealPrepRepository.execute(any(UserMealPrep.class))).thenReturn(false);

        useCase.execute(command);
        verify(createUserMealPrepRepository, times(1)).execute(any(UserMealPrep.class));
    }

    @Test
    void GIVEN_existing_WHEN_execute_THEN_throw_conflict() {
        User user = User.builder().id(1L).build();
        MealPrep mealPrep = MealPrep.builder().id(2L).build();
        CreateUserMealPrepCommand.Command command = CreateUserMealPrepCommand.Command.builder().id(2L).build();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        when(getMealPrepByIdRepository.execute(2L)).thenReturn(mealPrep);
        when(existsUserMealPrepRepository.execute(any(UserMealPrep.class))).thenReturn(true);

        assertThrows(ConflictException.class, () -> useCase.execute(command));
        verify(createUserMealPrepRepository, never()).execute(any(UserMealPrep.class));
    }
} 