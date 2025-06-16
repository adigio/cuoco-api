package com.cuoco.factory;

import com.cuoco.application.usecase.model.Allergy;
import com.cuoco.application.usecase.model.CookLevel;
import com.cuoco.application.usecase.model.Diet;
import com.cuoco.application.usecase.model.DietaryNeed;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPreferences;

import java.time.LocalDateTime;
import java.util.List;

public class UserFactory {

    public static User create() {
        return User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .plan(
                        Plan.builder().id(1).description("plan a").build()
                )
                .active(true)
                .preferences(
                        UserPreferences.builder()
                                .diet(Diet.builder().id(1).description("diet").build())
                                .cookLevel(CookLevel.builder().id(1).description("cookLevel").build())
                                .build()
                )
                .createdAt(LocalDateTime.now())
                .dietaryNeeds(List.of(
                        DietaryNeed.builder().id(1).description("dietary need 1").build(),
                        DietaryNeed.builder().id(2).description("dietary need 2").build(),
                        DietaryNeed.builder().id(3).description("dietary need 3").build()
                ))
                .allergies(List.of(
                        Allergy.builder().id(1).description("allergy 1").build(),
                        Allergy.builder().id(2).description("allergy 2").build(),
                        Allergy.builder().id(3).description("allergy 3").build()
                ))
                .build();
    }
}