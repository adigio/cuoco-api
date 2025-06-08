package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.FindDietaryNeedsByDescriptionHibernateRepositoryAdapter;
import com.cuoco.application.port.out.FindDietaryNeedsByDescriptionRepository;
import com.cuoco.application.usecase.model.DietaryNeed;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FindDietaryNeedsByDescriptionDatabaseRepositoryAdapter implements FindDietaryNeedsByDescriptionRepository {

    private final FindDietaryNeedsByDescriptionHibernateRepositoryAdapter findDietaryNeedsByDescriptionHibernateRepositoryAdapter;

    public FindDietaryNeedsByDescriptionDatabaseRepositoryAdapter(FindDietaryNeedsByDescriptionHibernateRepositoryAdapter findDietaryNeedsByDescriptionHibernateRepositoryAdapter) {
        this.findDietaryNeedsByDescriptionHibernateRepositoryAdapter = findDietaryNeedsByDescriptionHibernateRepositoryAdapter;
    }

    @Override
    public List<DietaryNeed> execute(List<String> descriptions) {
        List<DietaryNeedHibernateModel> dietaryNeeds = findDietaryNeedsByDescriptionHibernateRepositoryAdapter.findByDescriptionIn(descriptions);

        return buildDietaryNeeds(dietaryNeeds);
    }

    private List<DietaryNeed> buildDietaryNeeds(List<DietaryNeedHibernateModel> dietaryNeeds) {
        return dietaryNeeds.stream()
                .map(this::buildDietaryNeed)
                .collect(Collectors.toList());
    }

    private DietaryNeed buildDietaryNeed(DietaryNeedHibernateModel dietaryNeedResponse) {
        return DietaryNeed.builder()
                .id(dietaryNeedResponse.getId())
                .description(dietaryNeedResponse.getDescription())
                .build();
    }
}
