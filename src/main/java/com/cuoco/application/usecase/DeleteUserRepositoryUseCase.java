package com.cuoco.application.usecase;

import com.cuoco.application.port.in.DeleteUserRecipeCommand;
import com.cuoco.application.port.out.DeleteUserRecipeRepository;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteUserRepositoryUseCase implements DeleteUserRecipeCommand {

    private final DeleteUserRecipeRepository deleteUserRecipeRepository;

    public DeleteUserRepositoryUseCase(DeleteUserRecipeRepository deleteUserRecipeRepository) {
        this.deleteUserRecipeRepository = deleteUserRecipeRepository;
    }

    @Override
    public void execute(Command command) {
        log.info("Executing delete recipe from user with recipe id {}", command.getId());

        User user = getUser();

        deleteUserRecipeRepository.execute(user.getId(), command.getId());
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
