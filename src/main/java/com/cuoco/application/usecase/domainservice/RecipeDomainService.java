package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.port.out.CreateRecipeImagesRepository;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.GenerateRecipeImagesRepository;
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
import com.cuoco.application.usecase.model.RecipeImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class RecipeDomainService {

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;
    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider;
    private final CreateRecipeRepository createRecipeRepository;
    private final CreateRecipeImagesRepository createRecipeImagesRepository;

    private final GenerateRecipeImagesRepository generateRecipeImagesRepository;

    private final GetAllUnitsRepository getAllUnitsRepository;
    private final GetAllPreparationTimesRepository getAllPreparationTimesRepository;
    private final GetAllCookLevelsRepository getAllCookLevelsRepository;
    private final GetAllDietsRepository getAllDietsRepository;
    private final GetAllMealTypesRepository getAllMealTypesRepository;
    private final GetAllAllergiesRepository getAllAllergiesRepository;
    private final GetAllDietaryNeedsRepository getAllDietaryNeedsRepository;

    public RecipeDomainService(
            @Qualifier("repository") GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository,
            @Qualifier("provider") GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider,
            CreateRecipeRepository createRecipeRepository,
            CreateRecipeImagesRepository createRecipeImagesRepository,
            GenerateRecipeImagesRepository generateRecipeImagesRepository,
            GetAllUnitsRepository getAllUnitsRepository,
            GetAllPreparationTimesRepository getAllPreparationTimesRepository,
            GetAllCookLevelsRepository getAllCookLevelsRepository,
            GetAllDietsRepository getAllDietsRepository,
            GetAllMealTypesRepository getAllMealTypesRepository,
            GetAllAllergiesRepository getAllAllergiesRepository,
            GetAllDietaryNeedsRepository getAllDietaryNeedsRepository
    ) {
        this.getRecipesFromIngredientsRepository = getRecipesFromIngredientsRepository;
        this.getRecipesFromIngredientsProvider = getRecipesFromIngredientsProvider;
        this.createRecipeRepository = createRecipeRepository;
        this.createRecipeImagesRepository =  createRecipeImagesRepository;
        this.generateRecipeImagesRepository = generateRecipeImagesRepository;
        this.getAllUnitsRepository = getAllUnitsRepository;
        this.getAllPreparationTimesRepository = getAllPreparationTimesRepository;
        this.getAllCookLevelsRepository = getAllCookLevelsRepository;
        this.getAllDietsRepository = getAllDietsRepository;
        this.getAllMealTypesRepository = getAllMealTypesRepository;
        this.getAllAllergiesRepository = getAllAllergiesRepository;
        this.getAllDietaryNeedsRepository = getAllDietaryNeedsRepository;
    }

    public List<Recipe> getOrCreate(Recipe recipeToFind) {
        List<Recipe> foundedRecipes = getRecipesFromIngredientsRepository.execute(recipeToFind);

        int targetSize = recipeToFind.getConfiguration().getSize();

        if(foundedRecipes.isEmpty()) {
            log.info("Can't find saved recipes with the provided ingredients and filters. Generating new ones");

            recipeToFind.getConfiguration().setParametricData(buildParametricData());

            return generateRecipes(recipeToFind, targetSize);
        }

        if(foundedRecipes.size() < targetSize) {
            int remaining = targetSize - foundedRecipes.size();

            log.info("Founded only {} saved recipes. Generating {} new recipes to complete", foundedRecipes.size(), remaining);

            recipeToFind.getConfiguration().setParametricData(buildParametricData());

            List<Recipe> newRecipes = generateRecipes(recipeToFind, remaining);

            return Stream.concat(foundedRecipes.stream(), newRecipes.stream())
                    .limit(targetSize)
                    .toList();
        }

        log.info("Founded enough {} saved recipes with the provided ingredients and filters.", foundedRecipes.size());
        return foundedRecipes.stream().limit(targetSize).toList();
    }

    private List<Recipe> generateRecipes(Recipe recipeParameters, int size) {
        List<Recipe> recipesToSave = getRecipesFromIngredientsProvider.execute(recipeParameters);

        List<Recipe> createdRecipes = recipesToSave.stream().map(recipe -> {
            Recipe savedRecipe = createRecipeRepository.execute(recipe);
            return generateImages(savedRecipe);
        }).toList();

        return createdRecipes.stream().limit(size).toList();
    }

    public Recipe generateImages(Recipe recipe) {
        log.info("Executing image creation for recipe with ID {}", recipe.getId());

        recipe.setImages(generateRecipeImagesRepository.execute(recipe));

        List<RecipeImage> savedImages = createRecipeImagesRepository.execute(recipe);

        recipe.setImages(savedImages);

        log.info("Successfully generated {} images for recipe with ID {}", savedImages.size(), recipe.getId());

        return recipe;
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
