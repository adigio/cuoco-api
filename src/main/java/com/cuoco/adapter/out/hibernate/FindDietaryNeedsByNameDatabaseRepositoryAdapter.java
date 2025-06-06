package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedsHibernateModel;
import com.cuoco.application.port.out.FindDietaryNeedsByNameRepository;
import com.cuoco.application.usecase.model.DietaryNeeds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FindDietaryNeedsByNameDatabaseRepositoryAdapter implements FindDietaryNeedsByNameRepository {

    private final FindDietaryNeedsByNameHibernateRepositoryAdapter findDietaryNeedsByNameHibernateRepositoryAdapter;

    public FindDietaryNeedsByNameDatabaseRepositoryAdapter(FindDietaryNeedsByNameHibernateRepositoryAdapter findDietaryNeedsByNameHibernateRepositoryAdapter) {
        this.findDietaryNeedsByNameHibernateRepositoryAdapter = findDietaryNeedsByNameHibernateRepositoryAdapter;
    }

    @Override
    public List<DietaryNeeds> execute(List<String> dietaryNeedsNames) {
        List<DietaryNeedsHibernateModel> dietaryNeeds = findDietaryNeedsByNameHibernateRepositoryAdapter.findByNameIn(dietaryNeedsNames);

        //recibir dietartneedhibernatemodel y convertir en dietarynneedmodel y retornala


    }
}
