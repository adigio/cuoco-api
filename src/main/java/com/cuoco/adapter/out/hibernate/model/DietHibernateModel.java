package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Diet;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "diet")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public Diet toDomain() {
        return Diet
                .builder()
                .id(id)
                .description(description)
                .build();
    }
}