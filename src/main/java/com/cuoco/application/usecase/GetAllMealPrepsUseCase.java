package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllUserMealPrepsQuery;
import com.cuoco.application.port.out.GetAllUserMealPrepsByUserIdRepository;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserMealPrep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllMealPrepsUseCase implements GetAllUserMealPrepsQuery {

    private final GetAllUserMealPrepsByUserIdRepository getAllUserMealPrepsByUserIdRepository;

    public GetAllMealPrepsUseCase(GetAllUserMealPrepsByUserIdRepository getAllUserMealPrepsByUserIdRepository) {
        this.getAllUserMealPrepsByUserIdRepository = getAllUserMealPrepsByUserIdRepository;
    }

    @Override
    public List<MealPrep> execute() {
        log.info("Executing get all user meal preps use case");

        User user = getUser();

        List<UserMealPrep> userMealPreps = getAllUserMealPrepsByUserIdRepository.execute(user.getId());

        return userMealPreps.stream().map(UserMealPrep::getMealPrep).toList();
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
