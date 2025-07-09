package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllUserRecipesByUserIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllUserRecipesByUserIdRepository;
import com.cuoco.application.usecase.model.UserRecipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllUserRecipesByUserUserIdDatabaseRepositoryAdapter implements GetAllUserRecipesByUserIdRepository {

    private final GetAllUserRecipesByUserIdHibernateRepositoryAdapter getAllUserRecipesByUserIdHibernateRepositoryAdapter;

    public GetAllUserRecipesByUserUserIdDatabaseRepositoryAdapter(GetAllUserRecipesByUserIdHibernateRepositoryAdapter getAllUserRecipesByUserIdHibernateRepositoryAdapter) {
        this.getAllUserRecipesByUserIdHibernateRepositoryAdapter = getAllUserRecipesByUserIdHibernateRepositoryAdapter;
    }

    @Override
    public List<UserRecipe> execute(Long userId) {
        log.info("Executing get all user recipes database repository");

        List<UserRecipesHibernateModel> response = getAllUserRecipesByUserIdHibernateRepositoryAdapter.findByUserId(userId);
        return response.stream().map(UserRecipesHibernateModel::toDomain).toList();
    }
}
