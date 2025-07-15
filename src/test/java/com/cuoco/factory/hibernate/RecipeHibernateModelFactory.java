package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeStepsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;

import java.util.List;

public class RecipeHibernateModelFactory {

    public static RecipeHibernateModel create() {
        return RecipeHibernateModel.builder()
                .id(1L)
                .name("Test Recipe")
                .subtitle("Test Subtitle")
                .description("Test Description")
                .imageUrl("test-image.jpg")
                .steps(List.of(
                        RecipeStepsHibernateModel.builder()
                                .id(1L)
                                .number(1)
                                .title("Step 1")
                                .description("Description 1")
                                .imageName("step1.jpg")
                                .build()
                ))
                .ingredients(List.of(
                        RecipeIngredientsHibernateModel.builder()
                                .id(1L)
                                .ingredient(
                                        IngredientHibernateModel.builder()
                                                .id(1L)
                                                .name("Test Ingredient")
                                                .unit(UnitHibernateModel.builder().id(1).description("Gramo").symbol("gr").build())
                                                .build()
                                )
                                .quantity(1.0)
                                .optional(false)
                                .build()
                        )
                )
                .build();
    }
} 