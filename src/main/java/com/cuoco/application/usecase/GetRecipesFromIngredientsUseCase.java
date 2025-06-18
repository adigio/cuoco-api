package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.application.usecase.model.User;
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

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;
    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider;
    private final CreateRecipeRepository createRecipeRepository;

    public GetRecipesFromIngredientsUseCase(
            @Qualifier("repository") GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository,
            @Qualifier("provider") GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider,
            CreateRecipeRepository createRecipeRepository
    ) {
        this.getRecipesFromIngredientsRepository = getRecipesFromIngredientsRepository;
        this.getRecipesFromIngredientsProvider = getRecipesFromIngredientsProvider;
        this.createRecipeRepository = createRecipeRepository;
    }

    public List<Recipe> execute(Command command) {
        log.info("Executing get recipes from ingredients and filters use case with command {}", command);

        int userPlan = getUserPlan();
        int maxRecipesToGenerate = userPlan == PlanConstants.FREE.getValue() ? FREE_MAX_RECIPES : PREMIUM_MAX_RECIPES;
        Recipe recipeToGenerate = buildRecipe(command, userPlan);

        List<Recipe> foundedRecipes = getRecipesFromIngredientsRepository.execute(recipeToGenerate);

        if (!foundedRecipes.isEmpty() && foundedRecipes.size() >= maxRecipesToGenerate) {
            log.info("Founded enough {} saved recipes with the provided ingredients and filters.", foundedRecipes.size());
            return foundedRecipes.stream().limit(maxRecipesToGenerate).toList();
        }

        List<Recipe> recipesToSave;
        List<Recipe> savedRecipes;

        if (!foundedRecipes.isEmpty()) {
            int recipesNeeded = maxRecipesToGenerate - foundedRecipes.size();

            log.info("Founded only {} saved recipes. Generating {} new recipes to complete", foundedRecipes.size(), recipesNeeded);

            recipesToSave = getRecipesFromIngredientsProvider.execute(recipeToGenerate);
            savedRecipes = recipesToSave.stream().map(createRecipeRepository::execute).limit(recipesNeeded).toList();

            return Stream.concat(foundedRecipes.stream(), savedRecipes.stream()).toList();
        }

        log.info("Can't find saved recipes with the provided ingredients and filters. Generating new ones");

        recipesToSave = getRecipesFromIngredientsProvider.execute(recipeToGenerate);
        savedRecipes = recipesToSave.stream().map(createRecipeRepository::execute).toList();

        return savedRecipes.stream().limit(maxRecipesToGenerate).toList();
    }

    private Recipe buildRecipe(Command command, int userPlan) {
        return Recipe.builder()
                .ingredients(command.getIngredients())
                .filters(buildFilters(command.getFilters(), userPlan))
                .build();
    }

    private RecipeFilter buildFilters(RecipeFilter filter, int userPlan) {

        int maxRecipesToGenerate = userPlan == PlanConstants.FREE.getValue() ? FREE_MAX_RECIPES : PREMIUM_MAX_RECIPES;

        if(filter != null) {
            return RecipeFilter.builder()
                    .time(filter.getTime() != null ? filter.getTime() : null)
                    .difficulty(filter.getDifficulty() != null ? filter.getDifficulty() : null)
                    .types(filter.getTypes() != null ? filter.getTypes() : Collections.emptyList())
                    .diet(filter.getDiet() != null ? filter.getDiet() : null)
                    .quantity(filter.getQuantity() != null ? filter.getQuantity() : null)
                    .maxRecipes(maxRecipesToGenerate)
                    .enable(userPlan == PlanConstants.PREMIUM.getValue())
                    .build();
        } else {
            return RecipeFilter.builder()
                    .maxRecipes(maxRecipesToGenerate)
                    .enable(false)
                    .build();
        }


    }

    private int getUserPlan() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getPlan().getId();
    }

}
