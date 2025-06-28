package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Ingredient;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "ingredients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryHibernateModel category;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private UnitHibernateModel unit;

    public Ingredient toDomain() {
        return Ingredient.builder()
                .name(name)
                .unit(unit.toDomain())
                .build();
    }

}