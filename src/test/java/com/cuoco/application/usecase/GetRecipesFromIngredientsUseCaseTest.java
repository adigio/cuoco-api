package com.cuoco.application.usecase;

import com.cuoco.adapter.out.hibernate.GetRecipesFromIngredientsDatabaseRepositoryAdapter;
import com.cuoco.adapter.out.rest.gemini.GetRecipesFromIngredientsGeminiRestRepositoryAdapter;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.IngredientFactory;
import com.cuoco.factory.domain.RecipeFactory;
import com.cuoco.shared.utils.PlanConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetRecipesFromIngredientsUseCaseTest {

    private GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;
    private GetRecipesFromIngredientsRepository getRecipesFromIngredientsProvider;
    private CreateRecipeRepository recipeRepository;

    private GetRecipesFromIngredientsUseCase useCase;

    @BeforeEach
    void setUp() {
        getRecipesFromIngredientsRepository = mock(GetRecipesFromIngredientsDatabaseRepositoryAdapter.class);
        getRecipesFromIngredientsProvider = mock(GetRecipesFromIngredientsGeminiRestRepositoryAdapter.class);
        recipeRepository = mock(CreateRecipeRepository.class);

        useCase = new GetRecipesFromIngredientsUseCase(getRecipesFromIngredientsRepository, getRecipesFromIngredientsProvider, recipeRepository);


        ReflectionTestUtils.setField(useCase, "FREE_MAX_RECIPES", 3);
        ReflectionTestUtils.setField(useCase, "PREMIUM_MAX_RECIPES", 5);

        User user = User.builder()
                .plan(Plan.builder().id(PlanConstants.FREE.getValue()).build())
                .build();

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void GIVEN_enough_saved_recipes_WHEN_execute_THEN_return_limited_list() {
        List<Recipe> savedRecipes = List.of(
                RecipeFactory.create(),
                RecipeFactory.create(),
                RecipeFactory.create(),
                RecipeFactory.create()
        );

        List<Ingredient> ingredients = List.of(IngredientFactory.create());

        when(getRecipesFromIngredientsRepository.execute(any())).thenReturn(savedRecipes);

        GetRecipesFromIngredientsCommand.Command command = GetRecipesFromIngredientsCommand.Command.builder()
                .ingredients(ingredients)
                .build();

        List<Recipe> result = useCase.execute(command);

        assertEquals(3, result.size());
        verify(getRecipesFromIngredientsRepository).execute(any());
        verifyNoInteractions(getRecipesFromIngredientsProvider, recipeRepository);
    }

    @Test
    void GIVEN_few_saved_recipes_WHEN_execute_THEN_generate_and_save_missing() {
        List<Ingredient> ingredients = List.of(IngredientFactory.create());

        List<Recipe> savedRecipes = List.of(
                RecipeFactory.create()
        );

        List<Recipe> generatedRecipes = List.of(
                RecipeFactory.create(),
                RecipeFactory.create(),
                RecipeFactory.create(),
                RecipeFactory.create()
        );

        when(getRecipesFromIngredientsRepository.execute(any())).thenReturn(savedRecipes);
        when(getRecipesFromIngredientsProvider.execute(any())).thenReturn(generatedRecipes);
        when(recipeRepository.execute(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GetRecipesFromIngredientsCommand.Command command = GetRecipesFromIngredientsCommand.Command.builder()
                .ingredients(ingredients)
                .build();

        List<Recipe> result = useCase.execute(command);

        assertEquals(3, result.size());
        verify(getRecipesFromIngredientsProvider).execute(any());
        verify(recipeRepository, times(2)).execute(any());
    }

    @Test
    void GIVEN_no_saved_recipes_WHEN_execute_THEN_generate_and_return_limited() {
        List<Recipe> recipes = List.of(
                RecipeFactory.create(),
                RecipeFactory.create(),
                RecipeFactory.create()
        );

        List<Ingredient> ingredients = List.of(IngredientFactory.create());

        when(getRecipesFromIngredientsRepository.execute(any())).thenReturn(List.of());
        when(getRecipesFromIngredientsProvider.execute(any())).thenReturn(recipes);
        when(recipeRepository.execute(any())).thenAnswer(invocation -> invocation.getArgument(0));

        GetRecipesFromIngredientsCommand.Command command = GetRecipesFromIngredientsCommand.Command.builder()
                .ingredients(ingredients)
                .build();

        List<Recipe> result = useCase.execute(command);

        assertEquals(3, result.size());
        verify(getRecipesFromIngredientsProvider).execute(any());
        verify(recipeRepository, times(3)).execute(any());
    }

}
