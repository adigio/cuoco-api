package com.cuoco.application.usecase;

import com.cuoco.application.port.in.DeleteUserMealPrepCommand;
import com.cuoco.application.port.out.DeleteUserMealPrepRepository;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteUserMealPrepUseCase implements DeleteUserMealPrepCommand {

    private final DeleteUserMealPrepRepository deleteUserMealPrepRepository;

    public DeleteUserMealPrepUseCase(DeleteUserMealPrepRepository deleteUserMealPrepRepository) {
        this.deleteUserMealPrepRepository = deleteUserMealPrepRepository;
    }

    @Override
    public void execute(Command command) {
        log.info("Executing delete meal prep from user with meal prep id {}", command.getId());

        User user = getUser();

        deleteUserMealPrepRepository.execute(user.getId(), command.getId());
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
