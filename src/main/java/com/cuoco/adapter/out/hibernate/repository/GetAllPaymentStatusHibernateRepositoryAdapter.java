package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.PaymentStatusHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GetAllPaymentStatusHibernateRepositoryAdapter extends JpaRepository<PaymentStatusHibernateModel, Long> {
}
