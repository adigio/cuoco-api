package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.GetRecipeByIDHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.usecase.model.Recipe;
import org.springframework.stereotype.Repository;

@Repository
public class GetRecipeFromIDRepositoryAdapter implements GetRecipeByIdRepository {

    private final GetRecipeByIDHibernateRepositoryAdapter getRecipeByIDHibernateRepositoryAdapter;

    public GetRecipeFromIDRepositoryAdapter(GetRecipeByIDHibernateRepositoryAdapter getRecipeByIDHibernateRepositoryAdapter) {
        this.getRecipeByIDHibernateRepositoryAdapter = getRecipeByIDHibernateRepositoryAdapter;
    }

    @Override
    public Recipe execute(long id) {
        return getRecipeByIDHibernateRepositoryAdapter.findById(id).get().toDomain();
    }
}
