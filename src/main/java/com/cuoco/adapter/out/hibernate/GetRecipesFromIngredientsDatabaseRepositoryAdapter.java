package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetRecipeIngredientsByRecipeIdHibernateRepository;
import com.cuoco.adapter.out.hibernate.utils.Constants;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLSyntaxErrorException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@Qualifier("repository")
public class GetRecipesFromIngredientsDatabaseRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    private GetRecipeIngredientsByRecipeIdHibernateRepository getRecipeIngredientsByRecipeIdHibernateRepository;

    private String findByIngredientAndFilters = FileReader.execute("sql/getRecipesFromIngredientAndFilters.sql");

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GetRecipesFromIngredientsDatabaseRepositoryAdapter(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> execute(Recipe recipe) {
        try {
            List<String> ingredientNames = recipe.getIngredients().stream().map(i -> i.getName().toLowerCase()).toList();

            log.info("Getting recipes by ingredients {} and filters from database", recipe.getIngredients());

            Map<String, Object> params = new HashMap<>();

            params.put(Constants.INGREDIENT_NAMES.getValue(), ingredientNames);
            params.put(Constants.INGREDIENT_COUNT.getValue(), ingredientNames.size());
            params.put(Constants.MAX_RECIPES.getValue(), recipe.getFilters().getMaxRecipes());

            if (recipe.getFilters().getEnable()) {
                params.put(Constants.COOK_LEVEL_ID.getValue(), recipe.getFilters().getDifficulty() != null ? recipe.getFilters().getDifficulty().getId() : null);
                params.put(Constants.MAX_PREPARATION_TIME.getValue(), recipe.getFilters().getTime());
            } else {
                params.put(Constants.COOK_LEVEL_ID.getValue(), null);
                params.put(Constants.MAX_PREPARATION_TIME.getValue(), null);
            }

            List<RecipeHibernateModel> savedRecipes = jdbcTemplate.query(
                    findByIngredientAndFilters,
                    params,
                    new BeanPropertyRowMapper<>(RecipeHibernateModel.class)
            );

            if(savedRecipes.isEmpty()) {
                log.info("No recipes found in database with the provided ingredients and filters");
                return Collections.emptyList();
            }

            List<Recipe> recipesResponse = savedRecipes.stream().map(RecipeHibernateModel::toDomain).toList();

            for (Recipe recipeResponse : recipesResponse) {
                List<RecipeIngredientsHibernateModel> recipeIngredients = getRecipeIngredientsByRecipeIdHibernateRepository.findByRecipeId(recipeResponse.getId());
                List<Ingredient> ingredients = recipeIngredients.stream().map(this::buildIngredientResponse).toList();

                recipeResponse.setIngredients(ingredients);
            }

            log.info("Successfully retrieved {} recipes from ingredients and filters", recipesResponse.size());
            return List.of();
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            log.error(ErrorDescription.UNEXPECTED_ERROR.getValue(), e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
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

}
