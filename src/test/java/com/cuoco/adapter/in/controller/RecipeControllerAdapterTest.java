package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.RecipeRequest;
import com.cuoco.application.port.in.GenerateRecipeImagesCommand;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import com.cuoco.factory.domain.RecipeFactory;
import com.cuoco.factory.domain.RecipeImageFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecipeControllerAdapter.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class RecipeControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;

    @MockitoBean
    private GenerateRecipeImagesCommand generateRecipeImagesCommand;

    @Test
    void GIVEN_valid_ingredients_request_WHEN_generate_THEN_return_recipes_response_with_images() throws Exception {
        Recipe recipe = RecipeFactory.create();
        RecipeRequest request = RecipeFactory.getRecipeRequest();
        List<Step> generatedImages = List.of(
                RecipeImageFactory.createMainRecipeImage(),
                RecipeImageFactory.createStepRecipeImage()
        );

        when(getRecipesFromIngredientsCommand.execute(any())).thenReturn(List.of(recipe));
        when(generateRecipeImagesCommand.execute(any())).thenReturn(generatedImages);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(recipe.getName()))
                .andExpect(jsonPath("$[0].preparation_time").value(recipe.getPreparationTime()))
                .andExpect(jsonPath("$[0].image").value(recipe.getImage()))
                .andExpect(jsonPath("$[0].subtitle").value(recipe.getSubtitle()))
                .andExpect(jsonPath("$[0].description").value(recipe.getDescription()))
                .andExpect(jsonPath("$[0].ingredients[0].name").value(recipe.getIngredients().get(0).getName()))
                .andExpect(jsonPath("$[0].instructions").value(recipe.getInstructions()))
                .andExpect(jsonPath("$[0].generated_images").exists())
                .andExpect(jsonPath("$[0].generated_images.size()").value(2))
                .andExpect(jsonPath("$[0].generated_images[0].image_type").value("MAIN"))
                .andExpect(jsonPath("$[0].generated_images[1].image_type").value("STEP"));
    }

    @Test
    void GIVEN_valid_ingredients_request_WHEN_image_generation_fails_THEN_return_recipes_without_images() throws Exception {
        Recipe recipe = RecipeFactory.create();
        RecipeRequest request = RecipeFactory.getRecipeRequest();

        when(getRecipesFromIngredientsCommand.execute(any())).thenReturn(List.of(recipe));
        when(generateRecipeImagesCommand.execute(any())).thenThrow(new RuntimeException("Image generation failed"));

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(recipe.getName()))
                .andExpect(jsonPath("$[0].generated_images").exists())
                .andExpect(jsonPath("$[0].generated_images.size()").value(0));
    }
}
