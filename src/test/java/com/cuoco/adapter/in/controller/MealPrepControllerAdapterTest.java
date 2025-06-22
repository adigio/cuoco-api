package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.MealPrepRequest;
import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.factory.domain.MealPrepFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MealPrepControllerAdapter.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class MealPrepControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GetMealPrepFromIngredientsCommand getMealPrepFromIngredientsCommand;

    @MockitoBean
    private AuthenticateUserCommand authenticateUserCommand;

    @Test
    void GIVEN_valid_mealprep_request_WHEN_generate_THEN_return_mealpreps_response() throws Exception {
        MealPrep mealPrep = MealPrepFactory.create();
        MealPrepRequest request = MealPrepFactory.getMealPrepRequest();

        when(getMealPrepFromIngredientsCommand.execute(any())).thenReturn(List.of(mealPrep));

        mockMvc.perform(post("/meal-preps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(mealPrep.getName()))
                .andExpect(jsonPath("$[0].preparation_time").value(mealPrep.getPreparationTime()))
                .andExpect(jsonPath("$[0].subtitle").value(mealPrep.getSubtitle()))
                .andExpect(jsonPath("$[0].recipes[0]").value(mealPrep.getRecipes().get(0)))
                .andExpect(jsonPath("$[0].ingredients[0].name").value(mealPrep.getIngredients().get(0).getName()))
                .andExpect(jsonPath("$[0].instructions[0].title").value(mealPrep.getInstructions().get(0).getTitle()));
    }
}