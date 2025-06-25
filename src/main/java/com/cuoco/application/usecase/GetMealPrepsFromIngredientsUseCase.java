package com.cuoco.application.usecase;

import com.cuoco.application.exception.ForbiddenException;
import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.port.out.GetPreparationTimeByIdRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealPrepFilter;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.PlanConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class GetMealPrepsFromIngredientsUseCase implements GetMealPrepFromIngredientsCommand {

    private final GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider;

    private final GetPreparationTimeByIdRepository getPreparationTimeByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetMealTypeByIdRepository getMealTypeByIdRepository;
    private final GetDietByIdRepository getDietByIdRepository;
    private final GetAllergiesByIdRepository getAllergiesByIdRepository;
    private final GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;

    public GetMealPrepsFromIngredientsUseCase(
            @Qualifier("provider") GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider,
            GetPreparationTimeByIdRepository getPreparationTimeByIdRepository,
            GetCookLevelByIdRepository getCookLevelByIdRepository,
            GetMealTypeByIdRepository getMealTypeByIdRepository,
            GetDietByIdRepository getDietByIdRepository,
            GetAllergiesByIdRepository getAllergiesByIdRepository,
            GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository
    ) {
        this.getMealPrepsFromIngredientsProvider = getMealPrepsFromIngredientsProvider;
        this.getPreparationTimeByIdRepository = getPreparationTimeByIdRepository;
        this.getCookLevelByIdRepository = getCookLevelByIdRepository;
        this.getMealTypeByIdRepository = getMealTypeByIdRepository;
        this.getDietByIdRepository = getDietByIdRepository;
        this.getAllergiesByIdRepository = getAllergiesByIdRepository;
        this.getDietaryNeedsByIdRepository = getDietaryNeedsByIdRepository;
    }

    @Override
    public List<MealPrep> execute(Command command) {
        log.info("Executing get recipes from ingredients and filters use case with command {}", command);

        int userPlan = getUserPlan();
        if (userPlan != PlanConstants.PREMIUM.getValue()) {
            log.warn("User plan is not premium. Access denied.");
            throw new ForbiddenException(ErrorDescription.PRO_FEATURE.getValue());
        }

        MealPrep mealPrepToGenerate = buildMealPrep(command);

        List<MealPrep> foundedMealPreps = getMealPrepsFromIngredientsProvider.execute(mealPrepToGenerate);

        log.info("Generated {} meal preps, returning first", foundedMealPreps.size());
        return foundedMealPreps.stream().limit(1).toList();
    }

    private MealPrep buildMealPrep(Command command) {
        return MealPrep.builder()
                .ingredients(command.getIngredients())
                .filters(buildFiltersMealPrep(command))
                .build();
    }

    private MealPrepFilter buildFiltersMealPrep(Command command) {
        PreparationTime preparationTime = command.getPreparationTimeId() != null ? getPreparationTimeByIdRepository.execute(command.getPreparationTimeId()) : null;
        CookLevel cookLevel = command.getCookLevelId() != null ? getCookLevelByIdRepository.execute(command.getCookLevelId()) : null;
        Diet diet = command.getDietId() != null ? getDietByIdRepository.execute(command.getDietId()) : null;
        List<MealType> types = command.getTypeIds() != null ? command.getTypeIds().stream().map(getMealTypeByIdRepository::execute).toList() : null;
        List<DietaryNeed> dietaryNeeds = command.getDietaryNeedsIds() != null ? getDietaryNeedsByIdRepository.execute(command.getDietaryNeedsIds()) : null;
        List<Allergy> allergies = command.getAllergiesIds() != null ? getAllergiesByIdRepository.execute(command.getAllergiesIds()) : null;

        return MealPrepFilter.builder()
                .freeze(command.getFreeze())
                .servings(command.getServings())
                .preparationTime(preparationTime)
                .cookLevel(cookLevel)
                .mealTypes(types)
                .diet(diet)
                .allergies(allergies)
                .dietaryNeeds(dietaryNeeds)
                .build();
    }

    private int getUserPlan() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getPlan().getId();
    }
}
