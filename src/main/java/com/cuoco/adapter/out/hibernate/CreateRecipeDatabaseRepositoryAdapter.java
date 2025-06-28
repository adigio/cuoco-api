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
import com.cuoco.adapter.out.hibernate.repository.CreateIngredientHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeIngredientsHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetIngredientByNameHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetUnitBySymbolHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
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
    private final GetUnitBySymbolHibernateRepositoryAdapter getUnitBySymbolHibernateRepositoryAdapter;

    public CreateRecipeDatabaseRepositoryAdapter(
            GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter,
            CreateRecipeHibernateRepositoryAdapter createRecipeHibernateRepositoryAdapter,
            CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter,
            CreateRecipeIngredientsHibernateRepositoryAdapter createRecipeIngredientsHibernateRepositoryAdapter,
            GetUnitBySymbolHibernateRepositoryAdapter getUnitBySymbolHibernateRepositoryAdapter
    ) {
        this.getIngredientByNameHibernateRepositoryAdapter = getIngredientByNameHibernateRepositoryAdapter;
        this.createRecipeHibernateRepositoryAdapter = createRecipeHibernateRepositoryAdapter;
        this.createIngredientHibernateRepositoryAdapter = createIngredientHibernateRepositoryAdapter;
        this.createRecipeIngredientsHibernateRepositoryAdapter = createRecipeIngredientsHibernateRepositoryAdapter;
        this.getUnitBySymbolHibernateRepositoryAdapter = getUnitBySymbolHibernateRepositoryAdapter;
    }

    @Override
    public Recipe execute(Recipe recipe) {
        log.info("Saving recipe and ingredients in database: {}", recipe);

        // Check if recipe with same name already exists (normalized comparison)
        Optional<RecipeHibernateModel> existingRecipe = createRecipeHibernateRepositoryAdapter.findByNameIgnoreCase(recipe.getName().trim());
        if (existingRecipe.isPresent()) {
            log.info("Recipe with name '{}' already exists with ID {}. Returning existing recipe.", recipe.getName(), existingRecipe.get().getId());
            return existingRecipe.get().toDomain();
        }

        RecipeHibernateModel savedRecipe = createRecipeHibernateRepositoryAdapter.save(buildRecipeHibernateModel(recipe));

        List<RecipeIngredientsHibernateModel> recipeIngredientsHibernateModels = recipe.getIngredients().stream().map(ingredient -> buildRecipeIngredientHibernateModel(savedRecipe, ingredient)).toList();
        List<RecipeIngredientsHibernateModel> savedRecipeIngredients = createRecipeIngredientsHibernateRepositoryAdapter.saveAll(recipeIngredientsHibernateModels);
        savedRecipe.setIngredients(savedRecipeIngredients);

        Recipe recipeResponse = savedRecipe.toDomain();

        log.info("Successfully saved recipe and ingredients with ID {}", recipeResponse.getId());

        return recipeResponse;
    }

    private RecipeHibernateModel buildRecipeHibernateModel(Recipe recipe) {
        return RecipeHibernateModel.builder()
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
