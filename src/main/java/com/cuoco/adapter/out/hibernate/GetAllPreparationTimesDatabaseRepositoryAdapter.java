package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllPreparationTimesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllPreparationTimesRepository;
import com.cuoco.application.usecase.model.PreparationTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllPreparationTimesDatabaseRepositoryAdapter implements GetAllPreparationTimesRepository {

    private GetAllPreparationTimesHibernateRepositoryAdapter getAllPreparationTimesHibernateRepositoryAdapter;

    public GetAllPreparationTimesDatabaseRepositoryAdapter(GetAllPreparationTimesHibernateRepositoryAdapter getAllPreparationTimesHibernateRepositoryAdapter) {
        this.getAllPreparationTimesHibernateRepositoryAdapter = getAllPreparationTimesHibernateRepositoryAdapter;
    }

    @Override
    public List<PreparationTime> execute() {
        log.info("Get all preparation times from database");
        List<PreparationTimeHibernateModel> response = getAllPreparationTimesHibernateRepositoryAdapter.findAll();
        return response.stream().map(PreparationTimeHibernateModel::toDomain).toList();
    }
}
