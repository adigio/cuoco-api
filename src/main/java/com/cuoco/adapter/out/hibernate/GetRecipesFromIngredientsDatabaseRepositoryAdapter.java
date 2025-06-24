package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetRecipesIdsByIngredientsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@Qualifier("repository")
public class GetRecipesFromIngredientsDatabaseRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    private final GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter;
    private final GetRecipesIdsByIngredientsHibernateRepositoryAdapter getRecipesIdsByIngredientsHibernateRepositoryAdapter;

    public GetRecipesFromIngredientsDatabaseRepositoryAdapter(
            GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter,
            GetRecipesIdsByIngredientsHibernateRepositoryAdapter getRecipesIdsByIngredientsHibernateRepositoryAdapter
    ) {
        this.getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter = getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter;
        this.getRecipesIdsByIngredientsHibernateRepositoryAdapter = getRecipesIdsByIngredientsHibernateRepositoryAdapter;
    }

    @Override
    public List<Recipe> execute(Recipe recipe) {
        try {
            List<String> ingredientNames = recipe.getIngredients().stream().map(i -> i.getName().toLowerCase()).toList();
            log.info("Getting recipes by ingredients {} and filters from database", ingredientNames);

            Integer ingredientCount = ingredientNames.size();

            Integer preparationTimeId = null;
            Integer cookLevelId = null;
            Integer dietId = null;
            List<Integer> mealTypesIds = null;
            List<Integer> allergiesIds = null;
            List<Integer> dietaryNeedsIds = null;

            if (recipe.getFilters().getEnable()) {
                RecipeFilter filters = recipe.getFilters();

                if(filters.getPreparationTime() != null) {
                    preparationTimeId = filters.getPreparationTime().getId();
                }

                if(filters.getDiet() != null) {
                    dietId = filters.getDiet().getId();
                }

                if(filters.getCookLevel() != null) {
                    cookLevelId = filters.getCookLevel().getId();
                }

                if(!filters.getTypes().isEmpty()) {
                    mealTypesIds = filters.getTypes().stream().map(MealType::getId).toList();
                }

                if(!filters.getAllergies().isEmpty()) {
                    allergiesIds = filters.getAllergies().stream().map(Allergy::getId).toList();
                }

                if(!filters.getDietaryNeeds().isEmpty()) {
                    dietaryNeedsIds = filters.getDietaryNeeds().stream().map(DietaryNeed::getId).toList();
                }
            }

            List<Long> recipesIds = getRecipesIdsByIngredientsHibernateRepositoryAdapter.execute(ingredientNames, ingredientCount);

            List<RecipeHibernateModel> savedRecipes = getRecipesByIngredientsAndFiltersHibernateRepositoryAdapter.execute(
                    recipesIds,
                    preparationTimeId,
                    cookLevelId,
                    dietId,
                    mealTypesIds,
                    allergiesIds,
                    dietaryNeedsIds,
                    PageRequest.of(0, recipe.getConfiguration().getSize())
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
