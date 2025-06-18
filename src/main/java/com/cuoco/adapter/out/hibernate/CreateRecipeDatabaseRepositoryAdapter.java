package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateIngredientHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeIngredientsHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetIngredientByNameHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetUnitBySymbolHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.model.ErrorDescription;
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

        RecipeHibernateModel savedRecipe = createRecipeHibernateRepositoryAdapter.save(buildRecipeHibernateModel(recipe));

        List<RecipeIngredientsHibernateModel> recipeIngredientsHibernateModel = recipe.getIngredients().stream().map(ingredient -> buildRecipeIngredientHibernateModel(savedRecipe, ingredient)).toList();
        List<RecipeIngredientsHibernateModel> savedRecipeIngredients = createRecipeIngredientsHibernateRepositoryAdapter.saveAll(recipeIngredientsHibernateModel);

        Recipe recipeResponse = savedRecipe.toDomain();
        List<Ingredient> recipeIngredientsResponse = savedRecipeIngredients.stream().map(recipeIngredient -> recipeIngredient.getIngredient().toDomain()).toList();
        recipeResponse.setIngredients(recipeIngredientsResponse);

        log.info("Successfully saved recipe and ingredients with ID {}", recipeResponse.getId());

        return recipeResponse;
    }

    private RecipeHibernateModel buildRecipeHibernateModel(Recipe recipe) {
        return RecipeHibernateModel.builder()
                .name(recipe.getName())
                .imageUrl(recipe.getImage())
                .subtitle(recipe.getSubtitle())
                .description(recipe.getDescription())
                .instructions(recipe.getInstructions())
                .preparationTime(recipe.getPreparationTime())
                .cookLevel(
                        CookLevelHibernateModel.builder()
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
}
