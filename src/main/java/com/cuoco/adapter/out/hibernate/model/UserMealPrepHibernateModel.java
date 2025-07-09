package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.UserMealPrep;
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

@Entity(name = "user_meal_preps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMealPrepHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne
    @JoinColumn(name = "meal_prep_id", referencedColumnName = "id")
    private MealPrepHibernateModel mealPrep;

    public UserMealPrep toDomain() {
        return UserMealPrep.builder()
                .user(user.toDomain())
                .mealPrep(mealPrep.toDomain())
                .build();
    }
}
