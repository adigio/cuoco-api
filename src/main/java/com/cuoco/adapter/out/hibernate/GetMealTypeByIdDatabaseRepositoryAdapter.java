package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetMealTypeByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetMealTypeByIdRepository;
import com.cuoco.application.usecase.model.MealType;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetMealTypeByIdDatabaseRepositoryAdapter implements GetMealTypeByIdRepository {

    private final GetMealTypeByIdHibernateRepositoryAdapter getMealTypeByIdHibernateRepositoryAdapter;

    public GetMealTypeByIdDatabaseRepositoryAdapter(GetMealTypeByIdHibernateRepositoryAdapter getMealTypeByIdHibernateRepositoryAdapter) {
        this.getMealTypeByIdHibernateRepositoryAdapter = getMealTypeByIdHibernateRepositoryAdapter;
    }

    @Override
    public MealType execute(Integer id) {
        Optional<MealTypeHibernateModel> type = getMealTypeByIdHibernateRepositoryAdapter.findById(id);

        if (type.isPresent()) {
            return type.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.MEAL_TYPE_NOT_EXISTS.getValue());
        }
    }

}
