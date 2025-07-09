package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.UserProPlanPaymentHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProPlanPaymentRepository extends JpaRepository<UserProPlanPaymentHibernateModel, Long> {
    Optional<UserProPlanPaymentHibernateModel> findTopByUserIdAndExpirationDateAfterOrderByExpirationDateDesc(Long userId, java.time.LocalDateTime now);
    Optional<UserProPlanPaymentHibernateModel> findByExternalReference(String externalReference);
} 