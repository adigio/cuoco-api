package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.repository.GetRecipeTableSizeHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetRecipeTableSizeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class GetRecipesTableSizeRepositoryAdapter implements GetRecipeTableSizeRepository {

    private GetRecipeTableSizeHibernateRepositoryAdapter getRecipeTableSizeRepositoryAdapter;

    public GetRecipesTableSizeRepositoryAdapter(GetRecipeTableSizeHibernateRepositoryAdapter getRecipeTableSizeRepositoryAdapter) {
        this.getRecipeTableSizeRepositoryAdapter = getRecipeTableSizeRepositoryAdapter;
    }

    @Override
    public Long execute() {
        return getRecipeTableSizeRepositoryAdapter.count();
    }
}
