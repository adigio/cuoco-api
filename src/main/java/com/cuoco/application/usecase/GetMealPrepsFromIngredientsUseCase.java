package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealPrepFilter;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.utils.PlanConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class GetMealPrepsFromIngredientsUseCase implements GetMealPrepFromIngredientsCommand {

    private final GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider;

    public GetMealPrepsFromIngredientsUseCase(GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider) {
        this.getMealPrepsFromIngredientsProvider = getMealPrepsFromIngredientsProvider;
    }

    @Override
    public List<MealPrep> execute(Command command) {
        log.info("Executing get recipes from ingredients and filters use case with command {}", command);

        int userPlan = getUserPlan();
        if (userPlan != PlanConstants.PREMIUM.getValue()) {
            log.warn("User plan is not premium. Access denied.");
            throw new IllegalStateException("Only PREMIUM users can generate meal preps");
        }

        MealPrep mealPrepToGenerate = buildMealPrep(command);

        List<MealPrep> foundedMealPreps = getMealPrepsFromIngredientsProvider.execute(mealPrepToGenerate);

        log.info("Generated {} meal preps, returning first", foundedMealPreps.size());
        return foundedMealPreps.stream().limit(1).toList();
    }

    private MealPrep buildMealPrep(Command command) {
        return MealPrep.builder()
                .ingredients(command.getIngredients())
                .filters(buildFiltersMealPrep(command.getFilters()))
                .build();
    }

    private MealPrepFilter buildFiltersMealPrep(MealPrepFilter filter) {
        return MealPrepFilter.builder()
                .difficulty(filter.getDifficulty() != null ? filter.getDifficulty() : null)
                .diet(filter.getDiet() != null ? filter.getDiet() : null)
                .quantity(filter.getQuantity() != null ? filter.getQuantity() : null)
                .freeze(filter.getFreeze() != null ? filter.getFreeze() : null)
                .types(filter.getTypes() != null ? filter.getTypes() : Collections.emptyList())
                .build();
    }

    private int getUserPlan() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getPlan().getId();
    }
}
