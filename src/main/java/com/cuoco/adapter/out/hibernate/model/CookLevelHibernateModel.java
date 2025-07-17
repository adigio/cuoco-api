package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.CookLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "cook_levels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CookLevelHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public static CookLevelHibernateModel fromDomain(CookLevel cookLevel) {
        return CookLevelHibernateModel.builder()
                .id(cookLevel.getId())
                .description(cookLevel.getDescription())
                .build();
    }

    public CookLevel toDomain() {
        return CookLevel.builder()
                .id(id)
                .description(description)
                .build();
    }
}