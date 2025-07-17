package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Plan;
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

@Entity(name = "plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    @OneToOne
    @JoinColumn(name = "configuration_id")
    private PlanConfigurationHibernateModel configuration;

    public static PlanHibernateModel fromDomain(Plan plan) {
        return PlanHibernateModel.builder()
                .id(plan.getId())
                .description(plan.getDescription())
                .configuration(plan.getConfiguration() != null ? PlanConfigurationHibernateModel.fromDomain(plan.getConfiguration()) : null)
                .build();
    }

    public Plan toDomain() {
        return Plan.builder()
                .id(id)
                .description(description)
                .configuration(configuration != null ? configuration.toDomain() : null)
                .build();
    }
}