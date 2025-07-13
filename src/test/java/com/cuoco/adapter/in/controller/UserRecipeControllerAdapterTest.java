package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.CreateUserRecipeCommand;
import com.cuoco.application.port.in.DeleteUserRecipeCommand;
import com.cuoco.application.port.in.GetAllUserRecipesQuery;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserRecipeControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private CreateUserRecipeCommand createUserRecipeCommand;

    @Mock
    private GetAllUserRecipesQuery getAllUserRecipesQuery;

    @Mock
    private DeleteUserRecipeCommand deleteUserRecipeCommand;

    @InjectMocks
    private UserRecipeControllerAdapter userRecipeControllerAdapter;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userRecipeControllerAdapter).build();
    }

    @Test
    void GIVEN_valid_recipe_id_WHEN_save_recipe_THEN_return_created() throws Exception {
        Long recipeId = 1L;
        doNothing().when(createUserRecipeCommand).execute(any(CreateUserRecipeCommand.Command.class));

        mockMvc.perform(post("/users/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void GIVEN_user_has_recipes_WHEN_get_all_recipes_THEN_return_recipes_list() throws Exception {
        Recipe recipe1 = RecipeFactory.create();
        Recipe recipe2 = RecipeFactory.create();
        List<Recipe> recipes = List.of(recipe1, recipe2);

        when(getAllUserRecipesQuery.execute()).thenReturn(recipes);

        mockMvc.perform(get("/users/recipes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(recipe1.getId()))
                .andExpect(jsonPath("$[0].name").value(recipe1.getName()))
                .andExpect(jsonPath("$[1].id").value(recipe2.getId()))
                .andExpect(jsonPath("$[1].name").value(recipe2.getName()));
    }

    @Test
    void GIVEN_valid_recipe_id_WHEN_delete_recipe_THEN_return_no_content() throws Exception {
        Long recipeId = 1L;
        doNothing().when(deleteUserRecipeCommand).execute(any(DeleteUserRecipeCommand.Command.class));

        mockMvc.perform(delete("/users/recipes/{id}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
