package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetDietByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetDietByIdRepository;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetDietByIdDatabaseRepositoryAdapter implements GetDietByIdRepository {

    private final GetDietByIdHibernateRepositoryAdapter getDietByIdHibernateRepositoryAdapter;

    public GetDietByIdDatabaseRepositoryAdapter(GetDietByIdHibernateRepositoryAdapter getDietByIdHibernateRepositoryAdapter) {
        this.getDietByIdHibernateRepositoryAdapter = getDietByIdHibernateRepositoryAdapter;
    }

    @Override
    public Diet execute(Integer id) {
        Optional<DietHibernateModel> diet = getDietByIdHibernateRepositoryAdapter.findById(id);

        if (diet.isPresent()) {
            return diet.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.DIET_NOT_EXISTS.getValue());
        }
    }
}
