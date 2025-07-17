package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllAllergiesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.usecase.model.Allergy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllAllergiesDatabaseRepositoryAdapter implements GetAllAllergiesRepository {

    private final GetAllAllergiesHibernateRepositoryAdapter getAllAllergiesHibernateRepositoryAdapter;

    public GetAllAllergiesDatabaseRepositoryAdapter(GetAllAllergiesHibernateRepositoryAdapter getAllAllergiesHibernateRepositoryAdapter) {
        this.getAllAllergiesHibernateRepositoryAdapter = getAllAllergiesHibernateRepositoryAdapter;
    }

    @Override
    public List<Allergy> execute() {
        log.info("Get all allergies from database");

        List<AllergyHibernateModel> response = getAllAllergiesHibernateRepositoryAdapter.findAll();

        return response.stream().map(AllergyHibernateModel::toDomain).toList();
    }
}
