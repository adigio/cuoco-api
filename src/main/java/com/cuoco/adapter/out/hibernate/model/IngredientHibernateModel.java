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

    public static IngredientHibernateModel fromDomain(Ingredient ingredient) {
        return IngredientHibernateModel.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .unit(UnitHibernateModel.fromDomain(ingredient.getUnit()))
                .build();
    }

    public Ingredient toDomain(Double quantity, Boolean optional) {
        return Ingredient.builder()
                .id(id)
                .name(name)
                .quantity(quantity)
                .optional(optional)
                .unit(unit.toDomain())
                .build();
    }

}