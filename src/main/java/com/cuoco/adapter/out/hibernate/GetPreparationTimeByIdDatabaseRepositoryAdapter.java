package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetPreparationTimeByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetPreparationTimeByIdRepository;
import com.cuoco.application.usecase.model.PreparationTime;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetPreparationTimeByIdDatabaseRepositoryAdapter implements GetPreparationTimeByIdRepository {

    public GetPreparationTimeByIdHibernateRepositoryAdapter getPreparationTimeByIdHibernateRepositoryAdapter;

    @Override
    public PreparationTime execute(Integer id) {
        Optional<PreparationTimeHibernateModel> preparationTime = getPreparationTimeByIdHibernateRepositoryAdapter.findById(id);

        if (preparationTime.isPresent()) {
            return preparationTime.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.PREPARATION_TIME_NOT_EXISTS.getValue());
        }
    }

}
