package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllAllergiesHibernateRepository;
import com.cuoco.application.port.out.GetAllAllergiesRepository;
import com.cuoco.application.usecase.model.Allergy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllAllergiesDatabaseRepositoryAdapter implements GetAllAllergiesRepository {

    private final GetAllAllergiesHibernateRepository getAllAllergiesHibernateRepository;

    public GetAllAllergiesDatabaseRepositoryAdapter(GetAllAllergiesHibernateRepository getAllAllergiesHibernateRepository) {
        this.getAllAllergiesHibernateRepository = getAllAllergiesHibernateRepository;
    }

    @Override
    public List<Allergy> execute() {
        log.info("Get all allergies from database");

        List<AllergyHibernateModel> response = getAllAllergiesHibernateRepository.findAll();

        return response.stream().map(AllergyHibernateModel::toDomain).toList();
    }
}
