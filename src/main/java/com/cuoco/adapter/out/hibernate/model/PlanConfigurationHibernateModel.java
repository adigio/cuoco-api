package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.PlanConfiguration;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity(name = "plan_configuration")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanConfigurationHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private Integer quantity;
    private BigDecimal price;
    private String currency;

    public static PlanConfigurationHibernateModel fromDomain(PlanConfiguration planConfiguration) {
        return PlanConfigurationHibernateModel.builder()
                .title(planConfiguration.getTitle())
                .description(planConfiguration.getDescription())
                .quantity(planConfiguration.getQuantity())
                .price(planConfiguration.getPrice())
                .currency(planConfiguration.getCurrency())
                .build();
    }

    public PlanConfiguration toDomain() {
        return PlanConfiguration.builder()
                .title(title)
                .description(description)
                .quantity(quantity)
                .price(price)
                .currency(currency)
                .build();
    }
}
