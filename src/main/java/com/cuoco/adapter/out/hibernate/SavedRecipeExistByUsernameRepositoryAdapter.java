package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.ExistUserRecipesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.SavedRecipeExistByUsernameRepository;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.stereotype.Repository;

@Repository
public class SavedRecipeExistByUsernameRepositoryAdapter implements SavedRecipeExistByUsernameRepository {

    private final ExistUserRecipesHibernateRepositoryAdapter existUserRecipesHibernateRepositoryAdapter;

    public SavedRecipeExistByUsernameRepositoryAdapter(ExistUserRecipesHibernateRepositoryAdapter existUserRecipesHibernateRepositoryAdapter) {
        this.existUserRecipesHibernateRepositoryAdapter = existUserRecipesHibernateRepositoryAdapter;
    }

    @Override
    public boolean execute(UserRecipe userRecipe) {
        return existUserRecipesHibernateRepositoryAdapter.existsByUserIdAndRecipeId(userRecipe.getUser().getId(), userRecipe.getRecipe().getId());
    }

}
