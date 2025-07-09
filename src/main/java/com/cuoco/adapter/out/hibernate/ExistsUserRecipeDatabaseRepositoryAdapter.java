package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.ExistsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.ExistsUserRecipeByUserIdAndRecipeIdRepository;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.stereotype.Repository;

@Repository
public class ExistsUserRecipeDatabaseRepositoryAdapter implements ExistsUserRecipeByUserIdAndRecipeIdRepository {

    private final ExistsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter existsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter;

    public ExistsUserRecipeDatabaseRepositoryAdapter(
            ExistsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter existsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter
    ) {
        this.existsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter = existsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter;
    }

    @Override
    public boolean execute(UserRecipe userRecipe) {
        return existsUserRecipeByUserIdAndRecipeIdHibernateRepositoryAdapter.existsByUserIdAndRecipeId(userRecipe.getUser().getId(), userRecipe.getRecipe().getId());
    }

}
