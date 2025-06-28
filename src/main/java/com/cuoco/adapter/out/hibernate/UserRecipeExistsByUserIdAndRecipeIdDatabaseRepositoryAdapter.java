package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.UserRecipeExistsByUserIdAndRecipeIdRepository;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.stereotype.Repository;

@Repository
public class UserRecipeExistsByUserIdAndRecipeIdDatabaseRepositoryAdapter implements UserRecipeExistsByUserIdAndRecipeIdRepository {

    private final UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter;

    public UserRecipeExistsByUserIdAndRecipeIdDatabaseRepositoryAdapter(
            UserRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter
    ) {
        this.userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter = userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter;
    }

    @Override
    public boolean execute(UserRecipe userRecipe) {
        return userRecipeExistsByUserIdAndRecipeIdHibernateRepositoryAdapter.existsByUserIdAndRecipeId(userRecipe.getUser().getId(), userRecipe.getRecipe().getId());
    }

}
