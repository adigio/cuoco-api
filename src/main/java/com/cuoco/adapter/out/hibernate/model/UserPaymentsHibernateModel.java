package com.cuoco.adapter.out.hibernate.model;

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

    private String externalId;
    private String externalReference;
    private String checkoutUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserPaymentsHibernateModel fromDomain(UserPayment userPayment) {
        return UserPaymentsHibernateModel.builder()
                .id(userPayment.getId())
                .toPlan(PlanHibernateModel.fromDomain(userPayment.getPlan()))
                .externalId(userPayment.getExternalId())
                .externalReference(userPayment.getExternalReference())
                .checkoutUrl(userPayment.getCheckoutUrl())
                .build();
    }

    public UserPayment toDomain() {
        return UserPayment.builder()
                .plan(toPlan.toDomain())
                .externalId(externalId)
                .externalReference(externalReference)
                .checkoutUrl(checkoutUrl)
                .build();
    }

}
