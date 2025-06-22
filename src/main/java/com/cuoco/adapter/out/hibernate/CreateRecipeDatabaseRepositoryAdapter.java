package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealCategoryHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeMealCategoriesHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateIngredientHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeIngredientsHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeMealCategoriesHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetIngredientByNameHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetUnitBySymbolHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.MealCategory;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeMealCategory;
import com.cuoco.shared.model.ErrorDescription;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
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
    private final CreateRecipeMealCategoriesHibernateRepositoryAdapter createRecipeMealCategoriesHibernateRepositoryAdapter;

    public CreateRecipeDatabaseRepositoryAdapter(
            GetIngredientByNameHibernateRepositoryAdapter getIngredientByNameHibernateRepositoryAdapter,
            CreateRecipeHibernateRepositoryAdapter createRecipeHibernateRepositoryAdapter,
            CreateIngredientHibernateRepositoryAdapter createIngredientHibernateRepositoryAdapter,
            CreateRecipeIngredientsHibernateRepositoryAdapter createRecipeIngredientsHibernateRepositoryAdapter,
            GetUnitBySymbolHibernateRepositoryAdapter getUnitBySymbolHibernateRepositoryAdapter,
            CreateRecipeMealCategoriesHibernateRepositoryAdapter createRecipeMealCategoriesHibernateRepositoryAdapter
    ) {
        this.getIngredientByNameHibernateRepositoryAdapter = getIngredientByNameHibernateRepositoryAdapter;
        this.createRecipeHibernateRepositoryAdapter = createRecipeHibernateRepositoryAdapter;
        this.createIngredientHibernateRepositoryAdapter = createIngredientHibernateRepositoryAdapter;
        this.createRecipeIngredientsHibernateRepositoryAdapter = createRecipeIngredientsHibernateRepositoryAdapter;
        this.getUnitBySymbolHibernateRepositoryAdapter = getUnitBySymbolHibernateRepositoryAdapter;
        this.createRecipeMealCategoriesHibernateRepositoryAdapter = createRecipeMealCategoriesHibernateRepositoryAdapter;
    }

    @Override
    public Recipe execute(Recipe recipe) {
        log.info("Saving recipe and ingredients in database: {}", recipe);

        RecipeHibernateModel savedRecipe = createRecipeHibernateRepositoryAdapter.save(buildRecipeHibernateModel(recipe));

        List<RecipeIngredientsHibernateModel> recipeIngredientsHibernateModels = recipe.getIngredients().stream().map(ingredient -> buildRecipeIngredientHibernateModel(savedRecipe, ingredient)).toList();
        List<RecipeIngredientsHibernateModel> savedRecipeIngredients = createRecipeIngredientsHibernateRepositoryAdapter.saveAll(recipeIngredientsHibernateModels);
        savedRecipe.setIngredients(savedRecipeIngredients);

        List<RecipeMealCategoriesHibernateModel> recipeMealCategoriesHibernateModels = recipe.getCategories().stream().map(category -> buildRecipeMealCategoriesHibernateModel(savedRecipe, category)).toList();
        List<RecipeMealCategoriesHibernateModel> savedRecipeCategories = createRecipeMealCategoriesHibernateRepositoryAdapter.saveAll(recipeMealCategoriesHibernateModels);
        savedRecipe.setCategories(savedRecipeCategories);

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
                .type(MealTypeHibernateModel.builder()
                        .id(recipe.getType().getId())
                        .description(recipe.getType().getDescription())
                        .build())
                .cookLevel(CookLevelHibernateModel.builder()
                        .id(recipe.getCookLevel().getId())
                        .description(recipe.getCookLevel().getDescription())
                        .build()
                )
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
        Optional<UnitHibernateModel> unitHibernateModel = getUnitBySymbolHibernateRepositoryAdapter.findBySymbolEqualsIgnoreCase(ingredient.getUnit().getSymbol());

        if (unitHibernateModel.isEmpty()) {
            log.warn("No unit found for symbol: {}", ingredient.getUnit());
            throw new UnprocessableException(ErrorDescription.UNEXPECTED_ERROR.getValue());
        }

        return IngredientHibernateModel.builder()
                .name(ingredient.getName())
                .unit(unitHibernateModel.get())
                .build();
    }

    private RecipeMealCategoriesHibernateModel buildRecipeMealCategoriesHibernateModel(RecipeHibernateModel savedRecipe, MealCategory category) {
        MealCategoryHibernateModel categoryHibernateModel = MealCategoryHibernateModel.builder().id(category.getId()).description(category.getDescription()).build();

        return RecipeMealCategoriesHibernateModel.builder()
                .recipe(savedRecipe)
                .category(categoryHibernateModel)
                .build();
    }
}
