package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "payment_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public static PaymentStatusHibernateModel fromDomain(PaymentStatus paymentStatus) {
        return PaymentStatusHibernateModel.builder()
                .id(paymentStatus.getId())
                .description(paymentStatus.getDescription())
                .build();
    }

    public PaymentStatus toDomain() {
        return PaymentStatus.builder()
                .id(id)
                .description(description)
                .build();
    }
}
