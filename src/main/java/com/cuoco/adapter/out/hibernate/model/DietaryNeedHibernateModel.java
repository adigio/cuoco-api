package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.DietaryNeed;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "dietary_needs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietaryNeedHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public static DietaryNeedHibernateModel fromDomain(DietaryNeed dietaryNeed) {
        return DietaryNeedHibernateModel.builder()
                .id(dietaryNeed.getId())
                .description(dietaryNeed.getDescription())
                .build();
    }

    public DietaryNeed toDomain() {
        return DietaryNeed.builder()
                .id(id)
                .description(description)
                .build();
    }
}