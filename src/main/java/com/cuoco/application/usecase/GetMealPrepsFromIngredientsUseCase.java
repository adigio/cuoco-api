package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.exception.ForbiddenException;
import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.port.out.CreateAllMealPrepsRepository;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.port.out.GetPreparationTimeByIdRepository;
import com.cuoco.application.usecase.domainservice.RecipeDomainService;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Filters;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeConfiguration;
import com.cuoco.application.usecase.model.User;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.PlanConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetMealPrepsFromIngredientsUseCase implements GetMealPrepFromIngredientsCommand {

    @Value("${shared.meal-preps.recipes-size}")
    private int MEAL_PREP_RECIPES_SIZE;

    private final RecipeDomainService recipeDomainService;
    private final GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider;
    private final CreateAllMealPrepsRepository createAllMealPrepsRepository;
    private final GetPreparationTimeByIdRepository getPreparationTimeByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetMealTypeByIdRepository getMealTypeByIdRepository;
    private final GetDietByIdRepository getDietByIdRepository;
    private final GetAllergiesByIdRepository getAllergiesByIdRepository;
    private final GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository;

    public GetMealPrepsFromIngredientsUseCase(
            RecipeDomainService recipeDomainService,
            @Qualifier("provider") GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider,
            CreateAllMealPrepsRepository createAllMealPrepsRepository,
            GetPreparationTimeByIdRepository getPreparationTimeByIdRepository,
            GetCookLevelByIdRepository getCookLevelByIdRepository,
            GetMealTypeByIdRepository getMealTypeByIdRepository,
            GetDietByIdRepository getDietByIdRepository,
            GetAllergiesByIdRepository getAllergiesByIdRepository,
            GetDietaryNeedsByIdRepository getDietaryNeedsByIdRepository
    ) {
        this.recipeDomainService = recipeDomainService;
        this.getMealPrepsFromIngredientsProvider = getMealPrepsFromIngredientsProvider;
        this.createAllMealPrepsRepository = createAllMealPrepsRepository;
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

        User user = validateAndGetUser();

        Recipe recipeParameters = buildRecipe(command);
        List<Recipe> recipes = recipeDomainService.getOrCreate(recipeParameters);

        MealPrep mealPrepToGenerate = buildMealPrep(
                user,
                recipeParameters.getIngredients(),
                recipes,
                recipeParameters.getFilters()
        );

        List<MealPrep> generatedMealPreps = getMealPrepsFromIngredientsProvider.execute(mealPrepToGenerate);
        List<MealPrep> savedMealPrep = createAllMealPrepsRepository.execute(generatedMealPreps);

        log.info("Generated {} meal preps, returning first", savedMealPrep.size());
        return savedMealPrep;
    }

    private User validateAndGetUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getPlan().getId() != PlanConstants.PRO.getValue()) {
            log.warn("User plan is not PRO. Access denied.");
            throw new ForbiddenException(ErrorDescription.PRO_FEATURE.getValue());
        }

        return user;
    }

    private Recipe buildRecipe(Command command) {

        if(command.getIngredients().isEmpty()) {
            throw new BadRequestException(ErrorDescription.INGREDIENTS_EMPTY.getValue());
        }

        return Recipe.builder()
                .ingredients(command.getIngredients())
                .filters(buildFilters(command))
                .configuration(buildConfiguration())
                .build();
    }

    private Filters buildFilters(Command command) {
        PreparationTime preparationTime = command.getPreparationTimeId() != null ? getPreparationTimeByIdRepository.execute(command.getPreparationTimeId()) : null;
        CookLevel cookLevel = command.getCookLevelId() != null ? getCookLevelByIdRepository.execute(command.getCookLevelId()) : null;
        Diet diet = command.getDietId() != null ? getDietByIdRepository.execute(command.getDietId()) : null;
        List<MealType> types = command.getTypeIds() != null ? command.getTypeIds().stream().map(getMealTypeByIdRepository::execute).toList() : null;
        List<DietaryNeed> dietaryNeeds = command.getDietaryNeedsIds() != null ? getDietaryNeedsByIdRepository.execute(command.getDietaryNeedsIds()) : null;
        List<Allergy> allergies = command.getAllergiesIds() != null ? getAllergiesByIdRepository.execute(command.getAllergiesIds()) : null;
        Boolean freeze = command.getFreeze() != null ? command.getFreeze() : Boolean.FALSE;

        return Filters.builder()
                .enable(true)
                .freeze(freeze)
                .servings(command.getServings())
                .preparationTime(preparationTime)
                .cookLevel(cookLevel)
                .mealTypes(types)
                .diet(diet)
                .allergies(allergies)
                .dietaryNeeds(dietaryNeeds)
                .build();
    }

    private RecipeConfiguration buildConfiguration() {
        return RecipeConfiguration.builder()
                .size(MEAL_PREP_RECIPES_SIZE)
                .build();
    }

    private MealPrep buildMealPrep(User user, List<Ingredient> ingredients, List<Recipe> recipes, Filters filters) {
        return MealPrep.builder()
                .user(user)
                .ingredients(ingredients)
                .recipes(recipes)
                .filters(filters)
                .build();
    }
}
