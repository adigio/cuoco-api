package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Allergy;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "allergy")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergyHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public Allergy toDomain() {
        return Allergy.builder()
                .id(id)
                .description(description)
                .build();
    }
}