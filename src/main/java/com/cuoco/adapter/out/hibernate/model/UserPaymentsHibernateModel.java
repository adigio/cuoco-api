package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPayment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "user_payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentsHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @OneToOne
    @JoinColumn(name = "to_plan_id")
    private PlanHibernateModel toPlan;

    @OneToOne
    @JoinColumn(name = "status_id")
    private PaymentStatusHibernateModel status;

    private String externalId;
    private String externalReference;
    private String checkoutUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserPaymentsHibernateModel fromDomain(UserPayment userPayment) {
        return UserPaymentsHibernateModel.builder()
                .id(userPayment.getId())
                .user(UserHibernateModel.fromDomain(userPayment.getUser()))
                .toPlan(PlanHibernateModel.fromDomain(userPayment.getPlan()))
                .status(PaymentStatusHibernateModel.fromDomain(userPayment.getStatus()))
                .externalId(userPayment.getExternalId())
                .externalReference(userPayment.getExternalReference())
                .checkoutUrl(userPayment.getCheckoutUrl())
                .build();
    }

    public UserPayment toDomain() {
        return UserPayment.builder()
                .id(id)
                .user(user.toDomain())
                .plan(toPlan.toDomain())
                .status(status.toDomain())
                .externalId(externalId)
                .externalReference(externalReference)
                .checkoutUrl(checkoutUrl)
                .build();
    }

}
