package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.port.out.CreateAllRecipesRepository;
import com.cuoco.application.port.out.CreateRecipeImagesRepository;
import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.port.out.GetAllCookLevelsRepository;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.port.out.GetAllDietsRepository;
import com.cuoco.application.port.out.GetAllMealTypesRepository;
import com.cuoco.application.port.out.GetAllPreparationTimesRepository;
import com.cuoco.application.port.out.GetAllUnitsRepository;
import com.cuoco.application.port.out.GetRecipeStepsImagesRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import com.cuoco.shared.utils.ImageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Component
public class RecipeDomainService {

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;
    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider;
    private final CreateAllRecipesRepository createAllRecipesRepository;
    private final CreateRecipeImagesRepository createRecipeImagesRepository;

    private final GetRecipeStepsImagesRepository getRecipeStepsImagesRepository;

    private final AsyncRecipeDomainService asyncRecipeDomainService;
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
            CreateAllRecipesRepository createAllRecipesRepository,
            CreateRecipeImagesRepository createRecipeImagesRepository,
            GetRecipeStepsImagesRepository getRecipeStepsImagesRepository,
            AsyncRecipeDomainService asyncRecipeDomainService,
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
        this.createAllRecipesRepository = createAllRecipesRepository;
        this.createRecipeImagesRepository =  createRecipeImagesRepository;
        this.asyncRecipeDomainService = asyncRecipeDomainService;
        this.getRecipeStepsImagesRepository = getRecipeStepsImagesRepository;
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

        List<Recipe> savedRecipes = createAllRecipesRepository.execute(recipesToSave);

        asyncRecipeDomainService.generateMainImages(savedRecipes);

        return savedRecipes.stream().limit(size).toList();
    }

    public Recipe generateImages(Recipe recipe) {
        log.info("Executing image creation for recipe with ID {}", recipe.getId());

        List<Step> stepsImagesToCreate = splitInstructionsSteps(recipe.getInstructions());
        recipe.setImages(stepsImagesToCreate);

        List<Step> recipeImagesToSave = getRecipeStepsImagesRepository.execute(recipe);
        recipe.setImages(recipeImagesToSave);

        if(!recipe.getImages().isEmpty()) {
            List<Step> savedImages = createRecipeImagesRepository.execute(recipe);
            recipe.setImages(savedImages);
            log.info("Successfully generated {} images for recipe with ID {}", savedImages.size(), recipe.getId());
        } else {
            log.info("Failed to create images for recipe with ID {}", recipe.getId());
        }

        return recipe;
    }

    private List<Step> splitInstructionsSteps(String instructions) {
        int maxStepsSize = Integer.parseInt(ImageConstants.MAX_STEPS_SIZE_INT.getValue());

        List<String> stepsInstructions = Pattern.compile(ImageConstants.INSTRUCTIONS_SPLIT_PATTERN.getValue())
                .splitAsStream(instructions)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .limit(maxStepsSize)
                .toList();

        AtomicInteger stepCounter = new AtomicInteger(1);
        return stepsInstructions.stream()
                .map(stepInstruction -> buildRecipeImage(stepCounter.getAndIncrement(), stepInstruction))
                .toList();

    }

    private Step buildRecipeImage(int currentStepNumber, String currentStepInstruction) {
        return Step.builder()
                .number(currentStepNumber)
                .description(currentStepInstruction)
                .build();
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
