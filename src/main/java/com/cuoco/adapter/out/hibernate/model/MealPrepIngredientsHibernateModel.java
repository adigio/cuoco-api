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

@Entity(name = "meal_prep_ingredients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPrepIngredientsHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meal_prep_id", referencedColumnName = "id")
    private MealPrepHibernateModel mealPrep;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private IngredientHibernateModel ingredient;

    private Double quantity;

    public Ingredient toDomain() {
        return ingredient.toDomain(quantity, false);
    }

}
