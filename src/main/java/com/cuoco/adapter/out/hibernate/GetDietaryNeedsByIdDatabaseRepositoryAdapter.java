package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetDietaryNeedsByIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetDietaryNeedsByIdRepository;
import com.cuoco.application.usecase.model.DietaryNeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GetDietaryNeedsByIdDatabaseRepositoryAdapter implements GetDietaryNeedsByIdRepository {

    static final Logger log = LoggerFactory.getLogger(GetDietaryNeedsByIdDatabaseRepositoryAdapter.class);

    private final GetDietaryNeedsByIdHibernateRepositoryAdapter getDietaryNeedsByIdHibernateRepositoryAdapter;

    public GetDietaryNeedsByIdDatabaseRepositoryAdapter(GetDietaryNeedsByIdHibernateRepositoryAdapter getDietaryNeedsByIdHibernateRepositoryAdapter) {
        this.getDietaryNeedsByIdHibernateRepositoryAdapter = getDietaryNeedsByIdHibernateRepositoryAdapter;
    }

    @Override
    public List<DietaryNeed> execute(List<Integer> dietaryNeedIds) {
        log.debug("Get dietary needs by IDs in {} from database", dietaryNeedIds);

        List<DietaryNeedHibernateModel> dietaryNeeds = getDietaryNeedsByIdHibernateRepositoryAdapter.findByIdIn(dietaryNeedIds);
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
