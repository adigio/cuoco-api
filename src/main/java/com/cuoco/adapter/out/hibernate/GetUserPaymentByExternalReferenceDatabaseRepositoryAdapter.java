package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.exception.NotFoundException;
import com.cuoco.adapter.out.hibernate.model.UserPaymentsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetUserPaymentByExternalReferenceHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetUserPaymentByExternalReferenceRepository;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.shared.model.ErrorDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetUserPaymentByExternalReferenceDatabaseRepositoryAdapter implements GetUserPaymentByExternalReferenceRepository {

    private final GetUserPaymentByExternalReferenceHibernateRepositoryAdapter getUserPaymentByExternalReferenceHibernateRepositoryAdapter;

    @Override
    public UserPayment execute(String externalReference) {
        log.info("Executing get user payment by external reference {}", externalReference);

        Optional<UserPaymentsHibernateModel> maybeResponse = getUserPaymentByExternalReferenceHibernateRepositoryAdapter.findByExternalReference(externalReference);

        if(!maybeResponse.isPresent()) {
            log.info("Not found user payment with external reference {}", externalReference);
            throw new NotFoundException(ErrorDescription.NOT_FOUND.getValue());
        }

        UserPaymentsHibernateModel response = maybeResponse.get();
        return response.toDomain();
    }
}
