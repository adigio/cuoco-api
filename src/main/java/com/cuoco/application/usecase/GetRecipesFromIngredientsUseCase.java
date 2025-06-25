package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.port.out.GetPreparationTimeByIdRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.domainservice.RecipeDomainService;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeConfiguration;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.PlanConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class GetRecipesFromIngredientsUseCase implements GetRecipesFromIngredientsCommand {

    @Value("${shared.plan.free.max-recipes}")
    private int FREE_MAX_RECIPES;

    @Value("${shared.plan.premium.max-recipes}")
    private int PREMIUM_MAX_RECIPES;

    private final RecipeDomainService recipeDomainService;

    private final GetPreparationTimeByIdRepository getPreparationTimeByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetMealTypeByIdRepository getMealTypeByIdRepository;
    private final GetDietByIdRepository getDietByIdRepository;
    private final GetAllergiesByIdRepository getAllergiesByIdRepository;
    private final GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;

    public GetRecipesFromIngredientsUseCase(
            RecipeDomainService recipeDomainService,
            GetPreparationTimeByIdRepository getPreparationTimeByIdRepository,
            GetCookLevelByIdRepository getCookLevelByIdRepository,
            GetMealTypeByIdRepository getMealTypeByIdRepository,
            GetDietByIdRepository getDietByIdRepository,
            GetAllergiesByIdRepository getAllergiesByIdRepository,
            GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository
    ) {
        this.recipeDomainService = recipeDomainService;
        this.getPreparationTimeByIdRepository = getPreparationTimeByIdRepository;
        this.getCookLevelByIdRepository = getCookLevelByIdRepository;
        this.getMealTypeByIdRepository = getMealTypeByIdRepository;
        this.getDietByIdRepository = getDietByIdRepository;
        this.getAllergiesByIdRepository = getAllergiesByIdRepository;
        this.getDietaryNeedsByIdRepository = getDietaryNeedsByIdRepository;
    }

    public List<Recipe> execute(Command command) {
        log.info("Executing get recipes from ingredients and filters use case with command {}", command);

        int userPlan = getUserPlan();

        Recipe recipeToFind = buildRecipe(command, userPlan);

        List<Recipe> recipesToResponse = recipeDomainService.getOrCreate(recipeToFind);

        return recipesToResponse;
    }

    private int getUserPlan() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getPlan().getId();
    }

    private Recipe buildRecipe(Command command, int userPlan) {

        if(command.getIngredients().isEmpty()) {
            throw new BadRequestException(ErrorDescription.INGREDIENTS_EMPTY.getValue());
        }

        return Recipe.builder()
                .ingredients(command.getIngredients())
                .filters(buildFilters(command, userPlan))
                .configuration(buildConfiguration(command, userPlan))
                .build();
    }

    private RecipeFilter buildFilters(Command command, int userPlan) {

        if(userPlan == PlanConstants.FREE.getValue() || !command.getFiltersEnabled()) {
            return RecipeFilter.builder()
                    .enable(false)
                    .build();
        }

        PreparationTime preparationTime = command.getPreparationTimeId() != null ? getPreparationTimeByIdRepository.execute(command.getPreparationTimeId()) : null;
        CookLevel cookLevel = command.getCookLevelId() != null ? getCookLevelByIdRepository.execute(command.getCookLevelId()) : null;
        Diet diet = command.getDietId() != null ? getDietByIdRepository.execute(command.getDietId()) : null;
        List<MealType> types = command.getTypeIds() != null ? command.getTypeIds().stream().map(getMealTypeByIdRepository::execute).toList() : null;
        List<DietaryNeed> dietaryNeeds = command.getDietaryNeedsIds() != null ? getDietaryNeedsByIdRepository.execute(command.getDietaryNeedsIds()) : null;
        List<Allergy> allergies = command.getAllergiesIds() != null ? getAllergiesByIdRepository.execute(command.getAllergiesIds()) : null;

        return RecipeFilter.builder()
                .enable(true)
                .servings(command.getServings())
                .preparationTime(preparationTime)
                .cookLevel(cookLevel)
                .types(types)
                .diet(diet)
                .allergies(allergies)
                .dietaryNeeds(dietaryNeeds)
                .build();
    }

    private RecipeConfiguration buildConfiguration(Command command, int userPlan) {
        if(userPlan == PlanConstants.PREMIUM.getValue()) {
            int size = command.getSize() != null ? command.getSize() : PREMIUM_MAX_RECIPES;

            return RecipeConfiguration.builder()
                    .size(size)
                    .notInclude(command.getNotInclude())
                    .build();
        } else {
            return RecipeConfiguration.builder()
                    .size(FREE_MAX_RECIPES)
                    .build();
        }
    }
}
