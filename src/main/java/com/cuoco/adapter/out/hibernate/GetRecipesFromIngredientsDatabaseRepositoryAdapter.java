package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@Qualifier("repository")
public class GetRecipesFromIngredientsDatabaseRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    private final GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter;

    public GetRecipesFromIngredientsDatabaseRepositoryAdapter(
            GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter
    ) {
        this.getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter = getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter;
    }

    @Override
    public List<Recipe> execute(Recipe recipe) {
        try {
            List<String> ingredientNames = recipe.getIngredients().stream().map(i -> i.getName().toLowerCase()).toList();
            log.info("Getting recipes by ingredients {} and filters from database", ingredientNames);

            Integer cookLevelId = null;
            String maxPreparationTime = null;

            if (recipe.getFilters().getEnable()) {
                cookLevelId = recipe.getFilters().getDifficulty() != null ? recipe.getFilters().getDifficulty().getId() : null;
                maxPreparationTime = recipe.getFilters().getTime();
            }

            List<RecipeHibernateModel> savedRecipes = getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter.execute(
                    ingredientNames,
                    cookLevelId,
                    maxPreparationTime
            );

            if(savedRecipes.isEmpty()) {
                log.info("No recipes found in database with the provided ingredients and filters");
                return Collections.emptyList();
            }

            List<Recipe> recipesResponse = savedRecipes.stream().map(RecipeHibernateModel::toDomain).toList();

            log.info("Successfully retrieved {} recipes from ingredients and filters", recipesResponse.size());
            return recipesResponse;
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            log.error(ErrorDescription.UNEXPECTED_ERROR.getValue(), e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }
}
