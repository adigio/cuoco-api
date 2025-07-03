package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealPrepIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealPrepStepsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeStepsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateAllMealPrepsHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateIngredientHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetIngredientByNameHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateAllMealPrepsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CreateAllMealPrepsDatabaseRepository implements CreateAllMealPrepsRepository {

    private final CreateAllMealPrepsHibernateRepositoryAdapter createAllMealPrepsHibernateRepositoryAdapter;

    private final GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter;
    private final CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter;

    public CreateAllMealPrepsDatabaseRepository(
            CreateAllMealPrepsHibernateRepositoryAdapter createAllMealPrepsHibernateRepositoryAdapter,
            GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter,
            CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter
    ) {
        this.createAllMealPrepsHibernateRepositoryAdapter = createAllMealPrepsHibernateRepositoryAdapter;
        this.getIngredientByNameHibernateRepositoryAdapter = getIngredientByNameHibernateRepositoryAdapter;
        this.createIngredientHibernateRepositoryAdapter = createIngredientHibernateRepositoryAdapter;
    }

    @Override
    public List<MealPrep> execute(List<MealPrep> mealPreps) {
        log.info("Saving {} meal preps in database", mealPreps.size());

        List<MealPrepHibernateModel> mealPrepsToSave = mealPreps.stream().map(this::buildMealPrepHibernateModel).toList();

        List<MealPrepHibernateModel> savedMealPreps = createAllMealPrepsHibernateRepositoryAdapter.saveAll(mealPrepsToSave);

        List<MealPrep> mealPrepsResponse = savedMealPreps.stream().map(MealPrepHibernateModel::toDomain).toList();

        log.info("Successfully saved {} meal preps ", mealPrepsResponse.size());

        return mealPrepsResponse;
    }

    private MealPrepHibernateModel buildMealPrepHibernateModel(MealPrep mealPrep) {
        MealPrepHibernateModel mealPrepToSave = MealPrepHibernateModel.builder()
                .title(mealPrep.getTitle())
                .estimatedCookingTime(mealPrep.getEstimatedCookingTime())
                .servings(mealPrep.getServings())
                .freeze(mealPrep.getFreeze())
                .recipes(mealPrep.getRecipes().stream().map(this::buildRecipeHibernateModel).toList())
                .build();

        List<MealPrepStepsHibernateModel> stepsToSave = mealPrep.getSteps().stream()
                .map(step -> buildMealPrepStepHibernateModel(mealPrepToSave, step))
                .toList();

        mealPrepToSave.setSteps(stepsToSave);

        List<MealPrepIngredientsHibernateModel> ingredientsToSave = mealPrep.getIngredients().stream()
                .map(ingredient -> buildMealPrepIngredientsHibernateModel(mealPrepToSave, ingredient))
                .toList();

        mealPrepToSave.setIngredients(ingredientsToSave);

        return mealPrepToSave;
    }

    private MealPrepStepsHibernateModel buildMealPrepStepHibernateModel(MealPrepHibernateModel mealPrep, Step step) {
        return MealPrepStepsHibernateModel.builder()
                .mealPrep(mealPrep)
                .title(step.getTitle())
                .number(step.getNumber())
                .description(step.getDescription())
                .time(step.getTime())
                .imageName(step.getImageName())
                .build();
    }

    private MealPrepIngredientsHibernateModel buildMealPrepIngredientsHibernateModel(MealPrepHibernateModel mealPrep, Ingredient ingredient) {
        Optional<IngredientHibernateModel> oSavedIngredient = getIngredientByNameHibernateRepositoryAdapter.findByName(ingredient.getName());
        IngredientHibernateModel savedIngredient = oSavedIngredient.orElseGet(() ->
                createIngredientHibernateRepositoryAdapter.save(IngredientHibernateModel.fromDomain(ingredient))
        );

        return MealPrepIngredientsHibernateModel.builder()
                .mealPrep(mealPrep)
                .ingredient(savedIngredient)
                .quantity(ingredient.getQuantity())
                .build();
    }

    private RecipeHibernateModel buildRecipeHibernateModel(Recipe recipe) {
        RecipeHibernateModel recipeHibernate = RecipeHibernateModel.builder()
                .id(recipe.getId())
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
        return RecipeIngredientsHibernateModel.builder()
                .recipe(savedRecipe)
                .ingredient(IngredientHibernateModel.fromDomain(ingredient))
                .quantity(ingredient.getQuantity())
                .optional(ingredient.getOptional())
                .build();
    }
}
