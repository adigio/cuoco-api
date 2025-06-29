package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllRecipesByIdsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllRecipesByIdsRepository;
import com.cuoco.application.usecase.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllRecipesByIdsDatabaseRepositoryAdapter implements GetAllRecipesByIdsRepository {

    private final GetAllRecipesByIdsHibernateRepositoryAdapter getAllRecipesByIdsHibernateRepositoryAdapter;

    public GetAllRecipesByIdsDatabaseRepositoryAdapter(GetAllRecipesByIdsHibernateRepositoryAdapter getAllRecipesByIdsHibernateRepositoryAdapter) {
        this.getAllRecipesByIdsHibernateRepositoryAdapter = getAllRecipesByIdsHibernateRepositoryAdapter;
    }

    @Override
    public List<Recipe> execute(List<Long> ids) {
        log.info("Get all recipes by ids: {}", ids);

        List<RecipeHibernateModel> recipes = getAllRecipesByIdsHibernateRepositoryAdapter.findAllById(ids);
        return recipes.stream().map(RecipeHibernateModel::toDomain).toList();
    }
}
