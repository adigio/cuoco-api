package com.cuoco.factory.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.model.CookLevelHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietHibernateModel;
import com.cuoco.adapter.out.hibernate.model.DietaryNeedHibernateModel;
import com.cuoco.adapter.out.hibernate.model.IngredientHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealPrepHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealPrepIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealPrepStepsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.MealTypeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.PreparationTimeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeIngredientsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeStepsHibernateModel;
import com.cuoco.adapter.out.hibernate.model.UnitHibernateModel;
import com.cuoco.application.usecase.model.Step;

import java.util.List;

public class MealPrepHibernateModelFactory {

    public static MealPrepHibernateModel create() {
        return MealPrepHibernateModel.builder()
                .id(1L)
                .title("Test Meal Prep")
                .estimatedCookingTime("30 minutes")
                .steps(List.of(
                        MealPrepStepsHibernateModel.builder()
                                .id(2L)
                                .title("step 1")
                                .imageName("image1")
                                .description("description1")
                                .build()
                ))
                .recipes(List.of(
                        RecipeHibernateModel.builder()
                                .id(1L)
                                .name("Recipe 1")
                                .description("description")
                                .ingredients(List.of(
                                        RecipeIngredientsHibernateModel.builder()
                                                .ingredient(
                                                        IngredientHibernateModel.builder()
                                                                .id(1L)
                                                                .name("Harina")
                                                                .unit(UnitHibernateModel.builder().id(1).symbol("gr").build())
                                                                .build()
                                                )
                                                .build()
                                ))
                                .steps(List.of(
                                        RecipeStepsHibernateModel.builder()
                                                .id(2L)
                                                .title("step 1")
                                                .imageName("image1")
                                                .description("description1")
                                                .build()
                                ))
                                .preparationTime(PreparationTimeHibernateModel.builder().id(1).description("description").build())
                                .diet(DietHibernateModel.builder().id(1).description("description").build())
                                .cookLevel(CookLevelHibernateModel.builder().id(1).description("description").build())
                                .dietaryNeeds(List.of(DietaryNeedHibernateModel.builder().id(1).description("description").build()))
                                .allergies(List.of(AllergyHibernateModel.builder().id(1).description("description").build()))
                                .mealTypes(List.of(MealTypeHibernateModel.builder().id(1).description("description").build()))
                                .build()
                ))
                .ingredients(List.of(
                        MealPrepIngredientsHibernateModel.builder()
                                .ingredient(
                                        IngredientHibernateModel.builder()
                                                .id(1L)
                                                .name("Harina")
                                                .unit(UnitHibernateModel.builder().id(1).symbol("gr").build())
                                                .build()
                                )
                                .build()
                ))
                .servings(4)
                .freeze(true)
                .build();
    }
} 