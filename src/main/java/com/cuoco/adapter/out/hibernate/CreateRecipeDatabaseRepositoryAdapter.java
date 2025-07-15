package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeStepsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateIngredientHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeIngredientsHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.FindRecipeByNameHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetIngredientByNameHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class CreateRecipeDatabaseRepositoryAdapter implements CreateRecipeRepository {

    private final GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter;
    private final CreateRecipeHibernateRepositoryAdapter createRecipeHibernateRepositoryAdapter;
    private final CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter;
    private final CreateRecipeIngredientsHibernateRepositoryAdapter createRecipeIngredientsHibernateRepositoryAdapter;
    private final FindRecipeByNameHibernateRepositoryAdapter findRecipeByNameHibernateRepositoryAdapter;

    public CreateRecipeDatabaseRepositoryAdapter(
            GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter,
            CreateRecipeHibernateRepositoryAdapter createRecipeHibernateRepositoryAdapter,
            CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter,
            CreateRecipeIngredientsHibernateRepositoryAdapter createRecipeIngredientsHibernateRepositoryAdapter,
            FindRecipeByNameHibernateRepositoryAdapter findRecipeByNameHibernateRepositoryAdapter
    ) {
        this.getIngredientByNameHibernateRepositoryAdapter = getIngredientByNameHibernateRepositoryAdapter;
        this.createRecipeHibernateRepositoryAdapter = createRecipeHibernateRepositoryAdapter;
        this.createIngredientHibernateRepositoryAdapter = createIngredientHibernateRepositoryAdapter;
        this.createRecipeIngredientsHibernateRepositoryAdapter = createRecipeIngredientsHibernateRepositoryAdapter;
        this.findRecipeByNameHibernateRepositoryAdapter = findRecipeByNameHibernateRepositoryAdapter;
    }

    @Override
    public Recipe execute(Recipe recipe) {
        log.info("Saving recipe and ingredients in database: {}", recipe);

        RecipeHibernateModel savedRecipe = createRecipeHibernateRepositoryAdapter.save(buildRecipeHibernateModel(recipe));

        List<RecipeIngredientsHibernateModel> recipeIngredientsHibernateModels = recipe.getIngredients().stream().map(ingredient -> buildRecipeIngredientHibernateModel(savedRecipe, ingredient)).toList();
        List<RecipeIngredientsHibernateModel> savedRecipeIngredients = createRecipeIngredientsHibernateRepositoryAdapter.saveAll(recipeIngredientsHibernateModels);
        savedRecipe.setIngredients(savedRecipeIngredients);

        Recipe recipeResponse = savedRecipe.toDomain();

        log.info("Successfully saved recipe and ingredients with ID {}", recipeResponse.getId());

        return recipeResponse;
    }

    private RecipeHibernateModel buildRecipeHibernateModel(Recipe recipe) {

        Optional<RecipeHibernateModel> existingRecipe = findRecipeByNameHibernateRepositoryAdapter.findByNameIgnoreCase(recipe.getName().trim());

        if (existingRecipe.isPresent()) {
            log.info("Recipe with name '{}' already exists with ID {}. Returning existing recipe.", recipe.getName(), existingRecipe.get().getId());
            return existingRecipe.get();
        }

        RecipeHibernateModel recipeHibernate = RecipeHibernateModel.builder()
                .name(recipe.getName())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .imageUrl(recipe.getImage())
                .preparationTime(PreparationTimeHibernateModel.fromDomain(recipe.getPreparationTime()))
                .cookLevel(CookLevelHibernateModel.fromDomain(recipe.getCookLevel()))
                .diet(recipe.getDiet() != null ? DietHibernateModel.fromDomain(recipe.getDiet()) : null)
                .mealTypes(recipe.getMealTypes().stream().map(MealTypeHibernateModel::fromDomain).toList())
                .allergies(recipe.getAllergies().stream().map(AllergyHibernateModel::fromDomain).toList())
                .dietaryNeeds(recipe.getDietaryNeeds().stream().map(DietaryNeedHibernateModel::fromDomain).toList())
                .build();

        List<RecipeStepsHibernateModel> stepsHibernateModel = recipe.getSteps().stream()
                .map(step -> buildRecipeStepHibernateModel(recipeHibernate, step))
                .toList();

        recipeHibernate.setSteps(stepsHibernateModel);

        List<RecipeIngredientsHibernateModel> recipeIngredientsToSave = recipe.getIngredients().stream()
                .map(ingredient -> buildRecipeIngredientHibernateModel(recipeHibernate, ingredient))
                .toList();

        recipeHibernate.setIngredients(recipeIngredientsToSave);

        return recipeHibernate;
    }

    private RecipeStepsHibernateModel buildRecipeStepHibernateModel(RecipeHibernateModel savedRecipe, Step step) {
        return RecipeStepsHibernateModel.builder()
                .recipe(savedRecipe)
                .number(step.getNumber())
                .title(step.getTitle())
                .description(step.getDescription())
                .imageName(step.getImageName())
                .build();
    }

    @NotNull
    private RecipeIngredientsHibernateModel buildRecipeIngredientHibernateModel(RecipeHibernateModel savedRecipe, Ingredient ingredient) {

        Optional<IngredientHibernateModel> oSavedIngredient = getIngredientByNameHibernateRepositoryAdapter.findByName(ingredient.getName());

        IngredientHibernateModel savedIngredient = oSavedIngredient.orElseGet(() ->
                createIngredientHibernateRepositoryAdapter.save(IngredientHibernateModel.fromDomain(ingredient))
        );

        return RecipeIngredientsHibernateModel.builder()
                .recipe(savedRecipe)
                .ingredient(savedIngredient)
                .quantity(ingredient.getQuantity())
                .optional(ingredient.getOptional())
                .build();
    }
}
