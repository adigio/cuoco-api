package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetRecipeByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetRecipeByIdDatabaseRepositoryAdapter implements GetRecipeByIdRepository {

    private final GetRecipeByIdHibernateRepositoryAdapter getRecipeByIdHibernateRepositoryAdapter;

    public GetRecipeByIdDatabaseRepositoryAdapter(GetRecipeByIdHibernateRepositoryAdapter getRecipeByIdHibernateRepositoryAdapter) {
        this.getRecipeByIdHibernateRepositoryAdapter = getRecipeByIdHibernateRepositoryAdapter;
    }

    @Override
    public Recipe execute(Long id) {
        Optional<RecipeHibernateModel> recipe = getRecipeByIdHibernateRepositoryAdapter.findById(id);

        if (recipe.isPresent()) {
            return recipe.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.RECIPE_NOT_EXISTS.getValue());
        }
    }
}
