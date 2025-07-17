package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserPaymentsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GetUserPaymentByExternalReferenceHibernateRepositoryAdapter extends JpaRepository<UserPaymentsHibernateModel, Long> {

    Optional<UserPaymentsHibernateModel> findByExternalReference(String externalReference);

}
