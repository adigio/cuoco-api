package com.cuoco.application.command;

import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.RecipeFilter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetRecipesFromIngredientsCommandUnitTest {

    @Test
    void test1_commandShouldStoreFiltersAndIngredientsCorrectly() {
        // Given
        RecipeFilter filter = new RecipeFilter("corto", "facil", Arrays.asList("pasta"), "vegetariano", 4);
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("tomate", "imagen", true),
                new Ingredient("papa", "texto", false)
        );

        // When
        GetRecipesFromIngredientsCommand.Command command = new GetRecipesFromIngredientsCommand.Command(filter, ingredients);

        // Then
        assertEquals(filter, command.filters());
        assertEquals(ingredients, command.ingredients());
        assertEquals(2, command.ingredients().size());
    }

    @Test
    void test2_commandShouldHandleNullFilters() {
        // Given
        List<Ingredient> ingredients = Arrays.asList(new Ingredient("tomate", "imagen", true));

        // When
        GetRecipesFromIngredientsCommand.Command command = new GetRecipesFromIngredientsCommand.Command(null, ingredients);

        // Then
        assertNull(command.filters());
        assertEquals(ingredients, command.ingredients());
    }

    @Test
    void test3_commandShouldHandleEmptyIngredients() {
        // Given
        RecipeFilter filter = new RecipeFilter("corto", "facil", Arrays.asList("pasta"), "vegetariano", 2);
        List<Ingredient> emptyIngredients = Arrays.asList();

        // When
        GetRecipesFromIngredientsCommand.Command command = new GetRecipesFromIngredientsCommand.Command(filter, emptyIngredients);

        // Then
        assertEquals(filter, command.filters());
        assertTrue(command.ingredients().isEmpty());
    }

    @Test
    void test4_commandShouldHaveProperToString() {
        // Given
        RecipeFilter filter = new RecipeFilter("largo", "dificil", Arrays.asList("carne"), "omnivoro", 6);
        List<Ingredient> ingredients = Arrays.asList(new Ingredient("carne", "manual", true));

        // When
        GetRecipesFromIngredientsCommand.Command command = new GetRecipesFromIngredientsCommand.Command(filter, ingredients);

        // Then
        String result = command.toString();
        assertNotNull(result);
        assertTrue(result.contains("Command"));
        assertTrue(result.contains("filters"));
        assertTrue(result.contains("ingredients"));
    }

    @Test
    void test5_commandShouldHandleComplexFilters() {
        // Given
        RecipeFilter complexFilter = new RecipeFilter("medio", "intermedio",
                Arrays.asList("italiana", "mexicana", "china"), "vegano", 8);
        List<Ingredient> multipleIngredients = Arrays.asList(
                new Ingredient("quinoa", "manual", true),
                new Ingredient("aguacate", "imagen", true),
                new Ingredient("tofu", "texto", false)
        );

        // When
        GetRecipesFromIngredientsCommand.Command command = new GetRecipesFromIngredientsCommand.Command(complexFilter, multipleIngredients);

        // Then
        assertEquals(complexFilter, command.filters());
        assertEquals(3, command.ingredients().size());
        assertEquals("vegano", command.filters().getDiet());
        assertEquals(3, command.filters().getTypes().size());
    }
}