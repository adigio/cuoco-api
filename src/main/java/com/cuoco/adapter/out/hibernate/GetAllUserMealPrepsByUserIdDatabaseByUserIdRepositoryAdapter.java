package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserMealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllUserMealPrepsByUserIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllUserMealPrepsByUserIdRepository;
import com.cuoco.application.usecase.model.UserMealPrep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GetAllUserMealPrepsByUserIdDatabaseByUserIdRepositoryAdapter implements GetAllUserMealPrepsByUserIdRepository {

    private final GetAllUserMealPrepsByUserIdHibernateRepositoryAdapter getAllUserMealPrepsByUserIdHibernateRepositoryAdapter;

    public GetAllUserMealPrepsByUserIdDatabaseByUserIdRepositoryAdapter(
            GetAllUserMealPrepsByUserIdHibernateRepositoryAdapter getAllUserMealPrepsByUserIdHibernateRepositoryAdapter
    ) {
        this.getAllUserMealPrepsByUserIdHibernateRepositoryAdapter = getAllUserMealPrepsByUserIdHibernateRepositoryAdapter;
    }

    @Override
    public List<UserMealPrep> execute(Long userId) {
        log.info("Executing get all user meal preps database repository");

        List<UserMealPrepHibernateModel> response = getAllUserMealPrepsByUserIdHibernateRepositoryAdapter.findByUserId(userId);

        return response.stream().map(UserMealPrepHibernateModel::toDomain).toList();
    }
}
