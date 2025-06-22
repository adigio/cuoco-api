package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.MealPrepFilter;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.IngredientFactory;
import com.cuoco.factory.domain.MealPrepFactory;
import com.cuoco.shared.utils.PlanConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class GetMealPrepsFromIngredientsUseCaseTest {
    private GetMealPrepsFromIngredientsRepository mealPrepsProvider;
    private GetMealPrepsFromIngredientsUseCase useCase;

    @BeforeEach
    void setUp() {
        mealPrepsProvider = mock(GetMealPrepsFromIngredientsRepository.class);
        useCase = new GetMealPrepsFromIngredientsUseCase(mealPrepsProvider);
    }

    @Test
    void GIVEN_premium_user_WHEN_execute_THEN_return_one_meal_prep() {
        // Given
        User user = User.builder()
                .plan(Plan.builder().id(PlanConstants.PREMIUM.getValue()).build())
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of())
        );

        MealPrep mealPrep1 = MealPrepFactory.create();
        MealPrep mealPrep2 = MealPrepFactory.create();

        when(mealPrepsProvider.execute(any())).thenReturn(List.of(mealPrep1, mealPrep2));

        GetMealPrepFromIngredientsCommand.Command command = GetMealPrepFromIngredientsCommand.Command.builder()
                .ingredients(List.of(IngredientFactory.create()))
                .build();

        // When
        List<MealPrep> result = useCase.execute(command);

        // Then
        assertEquals(1, result.size());
        assertEquals(mealPrep1.getName(), result.get(0).getName());
        verify(mealPrepsProvider).execute(any());
    }

    @Test
    void GIVEN_non_premium_user_WHEN_execute_THEN_throw_exception() {
        // Given
        User user = User.builder()
                .plan(Plan.builder().id(PlanConstants.FREE.getValue()).build())
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of())
        );

        GetMealPrepFromIngredientsCommand.Command command = GetMealPrepFromIngredientsCommand.Command.builder()
                .ingredients(List.of(IngredientFactory.create()))
                .filters(MealPrepFilter.builder().build())
                .build();

        // When / Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> useCase.execute(command));
        assertEquals("Only PREMIUM users can generate meal preps", exception.getMessage());

        verifyNoInteractions(mealPrepsProvider);
    }
}
