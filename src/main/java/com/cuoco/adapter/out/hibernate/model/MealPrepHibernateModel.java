package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.MealPrep;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "meal_preps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPrepHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String estimatedCookingTime;
    private Integer servings;
    private Boolean freeze;

    @OneToMany(mappedBy = "mealPrep", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MealPrepStepsHibernateModel> steps;

    @ManyToMany
    @JoinTable(
            name = "meal_prep_recipes",
            joinColumns = @JoinColumn(name = "meal_prep_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<RecipeHibernateModel> recipes;

    @OneToMany(mappedBy = "mealPrep", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MealPrepIngredientsHibernateModel> ingredients;

    public MealPrep toDomain() {
        return MealPrep.builder()
                .id(id)
                .title(title)
                .estimatedCookingTime(estimatedCookingTime)
                .servings(servings)
                .freeze(freeze)
                .steps(steps.stream().map(MealPrepStepsHibernateModel::toDomain).toList())
                .recipes(recipes.stream().map(RecipeHibernateModel::toDomain).toList())
                .ingredients(ingredients.stream().map(MealPrepIngredientsHibernateModel::toDomain).toList())
                .build();
    }
}
