package com.cuoco.application.usecase;

import com.cuoco.application.port.in.DeleteUserMealPrepCommand;
import com.cuoco.application.port.out.DeleteUserMealPrepRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteUserMealPrepUseCaseTest {

    private UserDomainService userDomainService;
    private DeleteUserMealPrepRepository deleteUserMealPrepRepository;
    private DeleteUserMealPrepUseCase useCase;

    @BeforeEach
    void setup() {
        userDomainService = mock(UserDomainService.class);
        deleteUserMealPrepRepository = mock(DeleteUserMealPrepRepository.class);
        useCase = new DeleteUserMealPrepUseCase(userDomainService, deleteUserMealPrepRepository);
    }

    @Test
    void GIVEN_command_WHEN_execute_THEN_delete_called_with_user_id_and_meal_prep_id() {
        User user = User.builder().id(1L).build();
        DeleteUserMealPrepCommand.Command command = DeleteUserMealPrepCommand.Command.builder().id(2L).build();

        when(userDomainService.getCurrentUser()).thenReturn(user);

        useCase.execute(command);
        verify(deleteUserMealPrepRepository, times(1)).execute(1L, 2L);
    }
} 