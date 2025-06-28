package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "diets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public static DietHibernateModel fromDomain(Diet diet) {
        return DietHibernateModel.builder()
                .id(diet.getId())
                .description(diet.getDescription())
                .build();
    }

    public Diet toDomain() {
        return Diet
                .builder()
                .id(id)
                .description(description)
                .build();
    }
}