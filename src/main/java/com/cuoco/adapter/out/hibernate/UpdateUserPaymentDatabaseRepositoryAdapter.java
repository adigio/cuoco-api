package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.UserHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UserPaymentsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateUserPaymentHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateUserPaymentRepository;
import com.cuoco.application.port.out.UpdateUserPaymentRepository;
import com.cuoco.application.usecase.model.UserPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Qualifier("repository")
@RequiredArgsConstructor
public class UpdateUserPaymentDatabaseRepositoryAdapter implements UpdateUserPaymentRepository {

    private final CreateUserPaymentHibernateRepositoryAdapter createUserPaymentHibernateRepositoryAdapter;

    @Override
    public UserPayment execute(UserPayment userPayment) {
        log.info("Executing update user payment in database");

        UserPaymentsHibernateModel userPaymentToUpdate = UserPaymentsHibernateModel.fromDomain(userPayment);

        userPaymentToUpdate.setUser(UserHibernateModel.builder().id(userPayment.getUser().getId()).build());

        UserPaymentsHibernateModel updatedUserPayment = createUserPaymentHibernateRepositoryAdapter.save(userPaymentToUpdate);

        log.info("Updated user payment with ID {}", updatedUserPayment.getId());

        UserPayment response = updatedUserPayment.toDomain();

        response.setUser(userPayment.getUser());

        return response;
    }
}
