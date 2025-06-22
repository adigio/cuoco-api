package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.port.out.GetMealCategoryByIdRepository;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.port.out.GetPreparationTimeByIdRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.MealCategory;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class GetRecipesFromIngredientsUseCase implements GetRecipesFromIngredientsCommand {

    @Value("${shared.plan.free.max-recipes}")
    private int FREE_MAX_RECIPES;

    @Value("${shared.plan.premium.max-recipes}")
    private int PREMIUM_MAX_RECIPES;

    private final GetPreparationTimeByIdRepository getPreparationTimeByIdRepository;
    private final GetCookLevelByIdRepository getCookLevelByIdRepository;
    private final GetMealTypeByIdRepository getMealTypeByIdRepository;
    private final GetMealCategoryByIdRepository getMealCategoryByIdRepository;

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;
    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider;
    private final CreateRecipeRepository createRecipeRepository;

    public GetRecipesFromIngredientsUseCase(
            GetPreparationTimeByIdRepository getPreparationTimeByIdRepository,
            GetCookLevelByIdRepository getCookLevelByIdRepository,
            GetMealTypeByIdRepository getMealTypeByIdRepository,
            GetMealCategoryByIdRepository getMealCategoryByIdRepository,
            @Qualifier("repository") GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository,
            @Qualifier("provider") GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider,
            CreateRecipeRepository createRecipeRepository
    ) {
        this.getPreparationTimeByIdRepository = getPreparationTimeByIdRepository;
        this.getCookLevelByIdRepository = getCookLevelByIdRepository;
        this.getMealTypeByIdRepository = getMealTypeByIdRepository;
        this.getMealCategoryByIdRepository = getMealCategoryByIdRepository;
        this.getRecipesFromIngredientsRepository = getRecipesFromIngredientsRepository;
        this.getRecipesFromIngredientsProvider = getRecipesFromIngredientsProvider;
        this.createRecipeRepository = createRecipeRepository;
    }

    public List<Recipe> execute(Command command) {
        log.info("Executing get recipes from ingredients and filters use case with command {}", command);

        int userPlan = getUserPlan();

        Recipe recipeToGenerate = buildRecipe(command, userPlan);
        Integer recipesSize = recipeToGenerate.getConfiguration().getSize();

        List<Recipe> foundedRecipes = getRecipesFromIngredientsRepository.execute(recipeToGenerate);

        if (!foundedRecipes.isEmpty() && foundedRecipes.size() >= recipesSize) {
            log.info("Founded enough {} saved recipes with the provided ingredients and filters.", foundedRecipes.size());
            return foundedRecipes.stream().limit(recipesSize).toList();
        }

        List<Recipe> recipesToSave;
        List<Recipe> savedRecipes;

        if (!foundedRecipes.isEmpty()) {
            int recipesNeeded = recipesSize - foundedRecipes.size();

            log.info("Founded only {} saved recipes. Generating {} new recipes to complete", foundedRecipes.size(), recipesNeeded);

            recipesToSave = getRecipesFromIngredientsProvider.execute(recipeToGenerate);
            savedRecipes = recipesToSave.stream().map(createRecipeRepository::execute).limit(recipesNeeded).toList();

            return Stream.concat(foundedRecipes.stream(), savedRecipes.stream()).limit(maxRecipesToGenerate).toList();
        }

        log.info("Can't find saved recipes with the provided ingredients and filters. Generating new ones");

        recipesToSave = getRecipesFromIngredientsProvider.execute(recipeToGenerate);
        savedRecipes = recipesToSave.stream().map(createRecipeRepository::execute).toList();

        return savedRecipes.stream().limit(recipesSize).toList();
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
        List<MealType> types = command.getTypeIds() != null ? command.getTypeIds().stream().map(getMealTypeByIdRepository::execute).toList() : null;
        List<MealCategory> categories = command.getCategoryIds() != null ? command.getCategoryIds().stream().map(getMealCategoryByIdRepository::execute).toList() : null;

        return RecipeFilter.builder()
                .preparationTime(preparationTime)
                .servings(command.getServings())
                .cookLevel(cookLevel)
                .types(types)
                .categories(categories)
                .enable(true)
                .build();
    }

    private RecipeConfiguration buildConfiguration(Command command, int userPlan) {

        Integer recipesSize;

        if(userPlan == PlanConstants.PREMIUM.getValue()) {
            recipesSize = command.getRecipesSize() != null ? command.getRecipesSize() : PREMIUM_MAX_RECIPES;

            return RecipeConfiguration.builder()
                    .size(recipesSize)
                    .notInclude(command.getNotInclude())
                    .build();
        } else {
            return RecipeConfiguration.builder()
                    .size(FREE_MAX_RECIPES)
                    .build();
        }
    }

    private int getUserPlan() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getPlan().getId();
    }
}
