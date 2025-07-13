package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RecipeControllerAdapterTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;

    @InjectMocks
    private RecipeControllerAdapter recipeControllerAdapter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(recipeControllerAdapter).build();
    }

    @Test
    void GIVEN_valid_ingredients_request_WHEN_generate_THEN_return_recipes_response() throws Exception {
        Recipe recipe = RecipeFactory.create();
        RecipeRequest request = RecipeFactory.getRecipeRequest();

        when(getRecipesFromIngredientsCommand.execute(any())).thenReturn(List.of(recipe));

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(recipe.getName()))
                .andExpect(jsonPath("$[0].preparation_time").exists())
                .andExpect(jsonPath("$[0].image").value(recipe.getImage()))
                .andExpect(jsonPath("$[0].subtitle").value(recipe.getSubtitle()))
                .andExpect(jsonPath("$[0].description").value(recipe.getDescription()))
                .andExpect(jsonPath("$[0].ingredients").isArray())
                .andExpect(jsonPath("$[0].ingredients[0].name").value(recipe.getIngredients().get(0).getName()));
    }

    @Test
    void GIVEN_valid_ingredients_request_WHEN_generation_fails_THEN_return_recipes_without_images() throws Exception {
        Recipe recipe = RecipeFactory.create();
        RecipeRequest request = RecipeFactory.getRecipeRequest();

        when(getRecipesFromIngredientsCommand.execute(any())).thenReturn(List.of(recipe));

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(recipe.getName()))
                .andExpect(jsonPath("$[0].image").value(recipe.getImage()));
    }
}
