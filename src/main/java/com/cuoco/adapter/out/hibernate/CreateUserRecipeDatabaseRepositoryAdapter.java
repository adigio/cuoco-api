package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateUserRecipeHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserRecipeRepository;
import com.cuoco.application.usecase.model.UserRecipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CreateUserRecipeDatabaseRepositoryAdapter implements CreateUserRecipeRepository {

    private final CreateUserRecipeHibernateRepositoryAdapter createUserRecipeHibernateRepositoryAdapter;

    public CreateUserRecipeDatabaseRepositoryAdapter(CreateUserRecipeHibernateRepositoryAdapter createUserRecipeHibernateRepositoryAdapter) {
        this.createUserRecipeHibernateRepositoryAdapter = createUserRecipeHibernateRepositoryAdapter;
    }

    @Override
    public void execute(UserRecipe userRecipe) {
        log.info("Executing create user recipe in database with body {}", userRecipe);
        createUserRecipeHibernateRepositoryAdapter.save(buildUserRecipeModel(userRecipe));
    }

    private UserRecipesHibernateModel buildUserRecipeModel(UserRecipe userRecipe) {
        return UserRecipesHibernateModel
                .builder()
                .user(UserHibernateModel.builder().id(userRecipe.getUser().getId()).build())
                .recipe(RecipeHibernateModel.builder().id(userRecipe.getRecipe().getId()).build())
                .build();
    }
}
