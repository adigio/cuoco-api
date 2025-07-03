package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllUserRecipesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllUserRecipesRepository;
import com.cuoco.application.usecase.model.UserRecipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllUserRecipesDatabaseRepositoryAdapter implements GetAllUserRecipesRepository {

    private final GetAllUserRecipesHibernateRepositoryAdapter getAllUserRecipesHibernateRepositoryAdapter;

    public GetAllUserRecipesDatabaseRepositoryAdapter(GetAllUserRecipesHibernateRepositoryAdapter getAllUserRecipesHibernateRepositoryAdapter) {
        this.getAllUserRecipesHibernateRepositoryAdapter = getAllUserRecipesHibernateRepositoryAdapter;
    }

    @Override
    public List<UserRecipe> execute(Long userId) {
        log.info("Executing get all user recipes database repository");

        List<UserRecipesHibernateModel> response = getAllUserRecipesHibernateRepositoryAdapter.findByUserId(userId);
        return response.stream().map(UserRecipesHibernateModel::toDomain).toList();
    }
}
