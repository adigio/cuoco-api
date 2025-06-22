package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.SaveUserRecipeHibernateRepositoryAdapter;
import com.cuoco.application.port.out.FavRecipeRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.stereotype.Repository;

@Repository
public class FavRecipeRepositoryAdapter implements FavRecipeRepository {
    private final SaveUserRecipeHibernateRepositoryAdapter saveUserRecipeHibernateRepositoryAdapter;

    public FavRecipeRepositoryAdapter(SaveUserRecipeHibernateRepositoryAdapter saveUserRecipeHibernateRepositoryAdapter) {
        this.saveUserRecipeHibernateRepositoryAdapter = saveUserRecipeHibernateRepositoryAdapter;
    }

    @Override
    public Boolean execute(UserRecipe userRecipe) {
        saveUserRecipeHibernateRepositoryAdapter.save(buildUserRecipeModel(userRecipe));
        return null;
    }

    private UserRecipesHibernateModel buildUserRecipeModel(UserRecipe userRecipe) {

        return UserRecipesHibernateModel.builder().
                user(buildUserHibernateModel(userRecipe.getUser()))
                .recipe(buildUserRecipeHibernateModel(userRecipe.getRecipe()))
                .favorite(true)
                .build();
    }

    private RecipeHibernateModel buildUserRecipeHibernateModel(Recipe recipe) {
        return RecipeHibernateModel.builder()
                .id(recipe.getId())
                .build();
    }


    private UserHibernateModel buildUserHibernateModel(User user) {
        return UserHibernateModel.builder()
                .id(user.getId())
                .build();

    }
}
