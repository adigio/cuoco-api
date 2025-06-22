package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.RecipeMealCategory;
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

@Entity(name = "recipe_meal_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeMealCategoriesHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeHibernateModel recipe;

    @ManyToOne
    @JoinColumn(name = "meal_category_id", referencedColumnName = "id")
    private MealCategoryHibernateModel category;

    public RecipeMealCategory toDomain() {
        return RecipeMealCategory.builder()
                .category(category.toDomain())
                .build();
    }
}
