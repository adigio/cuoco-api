package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.port.out.CreateAllRecipesRepository;
import com.cuoco.application.port.out.CreateRecipeImagesRepository;
import com.cuoco.application.port.out.GetAllRecipesByIdsRepository;
import com.cuoco.application.port.out.GetRecipeStepsImagesRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class RecipeDomainService {

    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;
    private final GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider;
    private final GetAllRecipesByIdsRepository getAllRecipesByIdsRepository;
    private final CreateAllRecipesRepository createAllRecipesRepository;
    private final CreateRecipeImagesRepository createRecipeImagesRepository;
    private final GetRecipeStepsImagesRepository getRecipeStepsImagesRepository;
    private final AsyncRecipeDomainService asyncRecipeDomainService;
    private final ParametricDataDomainService parametricDataDomainService;

    public RecipeDomainService(
            @Qualifier("repository") GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository,
            @Qualifier("provider") GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider,
            GetAllRecipesByIdsRepository getAllRecipesByIdsRepository,
            CreateAllRecipesRepository createAllRecipesRepository,
            CreateRecipeImagesRepository createRecipeImagesRepository,
            GetRecipeStepsImagesRepository getRecipeStepsImagesRepository,
            AsyncRecipeDomainService asyncRecipeDomainService,
            ParametricDataDomainService parametricDataDomainService
    ) {
        this.getRecipesFromIngredientsRepository = getRecipesFromIngredientsRepository;
        this.getRecipesFromIngredientsProvider = getRecipesFromIngredientsProvider;
        this.getAllRecipesByIdsRepository = getAllRecipesByIdsRepository;
        this.createAllRecipesRepository = createAllRecipesRepository;
        this.createRecipeImagesRepository = createRecipeImagesRepository;
        this.getRecipeStepsImagesRepository = getRecipeStepsImagesRepository;
        this.asyncRecipeDomainService = asyncRecipeDomainService;
        this.parametricDataDomainService = parametricDataDomainService;
    }

    public List<Recipe> getOrCreate(Recipe recipeToFind) {
        List<Recipe> foundedRecipes = getRecipesFromIngredientsRepository.execute(recipeToFind);

        int targetSize = recipeToFind.getConfiguration().getSize();

        if(foundedRecipes.isEmpty()) {
            log.info("Can't find saved recipes with the provided ingredients and filters. Generating new ones");
            return generateRecipes(recipeToFind, List.of(), targetSize);
        }

        if(foundedRecipes.size() < targetSize) {
            int remaining = targetSize - foundedRecipes.size();

            log.info("Founded only {} saved recipes. Generating {} new recipes to complete", foundedRecipes.size(), remaining);

            List<Recipe> newRecipes = generateRecipes(recipeToFind, foundedRecipes, remaining);

            return Stream.concat(foundedRecipes.stream(), newRecipes.stream())
                    .limit(targetSize)
                    .toList();
        }

        log.info("Founded enough {} saved recipes with the provided ingredients and filters.", foundedRecipes.size());
        return foundedRecipes.stream().limit(targetSize).toList();
    }

    private List<Recipe> generateRecipes(Recipe recipeParameters, List<Recipe> foundedRecipes, int size) {

        recipeParameters.getConfiguration().setParametricData(parametricDataDomainService.getAll());

        List<Recipe> recipesToNotInclude = buildRecipesToNotInclude(recipeParameters.getConfiguration().getNotInclude(), foundedRecipes);

        recipeParameters.getConfiguration().setNotInclude(recipesToNotInclude);

        List<Recipe> recipesToSave = getRecipesFromIngredientsProvider.execute(recipeParameters);

        List<Recipe> savedRecipes = createAllRecipesRepository.execute(recipesToSave);

        asyncRecipeDomainService.generateMainImages(savedRecipes);

        return savedRecipes.stream().limit(size).toList();
    }

    public Recipe generateImages(Recipe recipe) {
        log.info("Executing image creation for recipe with ID {}", recipe.getId());

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

    public void adjustIngredientsByServings(Recipe recipe, Integer servings) {
        if(servings != null && servings > 1) {
            log.info("Multiplying recipe ID {} ingredients quantity by {} servings", recipe.getId(), servings);

            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.getQuantity() != null) {
                    ingredient.setQuantity(ingredient.getQuantity() * servings);
                }
            }
        }
    }

    private List<Recipe> buildRecipesToNotInclude(List<Recipe> requiredNotInclude, List<Recipe> foundedRecipes) {
        List<Recipe> recipesToNotInclude = new ArrayList<>(foundedRecipes);

        if (requiredNotInclude != null && !requiredNotInclude.isEmpty()) {
            List<Long> ids = requiredNotInclude.stream()
                    .map(Recipe::getId)
                    .toList();

            List<Recipe> fetched = getAllRecipesByIdsRepository.execute(ids);
            recipesToNotInclude.addAll(fetched);
        }

        return recipesToNotInclude;
    }
}
