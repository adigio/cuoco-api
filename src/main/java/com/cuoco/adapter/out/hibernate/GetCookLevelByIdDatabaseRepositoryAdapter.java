package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetCookLevelByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.out.GetCookLevelByIdRepository;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.shared.model.ErrorDescription;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GetCookLevelByIdDatabaseRepositoryAdapter implements GetCookLevelByIdRepository {

    private GetCookLevelByIdHibernateRepositoryAdapter getCookLevelByIdHibernateRepositoryAdapter;

    public GetCookLevelByIdDatabaseRepositoryAdapter(GetCookLevelByIdHibernateRepositoryAdapter getCookLevelByIdHibernateRepositoryAdapter) {
        this.getCookLevelByIdHibernateRepositoryAdapter = getCookLevelByIdHibernateRepositoryAdapter;
    }

    @Override
    public CookLevel execute(Integer id) {

        Optional<CookLevelHibernateModel> cookLevel = getCookLevelByIdHibernateRepositoryAdapter.findById(id);

        if (cookLevel.isPresent()) {
            return cookLevel.get().toDomain();
        } else {
            throw new BadRequestException(ErrorDescription.COOK_LEVEL_NOT_EXISTS.getValue());
        }
    }
}
