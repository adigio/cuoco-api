package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetCookLevelByIdHibernateRepository;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetCookLevelByIdDatabaseRepository implements GetCookLevelByIdRepository {

    private GetCookLevelByIdHibernateRepository getCookLevelByIdHibernateRepository;

    public GetCookLevelByIdDatabaseRepository(GetCookLevelByIdHibernateRepository getCookLevelByIdHibernateRepository) {
        this.getCookLevelByIdHibernateRepository = getCookLevelByIdHibernateRepository;
    }

    @Override
    public CookLevel execute(Integer id) {

        Optional<CookLevelHibernateModel> cookLevel = getCookLevelByIdHibernateRepository.findById(id);

        if (cookLevel.isPresent()) {
            return cookLevel.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.COOK_LEVEL_NOT_EXISTS.getValue());
        }
    }
}
