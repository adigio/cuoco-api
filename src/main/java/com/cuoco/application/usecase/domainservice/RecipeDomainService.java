package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.port.out.GetAllMealTypesRepository;
import com.cuoco.application.port.out.GetAllPreparationTimesRepository;
import com.cuoco.application.port.out.GetAllUnitsRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class RecipeDomainService {

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider;
    private final CreateRecipeRepository createRecipeRepository;

    private final GetAllUnitsRepository getAllUnitsRepository;
    private final GetAllPreparationTimesRepository getAllPreparationTimesRepository;
    private final GetAllCookLevelsRepository getAllCookLevelsRepository;
    private final GetAllDietsRepository getAllDietsRepository;
    private final GetAllMealTypesRepository getAllMealTypesRepository;
    private final GetAllAllergiesRepository getAllAllergiesRepository;
    private final GetAllDietaryNeedsRepository getAllDietaryNeedsRepository;

    public RecipeDomainService(
            @Qualifier("provider") GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider,
            CreateRecipeRepository createRecipeRepository,
            GetAllUnitsRepository getAllUnitsRepository,
            GetAllPreparationTimesRepository getAllPreparationTimesRepository,
            GetAllCookLevelsRepository getAllCookLevelsRepository,
            GetAllDietsRepository getAllDietsRepository,
            GetAllMealTypesRepository getAllMealTypesRepository,
            GetAllAllergiesRepository getAllAllergiesRepository,
            GetAllDietaryNeedsRepository getAllDietaryNeedsRepository
    ) {
        this.getRecipesFromIngredientsProvider = getRecipesFromIngredientsProvider;
        this.createRecipeRepository = createRecipeRepository;
        this.getAllUnitsRepository = getAllUnitsRepository;
        this.getAllPreparationTimesRepository = getAllPreparationTimesRepository;
        this.getAllCookLevelsRepository = getAllCookLevelsRepository;
        this.getAllDietsRepository = getAllDietsRepository;
        this.getAllMealTypesRepository = getAllMealTypesRepository;
        this.getAllAllergiesRepository = getAllAllergiesRepository;
        this.getAllDietaryNeedsRepository = getAllDietaryNeedsRepository;
    }

    public List<Recipe> generateIfNeeded(Recipe input, List<Recipe> existing) {
        int targetSize = input.getConfiguration().getSize();

        if(existing.isEmpty()) {
            log.info("Can't find saved recipes with the provided ingredients and filters. Generating new ones");

            input.getConfiguration().setParametricData(buildParametricData());

            return getRecipesFromIngredientsProvider.execute(input)
                    .stream()
                    .map(createRecipeRepository::execute)
                    .limit(targetSize)
                    .toList();
        }

        if(existing.size() < targetSize) {
            int remaining = targetSize - existing.size();

            log.info("Founded only {} saved recipes. Generating {} new recipes to complete", existing.size(), remaining);

            input.getConfiguration().setParametricData(buildParametricData());

            List<Recipe> newRecipes = getRecipesFromIngredientsProvider.execute(input).stream()
                    .map(createRecipeRepository::execute)
                    .limit(remaining)
                    .toList();

            return Stream.concat(existing.stream(), newRecipes.stream())
                    .limit(targetSize)
                    .toList();

        }

        log.info("Founded enough {} saved recipes with the provided ingredients and filters.", existing.size());
        return existing.stream().limit(targetSize).toList();
    }

    private ParametricData buildParametricData() {
        return ParametricData.builder()
                .units(getAllUnitsRepository.execute())
                .preparationTimes(getAllPreparationTimesRepository.execute())
                .cookLevels(getAllCookLevelsRepository.execute())
                .diets(getAllDietsRepository.execute())
                .mealTypes(getAllMealTypesRepository.execute())
                .allergies(getAllAllergiesRepository.execute())
                .dietaryNeeds(getAllDietaryNeedsRepository.execute())
                .build();
    }
}
