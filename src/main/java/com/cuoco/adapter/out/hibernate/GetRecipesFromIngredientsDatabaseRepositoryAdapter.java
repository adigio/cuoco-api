package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllRecipesByIdsHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.utils.Constants;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Filters;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.FileReader;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Repository
@Qualifier("repository")
public class GetRecipesFromIngredientsDatabaseRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    private final String getRecipeIdsByFilters = FileReader.execute("sql/getRecipeIdsByFilters.sql");

    private final GetAllRecipesByIdsHibernateRepositoryAdapter getAllRecipesByIdsHibernateRepositoryAdapter;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GetRecipesFromIngredientsDatabaseRepositoryAdapter(
            GetAllRecipesByIdsHibernateRepositoryAdapter getAllRecipesByIdsHibernateRepositoryAdapter,
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.getAllRecipesByIdsHibernateRepositoryAdapter = getAllRecipesByIdsHibernateRepositoryAdapter;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> execute(Recipe recipe) {
        try {
            List<String> ingredientNames = recipe.getIngredients().stream()
                    .map(i -> i.getName().toLowerCase())
                    .toList();

            log.info("Getting recipes by ingredients {} and filters from database", ingredientNames);

            Integer ingredientCount = ingredientNames.size();
            Filters filters = recipe.getFilters();

            List<Long> notIncludeIdsRaw = recipe.getConfiguration().getNotInclude()
                    .stream()
                    .map(Recipe::getId)
                    .toList();

            boolean notIncludeIdsIsEmpty = notIncludeIdsRaw.isEmpty();
            List<Long> safeNotIncludeIds = notIncludeIdsIsEmpty ? List.of(-1L) : notIncludeIdsRaw;

            Integer preparationTimeId = Optional.ofNullable(filters.getPreparationTime()).map(PreparationTime::getId).orElse(null);
            Integer cookLevelId       = Optional.ofNullable(filters.getCookLevel()).map(CookLevel::getId).orElse(null);
            Integer dietId            = Optional.ofNullable(filters.getDiet()).map(Diet::getId).orElse(null);

            List<Integer> mealTypesIdsRaw = Optional.ofNullable(filters.getMealTypes()).orElse(List.of()).stream().map(MealType::getId).toList();
            boolean mealTypesIdsIsEmpty = mealTypesIdsRaw.isEmpty();

            List<Integer> allergiesIdsRaw = Optional.ofNullable(filters.getAllergies()).orElse(List.of()).stream().map(Allergy::getId).toList();
            boolean allergiesIdsIsEmpty = allergiesIdsRaw.isEmpty();

            List<Integer> dietaryNeedsIdsRaw = Optional.ofNullable(filters.getDietaryNeeds()).orElse(List.of()).stream().map(DietaryNeed::getId).toList();
            boolean dietaryNeedsIdsIsEmpty = dietaryNeedsIdsRaw.isEmpty();

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue(Constants.RESULT_SIZE.getValue(), recipe.getConfiguration().getSize())
                    .addValue(Constants.INGREDIENT_NAMES.getValue(), ingredientNames)
                    .addValue(Constants.INGREDIENT_COUNT.getValue(), ingredientCount)
                    .addValue(Constants.PREPARATION_TIME_ID.getValue(), preparationTimeId)
                    .addValue(Constants.COOK_LEVEL_ID.getValue(), cookLevelId)
                    .addValue(Constants.DIET_ID.getValue(), dietId)
                    .addValue(Constants.MEAL_TYPES_IDS_IS_EMPTY.getValue(), mealTypesIdsIsEmpty)
                    .addValue(Constants.MEAL_TYPES_IDS.getValue(), mealTypesIdsRaw)
                    .addValue(Constants.ALLERGY_IDS_IS_EMPTY.getValue(), allergiesIdsIsEmpty)
                    .addValue(Constants.ALLERGY_IDS.getValue(), allergiesIdsRaw)
                    .addValue(Constants.DIETARY_NEEDS_IDS_IS_EMPTY.getValue(), dietaryNeedsIdsIsEmpty)
                    .addValue(Constants.DIETARY_NEEDS_IDS.getValue(), dietaryNeedsIdsRaw)
                    .addValue(Constants.DIETARY_NEEDS_COUNT.getValue(), dietaryNeedsIdsRaw.size())
                    .addValue(Constants.NOT_INCLUDE_IDS_IS_EMPTY.getValue(), notIncludeIdsIsEmpty)
                    .addValue(Constants.NOT_INCLUDE_IDS.getValue(), safeNotIncludeIds);

            List<Long> recipeIds = jdbcTemplate.query(
                    getRecipeIdsByFilters,
                    params,
                    (rs, rowNum) -> rs.getLong("id")
            );

            if (recipeIds.isEmpty()) {
                log.info("No recipes found in database with the provided ingredients and filters");
                return Collections.emptyList();
            }

            List<RecipeHibernateModel> savedRecipes = getAllRecipesByIdsHibernateRepositoryAdapter.findAllById(recipeIds);

            List<Recipe> recipesResponse = savedRecipes.stream()
                    .map(RecipeHibernateModel::toDomain)
                    .toList();

            log.info("Successfully retrieved {} recipes from ingredients and filters", recipesResponse.size());
            return recipesResponse;

        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            log.error(ErrorDescription.UNEXPECTED_ERROR.getValue(), e);
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }
}
