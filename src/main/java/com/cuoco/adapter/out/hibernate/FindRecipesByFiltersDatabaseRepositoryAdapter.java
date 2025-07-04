package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.FindRecipesByFiltersHibernateRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.repository.GetRecipesMaxIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.FindRecipesByFiltersRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.SearchFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
@Repository
public class FindRecipesByFiltersDatabaseRepositoryAdapter implements FindRecipesByFiltersRepository {

    private final GetRecipesMaxIdHibernateRepositoryAdapter getRecipesMaxIdHibernateRepositoryAdapter;
    private final FindRecipesByFiltersHibernateRepositoryAdapter findRecipesByFiltersHibernateRepositoryAdapter;

    public FindRecipesByFiltersDatabaseRepositoryAdapter(
            GetRecipesMaxIdHibernateRepositoryAdapter getRecipesMaxIdHibernateRepositoryAdapter,
            FindRecipesByFiltersHibernateRepositoryAdapter findRecipesByFiltersHibernateRepositoryAdapter
    ) {
        this.getRecipesMaxIdHibernateRepositoryAdapter = getRecipesMaxIdHibernateRepositoryAdapter;
        this.findRecipesByFiltersHibernateRepositoryAdapter = findRecipesByFiltersHibernateRepositoryAdapter;
    }

    @Override
    public List<Recipe> execute(SearchFilters filters) {
        log.info("Executing find recipes in database by filters: {}", filters);

        List<Long> ids = filters.getRandom() ? generateRandomIds(filters.getSize()) : List.of(-1L);

        List<RecipeHibernateModel> savedRecipes = findRecipesByFiltersHibernateRepositoryAdapter.execute(ids);

        List<Recipe> recipesResponse = savedRecipes.stream()
                .map(RecipeHibernateModel::toDomain)
                .toList();

        log.info("Successfully retrieved {} recipes from filters", recipesResponse.size());
        return recipesResponse;
    }

    private List<Long> generateRandomIds(Integer size) {
        Long maxId = getRecipesMaxIdHibernateRepositoryAdapter.execute();
        Set<Long> ids = new HashSet<>();
        Random random = new Random();

        while (ids.size() < size) {
            long id = 1 + random.nextLong(maxId);
            ids.add(id);
        }

        return new ArrayList<>(ids);
    }
}
