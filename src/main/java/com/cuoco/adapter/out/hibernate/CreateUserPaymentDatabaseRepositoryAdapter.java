package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPaymentsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateUserPaymentHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserPaymentRepository;
import com.cuoco.application.usecase.model.UserPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Qualifier("repository")
@RequiredArgsConstructor
public class CreateUserPaymentDatabaseRepositoryAdapter implements CreateUserPaymentRepository {

    private final CreateUserPaymentHibernateRepositoryAdapter createUserPaymentHibernateRepositoryAdapter;

    @Override
    public UserPayment execute(UserPayment userPayment) {
        log.info("Executing create user payment in database");

        UserPaymentsHibernateModel userPaymentToSave = UserPaymentsHibernateModel.fromDomain(userPayment);

        UserPaymentsHibernateModel savedUserPayment = createUserPaymentHibernateRepositoryAdapter.save(userPaymentToSave);

        log.info("Saved user payment with ID {}", savedUserPayment.getId());

        UserPayment response = savedUserPayment.toDomain();

        response.setUser(userPayment.getUser());

        return response;
    }
}
