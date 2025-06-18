package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateIngredientHibernateRepository;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeHibernateRepository;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeIngredientsHibernateRepository;
import com.cuoco.adapter.out.hibernate.repository.FindIngredientByNameHibernateRepository;
import com.cuoco.adapter.out.hibernate.repository.GetUnitBySymbolHibernateRepository;
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

    private final FindIngredientByNameHibernateRepository findIngredientByNameHibernateRepository;
    private final CreateRecipeHibernateRepository createRecipeHibernateRepository;
    private final CreateIngredientHibernateRepository createIngredientHibernateRepository;
    private final CreateRecipeIngredientsHibernateRepository createRecipeIngredientsHibernateRepository;
    private final GetUnitBySymbolHibernateRepository getUnitBySymbolHibernateRepository;

    public CreateRecipeDatabaseRepositoryAdapter(
            FindIngredientByNameHibernateRepository findIngredientByNameHibernateRepository,
            CreateRecipeHibernateRepository createRecipeHibernateRepository,
            CreateIngredientHibernateRepository createIngredientHibernateRepository,
            CreateRecipeIngredientsHibernateRepository createRecipeIngredientsHibernateRepository,
            GetUnitBySymbolHibernateRepository getUnitBySymbolHibernateRepository
    ) {
        this.findIngredientByNameHibernateRepository = findIngredientByNameHibernateRepository;
        this.createRecipeHibernateRepository = createRecipeHibernateRepository;
        this.createIngredientHibernateRepository = createIngredientHibernateRepository;
        this.createRecipeIngredientsHibernateRepository = createRecipeIngredientsHibernateRepository;
        this.getUnitBySymbolHibernateRepository = getUnitBySymbolHibernateRepository;
    }

    @Override
    public Recipe execute(Recipe recipe) {
        log.info("Saving recipe and ingredients in database: {}", recipe);

        RecipeHibernateModel savedRecipe = createRecipeHibernateRepository.save(buildRecipeHibernateModel(recipe));

        List<RecipeIngredientsHibernateModel> recipeIngredientsHibernateModel = recipe.getIngredients().stream().map(ingredient -> buildRecipeIngredientHibernateModel(savedRecipe, ingredient)).toList();

        recipeIngredientsHibernateModel = createRecipeIngredientsHibernateRepository.saveAll(recipeIngredientsHibernateModel);

        Recipe recipeResponse = savedRecipe.toDomain();
        List<Ingredient> recipeIngredientsResponse = recipeIngredientsHibernateModel.stream().map(this::buildIngredientResponse).toList();
        recipeResponse.setIngredients(recipeIngredientsResponse);

        log.info("Successfully saved recipe and ingredients with ID {}", recipeResponse.getId());

        return recipeResponse;
    }

    private Ingredient buildIngredientResponse(RecipeIngredientsHibernateModel recipeIngredientsHibernateModel) {
        IngredientHibernateModel ingredient = recipeIngredientsHibernateModel.getIngredient();

        return Ingredient.builder()
                .name(ingredient.getName())
                .quantity(recipeIngredientsHibernateModel.getQuantity())
                .unit(ingredient.getUnit().getDescription())
                .optional(recipeIngredientsHibernateModel.getOptional())
                .build();
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
        Optional<IngredientHibernateModel> oSavedIngredient = findIngredientByNameHibernateRepository.findByName(ingredient.getName());
        IngredientHibernateModel savedIngredient = oSavedIngredient.orElseGet(() -> createIngredientHibernateRepository.save(buildIngredientHibernateModel(ingredient)));

        return RecipeIngredientsHibernateModel.builder()
                .recipe(savedRecipe)
                .ingredient(savedIngredient)
                .quantity(ingredient.getQuantity())
                .optional(ingredient.getOptional())
                .build();
    }

    private IngredientHibernateModel buildIngredientHibernateModel(Ingredient ingredient) {
        Optional<UnitHibernateModel> unitHibernateModel = getUnitBySymbolHibernateRepository.findBySymbolEqualsIgnoreCase(ingredient.getUnit());

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
