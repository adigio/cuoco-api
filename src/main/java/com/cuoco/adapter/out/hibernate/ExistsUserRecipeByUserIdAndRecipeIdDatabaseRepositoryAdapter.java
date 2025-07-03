package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.ExistsUserRecipeByUserIdAndRecipeIdRepository;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.stereotype.Repository;

@Repository
public class ExistsUserRecipeByUserIdAndRecipeIdDatabaseRepositoryAdapter implements ExistsUserRecipeByUserIdAndRecipeIdRepository {

    private final UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter;

    public ExistsUserRecipeByUserIdAndRecipeIdDatabaseRepositoryAdapter(
            UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter
    ) {
        this.userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter = userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter;
    }

    @Override
    public boolean execute(UserRecipe userRecipe) {
        return userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter.existsByUserIdAndRecipeId(userRecipe.getUser().getId(), userRecipe.getRecipe().getId());
    }

}
