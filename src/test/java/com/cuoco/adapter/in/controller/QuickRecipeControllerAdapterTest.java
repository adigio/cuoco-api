package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.QuickRecipeRequest;
import com.cuoco.adapter.in.controller.model.RecipeResponse;
import com.cuoco.application.port.in.FindOrGenerateRecipeCommand;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class QuickRecipeControllerAdapterTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FindOrGenerateRecipeCommand findOrGenerateRecipeCommand;

    private QuickRecipeControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new QuickRecipeControllerAdapter(findOrGenerateRecipeCommand);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void GIVEN_valid_recipe_name_WHEN_findOrGenerate_THEN_return_recipe_response() throws Exception {
        // Given
        String recipeName = "Pasta Bolognesa";
        Recipe recipe = RecipeFactory.create();
        recipe.setName(recipeName);
        
        QuickRecipeRequest request = QuickRecipeRequest.builder()
                .recipeName(recipeName)
                .build();

        when(findOrGenerateRecipeCommand.execute(any())).thenReturn(recipe);

        // When & Then
        mockMvc.perform(post("/quick-recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(recipeName))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subtitle").value("RECIPE SUBTITLE"))
                .andExpect(jsonPath("$.description").value("RECIPE DESCRIPTION"));
    }

    @Test
    void GIVEN_empty_recipe_name_WHEN_findOrGenerate_THEN_return_bad_request() throws Exception {
        // Given
        QuickRecipeRequest request = QuickRecipeRequest.builder()
                .recipeName("")
                .build();

        // When & Then
        mockMvc.perform(post("/quick-recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void GIVEN_null_recipe_name_WHEN_findOrGenerate_THEN_return_bad_request() throws Exception {
        // Given
        QuickRecipeRequest request = QuickRecipeRequest.builder()
                .recipeName(null)
                .build();

        // When & Then
        mockMvc.perform(post("/quick-recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void GIVEN_recipe_name_with_special_characters_WHEN_findOrGenerate_THEN_return_recipe_response() throws Exception {
        // Given
        String recipeName = "Pollo al Limón con Arroz";
        Recipe recipe = RecipeFactory.create();
        recipe.setName(recipeName);
        
        QuickRecipeRequest request = QuickRecipeRequest.builder()
                .recipeName(recipeName)
                .build();

        when(findOrGenerateRecipeCommand.execute(any())).thenReturn(recipe);

        // When & Then
        mockMvc.perform(post("/quick-recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(recipeName));
    }

}
