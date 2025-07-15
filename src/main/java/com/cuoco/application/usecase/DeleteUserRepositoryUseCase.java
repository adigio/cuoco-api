package com.cuoco.application.usecase;

import com.cuoco.application.port.in.DeleteUserRecipeCommand;
import com.cuoco.application.port.out.DeleteUserRecipeRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteUserRepositoryUseCase implements DeleteUserRecipeCommand {

    private final UserDomainService userDomainService;
    private final DeleteUserRecipeRepository deleteUserRecipeRepository;

    public DeleteUserRepositoryUseCase(
            UserDomainService userDomainService,
            DeleteUserRecipeRepository deleteUserRecipeRepository
    ) {
        this.userDomainService = userDomainService;
        this.deleteUserRecipeRepository = deleteUserRecipeRepository;
    }

    @Override
    public void execute(Command command) {
        log.info("Executing delete recipe from user with recipe id {}", command.getId());

        User user = userDomainService.getCurrentUser();

        deleteUserRecipeRepository.execute(user.getId(), command.getId());
    }
}
