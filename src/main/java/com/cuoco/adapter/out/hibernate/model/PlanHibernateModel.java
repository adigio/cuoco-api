package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Plan;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    public static PlanHibernateModel fromDomain(Plan plan) {
        return PlanHibernateModel.builder()
                .id(plan.getId())
                .description(plan.getDescription())
                .build();
    }

    public Plan toDomain() {
        return Plan.builder()
                .id(id)
                .description(description)
                .build();
    }
}