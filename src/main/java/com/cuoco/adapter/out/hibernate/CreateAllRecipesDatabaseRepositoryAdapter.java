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
import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateAllRecipesHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateIngredientHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.FindRecipeByNameHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetIngredientByNameHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateAllRecipesRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class CreateAllRecipesDatabaseRepositoryAdapter implements CreateAllRecipesRepository {

    private final GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter;
    private final FindRecipeByNameHibernateRepositoryAdapter findRecipeByNameHibernateRepositoryAdapter;
    private final CreateAllRecipesHibernateRepositoryAdapter createAllRecipesHibernateRepositoryAdapter;
    private final CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter;

    public CreateAllRecipesDatabaseRepositoryAdapter(
            GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter,
            FindRecipeByNameHibernateRepositoryAdapter findRecipeByNameHibernateRepositoryAdapter,
            CreateAllRecipesHibernateRepositoryAdapter createAllRecipesHibernateRepositoryAdapter,
            CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter
    ) {
        this.getIngredientByNameHibernateRepositoryAdapter = getIngredientByNameHibernateRepositoryAdapter;
        this.findRecipeByNameHibernateRepositoryAdapter = findRecipeByNameHibernateRepositoryAdapter;
        this.createAllRecipesHibernateRepositoryAdapter = createAllRecipesHibernateRepositoryAdapter;
        this.createIngredientHibernateRepositoryAdapter = createIngredientHibernateRepositoryAdapter;
    }

    @Override
    public List<Recipe> execute(List<Recipe> recipes) {
        log.info("Saving {} recipes and ingredients in database", recipes.size());

        List<RecipeHibernateModel> recipesToSave = recipes.stream().map(this::buildRecipeHibernateModel).toList();

        List<RecipeHibernateModel> savedRecipes = createAllRecipesHibernateRepositoryAdapter.saveAll(recipesToSave);

        List<Recipe> recipesResponse = savedRecipes.stream().map(RecipeHibernateModel::toDomain).toList();

        log.info("Successfully saved {} recipes and ingredients ", recipesResponse.size());

        return recipesResponse;
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
                .instructions(recipe.getInstructions())
                .preparationTime(PreparationTimeHibernateModel.builder()
                        .id(recipe.getPreparationTime().getId())
                        .description(recipe.getPreparationTime().getDescription())
                        .build())
                .cookLevel(CookLevelHibernateModel.builder()
                        .id(recipe.getCookLevel().getId())
                        .description(recipe.getCookLevel().getDescription())
                        .build()
                )
                .diet(recipe.getDiet() != null ?
                        DietHibernateModel.builder()
                                .id(recipe.getDiet().getId())
                                .description(recipe.getDiet().getDescription()).build()
                        : null
                )
                .mealTypes(recipe.getMealTypes().stream().map(this::buildMealTypeHibernateModel).toList())
                .allergies(recipe.getAllergies().stream().map(this::buildAllergiesHibernateModel).toList())
                .dietaryNeeds(recipe.getDietaryNeeds().stream().map(this::buildDietaryNeedsHibernateModel).toList())
                .build();

        List<RecipeIngredientsHibernateModel> recipeIngredientsToSave = recipe.getIngredients().stream()
                .map(ingredient -> buildRecipeIngredientHibernateModel(recipeHibernate, ingredient))
                .toList();

        recipeHibernate.setIngredients(recipeIngredientsToSave);

        return recipeHibernate;
    }

    @NotNull
    private RecipeIngredientsHibernateModel buildRecipeIngredientHibernateModel(RecipeHibernateModel savedRecipe, Ingredient ingredient) {
        Optional<IngredientHibernateModel> oSavedIngredient = getIngredientByNameHibernateRepositoryAdapter.findByName(ingredient.getName());
        IngredientHibernateModel savedIngredient = oSavedIngredient.orElseGet(() -> createIngredientHibernateRepositoryAdapter.save(buildIngredientHibernateModel(ingredient)));

        return RecipeIngredientsHibernateModel.builder()
                .recipe(savedRecipe)
                .ingredient(savedIngredient)
                .quantity(ingredient.getQuantity())
                .optional(ingredient.getOptional())
                .build();
    }

    private IngredientHibernateModel buildIngredientHibernateModel(Ingredient ingredient) {
        return IngredientHibernateModel.builder()
                .name(ingredient.getName())
                .unit(UnitHibernateModel.builder()
                        .id(ingredient.getUnit().getId())
                        .description(ingredient.getUnit().getDescription())
                        .symbol(ingredient.getUnit().getSymbol())
                        .build()
                )
                .build();
    }

    private DietaryNeedHibernateModel buildDietaryNeedsHibernateModel(DietaryNeed dietaryNeed) {
        return DietaryNeedHibernateModel.builder()
                .id(dietaryNeed.getId())
                .description(dietaryNeed.getDescription())
                .build();
    }

    private AllergyHibernateModel buildAllergiesHibernateModel(Allergy allergy) {
        return AllergyHibernateModel.builder()
                .id(allergy.getId())
                .description(allergy.getDescription())
                .build();
    }

    private MealTypeHibernateModel buildMealTypeHibernateModel(MealType mealType) {
        return MealTypeHibernateModel.builder()
                .id(mealType.getId())
                .description(mealType.getDescription())
                .build();
    }
}
