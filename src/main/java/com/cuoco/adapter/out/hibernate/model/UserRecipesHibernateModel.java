package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.UserRecipe;
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

@Entity(name = "user_recipes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecipesHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeHibernateModel recipe;

    public UserRecipe toDomain() {
        return UserRecipe.builder()
                .user(user.toDomain())
                .recipe(recipe.toDomain())
                .build();
    }
}