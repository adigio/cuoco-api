package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealCategoryHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetMealCategoryByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetMealCategoryByIdRepository;
import com.cuoco.application.usecase.model.MealCategory;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetMealCategoryByIdDatabaseRepositoryAdapter implements GetMealCategoryByIdRepository {

    private final GetMealCategoryByIdHibernateRepositoryAdapter getMealCategoryByIdHibernateRepositoryAdapter;

    public GetMealCategoryByIdDatabaseRepositoryAdapter(GetMealCategoryByIdHibernateRepositoryAdapter getMealCategoryByIdHibernateRepositoryAdapter) {
        this.getMealCategoryByIdHibernateRepositoryAdapter = getMealCategoryByIdHibernateRepositoryAdapter;
    }

    @Override
    public MealCategory execute(Integer id) {
        Optional<MealCategoryHibernateModel> category = getMealCategoryByIdHibernateRepositoryAdapter.findById(id);

        if (category.isPresent()) {
            return category.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.MEAL_CATEGORY_NOT_EXISTS.getValue());
        }
    }

}
