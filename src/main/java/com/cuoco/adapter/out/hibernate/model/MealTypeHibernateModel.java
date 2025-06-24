package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.MealType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "meal_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealTypeHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public MealType toDomain() {
        return MealType.builder()
                .id(id)
                .description(description)
                .build();
    }

}
