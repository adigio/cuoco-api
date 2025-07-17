package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetMealPrepByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetMealPrepByIdRepository;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class GetMealPrepByIdDatabaseRepositoryAdapter implements GetMealPrepByIdRepository {

    private final GetMealPrepByIdHibernateRepositoryAdapter getMealPrepByIdHibernateRepositoryAdapter;

    public GetMealPrepByIdDatabaseRepositoryAdapter(GetMealPrepByIdHibernateRepositoryAdapter getMealPrepByIdHibernateRepositoryAdapter) {
        this.getMealPrepByIdHibernateRepositoryAdapter = getMealPrepByIdHibernateRepositoryAdapter;
    }

    @Override
    public MealPrep execute(Long id) {
        Optional<MealPrepHibernateModel> oMealPrep = getMealPrepByIdHibernateRepositoryAdapter.findById(id);

        if (oMealPrep.isPresent()) {
            return oMealPrep.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.MEAL_PREP_NOT_EXISTS.getValue());
        }
    }
}
