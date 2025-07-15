package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetMealPrepByIdQuery;
import com.cuoco.application.port.out.GetMealPrepByIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetMealPrepByIdUseCase implements GetMealPrepByIdQuery {

    private final UserDomainService userDomainService;
    private final GetMealPrepByIdRepository getMealPrepByIdRepository;

    @Override
    public MealPrep execute(Long id) {
        log.info("Executing get meal prep by id use case with ID: {}", id);

        MealPrep mealPrep = getMealPrepByIdRepository.execute(id);

        isFavorite(mealPrep);

        return mealPrep;
    }

    private void isFavorite(MealPrep mealPrep) {
        User user = userDomainService.getCurrentUser();
        mealPrep.setFavorite(user.getMealPreps().contains(mealPrep));
    }
}
