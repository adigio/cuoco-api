package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.IngredientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetRecipesFromIngredientsUseCaseTest {

    private GetRecipesFromIngredientsRepository getRecipesFromIngredientsRepository;
    private GetRecipesFromIngredientsUseCase useCase;

    @BeforeEach
    void setup() {
        getRecipesFromIngredientsRepository = mock(GetRecipesFromIngredientsRepository.class);
        useCase = new GetRecipesFromIngredientsUseCase(getRecipesFromIngredientsRepository);
    }

    @Test
    void GIVEN_valid_ingredients_WHEN_execute_THEN_return_recipe_list() {
        List<Ingredient> ingredients = List.of(
                IngredientFactory.create()
        );

        List<Recipe> expectedRecipes = List.of(
                Recipe.builder().name("Sandwich").build()
        );

        GetRecipesFromIngredientsCommand.Command command = GetRecipesFromIngredientsCommand.Command.builder()
                .ingredients(ingredients)
                .build();

        when(getRecipesFromIngredientsRepository.execute(ingredients)).thenReturn(expectedRecipes);

        List<Recipe> result = useCase.execute(command);

        assertEquals(1, result.size());
        assertEquals("Sandwich", result.get(0).getName());
        verify(getRecipesFromIngredientsRepository, times(1)).execute(ingredients);
    }
}
