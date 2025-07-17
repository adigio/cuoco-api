package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserPaymentsHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateUserPaymentHibernateRepositoryAdapter extends JpaRepository<UserPaymentsHibernateModel, Long> {
}
