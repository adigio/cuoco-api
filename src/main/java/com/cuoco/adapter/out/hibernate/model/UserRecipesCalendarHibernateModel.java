package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.UserRecipeCalendar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity(name = "user_recipes_calendar")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecipesCalendarHibernateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeHibernateModel recipe;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne
    @JoinColumn(name = "meal_type_id", referencedColumnName = "id")
    private MealTypeHibernateModel mealType;

    private LocalDate plannedDate;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public RecipeHibernateModel getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeHibernateModel recipe) {
        this.recipe = recipe;
    }

    public UserHibernateModel getUser() {
        return user;
    }

    public void setUser(UserHibernateModel user) {
        this.user = user;
    }

    public MealTypeHibernateModel getMealType() {
        return mealType;
    }

    public void setMealType(MealTypeHibernateModel mealType) {
        this.mealType = mealType;
    }

    public LocalDate getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(LocalDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    public UserRecipeCalendar toDomain(){
        return UserRecipeCalendar.builder()
                .recipe(recipe.toDomain())
                .mealType(mealType.toDomain())
                .date(plannedDate)
                .build();
    }
}
