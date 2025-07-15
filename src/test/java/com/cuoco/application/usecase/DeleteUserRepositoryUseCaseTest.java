package com.cuoco.application.usecase;

import com.cuoco.application.port.in.DeleteUserRecipeCommand;
import com.cuoco.application.port.out.DeleteUserRecipeRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteUserRepositoryUseCaseTest {

    private UserDomainService userDomainService;
    private DeleteUserRecipeRepository deleteUserRecipeRepository;
    private DeleteUserRepositoryUseCase useCase;

    @BeforeEach
    void setup() {
        userDomainService = mock(UserDomainService.class);
        deleteUserRecipeRepository = mock(DeleteUserRecipeRepository.class);
        useCase = new DeleteUserRepositoryUseCase(userDomainService, deleteUserRecipeRepository);
    }

    @Test
    void GIVEN_command_WHEN_execute_THEN_delete_called_with_user_id_and_recipe_id() {
        User user = User.builder().id(1L).build();
        DeleteUserRecipeCommand.Command command = DeleteUserRecipeCommand.Command.builder().id(2L).build();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        
        useCase.execute(command);
        verify(deleteUserRecipeRepository, times(1)).execute(1L, 2L);
    }
} 