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
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateUserMealPrepUseCase implements CreateUserMealPrepCommand {

    private final UserDomainService userDomainService;
    private final CreateUserMealPrepRepository createUserMealPrepRepository;
    private final ExistsUserMealPrepRepository existsUserMealPrepRepository;
    private final GetMealPrepByIdRepository getMealPrepByIdRepository;

    public CreateUserMealPrepUseCase(
            UserDomainService userDomainService,
            CreateUserMealPrepRepository createUserMealPrepRepository,
            ExistsUserMealPrepRepository existsUserMealPrepRepository,
            GetMealPrepByIdRepository getMealPrepByIdRepository
    ) {
        this.userDomainService = userDomainService;
        this.createUserMealPrepRepository = createUserMealPrepRepository;
        this.existsUserMealPrepRepository = existsUserMealPrepRepository;
        this.getMealPrepByIdRepository = getMealPrepByIdRepository;
    }

    @Override
    public void execute(Command command) {
        log.info("Executing create user meal prep use case with command {}", command);

        User user = userDomainService.getCurrentUser();

        MealPrep mealPrep = getMealPrepByIdRepository.execute(command.getId());

        UserMealPrep userMealPrep = buildUserMealPrep(user, mealPrep);

        if (existsUserMealPrepRepository.execute(userMealPrep)) {
            log.info("Meal prep already saved by user with ID {}", user.getId());
            throw new ConflictException(ErrorDescription.DUPLICATED.getValue());
        }

        createUserMealPrepRepository.execute(userMealPrep);
    }

    private UserMealPrep buildUserMealPrep(User user, MealPrep mealPrep) {
        return UserMealPrep.builder()
                .user(user)
                .mealPrep(mealPrep)
                .build();
    }
}
