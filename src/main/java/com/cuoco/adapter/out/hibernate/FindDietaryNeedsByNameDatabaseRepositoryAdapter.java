package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedsHibernateModel;
import com.cuoco.application.port.out.FindDietaryNeedsByNameRepository;
import com.cuoco.application.usecase.model.DietaryNeeds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FindDietaryNeedsByNameDatabaseRepositoryAdapter implements FindDietaryNeedsByNameRepository {

    private final FindDietaryNeedsByNameHibernateRepositoryAdapter findDietaryNeedsByNameHibernateRepositoryAdapter;

    public FindDietaryNeedsByNameDatabaseRepositoryAdapter(FindDietaryNeedsByNameHibernateRepositoryAdapter findDietaryNeedsByNameHibernateRepositoryAdapter) {
        this.findDietaryNeedsByNameHibernateRepositoryAdapter = findDietaryNeedsByNameHibernateRepositoryAdapter;
    }

    @Override
    public List<DietaryNeeds> execute(List<String> dietaryNeedsNames) {
        List<DietaryNeedsHibernateModel> dietaryNeeds = findDietaryNeedsByNameHibernateRepositoryAdapter.findByNameIn(dietaryNeedsNames);

        return buildDietaryNeeds(dietaryNeeds);

    }

    private List<DietaryNeeds> buildDietaryNeeds(List<DietaryNeedsHibernateModel> dietaryNeeds) {
        return dietaryNeeds.stream()
                .map(model -> new DietaryNeeds(model.getId(), model.getName()))
                .collect(Collectors.toList());
    }
}
