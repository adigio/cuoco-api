package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllMealPrepsByIdsHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllMealPrepsByIdsRepository;
import com.cuoco.application.usecase.model.MealPrep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetAllMealPrepsByIdsDatabaseRepositoryAdapter implements GetAllMealPrepsByIdsRepository {

    private final GetAllMealPrepsByIdsHibernateRepositoryAdapter getAllMealPrepsByIdsHibernateRepositoryAdapter;

    @Override
    public List<MealPrep> execute(List<Long> ids) {
        log.info("Get all meal preps by ids: {}", ids);

        List<MealPrepHibernateModel> recipes = getAllMealPrepsByIdsHibernateRepositoryAdapter.findAllById(ids);
        return recipes.stream().map(MealPrepHibernateModel::toDomain).toList();
    }
}
