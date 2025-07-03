package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.CalendarRecipe;
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

@Entity(name = "user_calendar_recipes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCalendarRecipesHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_calendar_id", referencedColumnName = "id")
    private UserCalendarsHibernateModel calendar;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeHibernateModel recipe;

    @ManyToOne
    @JoinColumn(name = "meal_type_id", referencedColumnName = "id")
    private MealTypeHibernateModel mealType;

    public CalendarRecipe toDomain() {
        return CalendarRecipe.builder()
                .recipe(recipe.toDomain())
                .mealType(mealType.toDomain())
                .build();
    }

}
