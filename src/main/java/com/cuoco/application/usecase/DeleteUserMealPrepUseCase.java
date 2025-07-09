package com.cuoco.application.usecase;

import com.cuoco.application.port.in.DeleteUserMealPrepCommand;
import com.cuoco.application.port.out.DeleteUserMealPrepRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteUserMealPrepUseCase implements DeleteUserMealPrepCommand {

    private final UserDomainService userDomainService;
    private final DeleteUserMealPrepRepository deleteUserMealPrepRepository;

    public DeleteUserMealPrepUseCase(
            UserDomainService userDomainService,
            DeleteUserMealPrepRepository deleteUserMealPrepRepository
    ) {
        this.userDomainService = userDomainService;
        this.deleteUserMealPrepRepository = deleteUserMealPrepRepository;
    }

    @Override
    public void execute(Command command) {
        log.info("Executing delete meal prep from user with meal prep id {}", command.getId());

        User user = userDomainService.getCurrentUser();

        deleteUserMealPrepRepository.execute(user.getId(), command.getId());
    }
}
