package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllDietaryNeedsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllDietaryNeedsRepository;
import com.cuoco.application.usecase.model.DietaryNeed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllDietaryNeedsDatabaseRepositoryAdapter implements GetAllDietaryNeedsRepository {

    private final GetAllDietaryNeedsHibernateRepositoryAdapter getAllDietaryNeedsHibernateRepositoryAdapter;

    public GetAllDietaryNeedsDatabaseRepositoryAdapter(GetAllDietaryNeedsHibernateRepositoryAdapter getAllDietaryNeedsHibernateRepositoryAdapter) {
        this.getAllDietaryNeedsHibernateRepositoryAdapter = getAllDietaryNeedsHibernateRepositoryAdapter;
    }

    @Override
    public List<DietaryNeed> execute() {
        log.info("Get all dietary needs from database");

        List<DietaryNeedHibernateModel> response = getAllDietaryNeedsHibernateRepositoryAdapter.findAll();

        return response.stream().map(DietaryNeedHibernateModel::toDomain).toList();
    }
}
