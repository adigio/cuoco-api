package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllUserMealPrepsQuery;
import com.cuoco.application.port.out.GetAllUserMealPrepsByUserIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserMealPrep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllMealPrepsUseCase implements GetAllUserMealPrepsQuery {

    private final UserDomainService userDomainService;
    private final GetAllUserMealPrepsByUserIdRepository getAllUserMealPrepsByUserIdRepository;

    public GetAllMealPrepsUseCase(
            UserDomainService userDomainService,
            GetAllUserMealPrepsByUserIdRepository getAllUserMealPrepsByUserIdRepository
    ) {
        this.userDomainService = userDomainService;
        this.getAllUserMealPrepsByUserIdRepository = getAllUserMealPrepsByUserIdRepository;
    }

    @Override
    public List<MealPrep> execute() {
        log.info("Executing get all user meal preps use case");

        User user = userDomainService.getCurrentUser();

        List<UserMealPrep> userMealPreps = getAllUserMealPrepsByUserIdRepository.execute(user.getId());

        return userMealPreps.stream().map(UserMealPrep::getMealPrep).toList();
    }
}
