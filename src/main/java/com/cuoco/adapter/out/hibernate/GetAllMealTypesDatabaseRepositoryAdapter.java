package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllMealTypesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllMealTypesRepository;
import com.cuoco.application.usecase.model.MealType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllMealTypesDatabaseRepositoryAdapter implements GetAllMealTypesRepository {

    private final GetAllMealTypesHibernateRepositoryAdapter getAllMealTypesHibernateRepositoryAdapter;

    public GetAllMealTypesDatabaseRepositoryAdapter(GetAllMealTypesHibernateRepositoryAdapter getAllMealTypesHibernateRepositoryAdapter) {
        this.getAllMealTypesHibernateRepositoryAdapter = getAllMealTypesHibernateRepositoryAdapter;
    }

    @Override
    public List<MealType> execute() {
        log.info("Get all meal types from database");

        List<MealTypeHibernateModel> response = getAllMealTypesHibernateRepositoryAdapter.findAll();

        return response.stream().map(MealTypeHibernateModel::toDomain).toList();
    }
}
