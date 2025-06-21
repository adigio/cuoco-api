package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserRecipesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetUserHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetUserRecipesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.UserRecipe;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class GetUserRecipesRepositoryAdapter implements GetUserRecipesRepository {

    private GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter;

    public GetUserRecipesRepositoryAdapter(GetUserHibernateRepositoryAdapter getUserHibernateRepositoryAdapter) {
        this.getUserHibernateRepositoryAdapter = getUserHibernateRepositoryAdapter;
    }

    @Override
    public List<UserRecipe> execute(long userId) {
        List<UserRecipesHibernateModel> response = getUserHibernateRepositoryAdapter.findByUserId(userId);
        return response.stream().map(UserRecipesHibernateModel::toDomain).toList();
    }
}
