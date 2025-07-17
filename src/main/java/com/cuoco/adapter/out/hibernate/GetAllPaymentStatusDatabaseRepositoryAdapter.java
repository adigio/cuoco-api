package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.PaymentStatusHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllPaymentStatusHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllPaymentStatusRepository;
import com.cuoco.application.usecase.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetAllPaymentStatusDatabaseRepositoryAdapter implements GetAllPaymentStatusRepository {

    private final GetAllPaymentStatusHibernateRepositoryAdapter getAllPaymentStatusHibernateRepositoryAdapter;

    @Override
    public List<PaymentStatus> getAll() {
        log.info("Get all payment statuses from database");

        List<PaymentStatusHibernateModel> response = getAllPaymentStatusHibernateRepositoryAdapter.findAll();

        return response.stream().map(PaymentStatusHibernateModel::toDomain).toList();
    }
}
