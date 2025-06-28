package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.UserRecipesResponse;
import com.cuoco.application.port.in.GetAllUserRecipesQuery;
import com.cuoco.application.port.in.CreateUserRecipeCommand;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRecipeControllerAdapterTest {

    private CreateUserRecipeCommand createUserRecipeCommand;
    private GetAllUserRecipesQuery getAllUserRecipesQuery;
    private UserRecipeControllerAdapter userRecipeControllerAdapter;

    @BeforeEach
    public void setUp() {
        createUserRecipeCommand = mock(CreateUserRecipeCommand.class);
        getAllUserRecipesQuery =mock(GetAllUserRecipesQuery.class);
        userRecipeControllerAdapter = new UserRecipeControllerAdapter(createUserRecipeCommand, getAllUserRecipesQuery);
    }

    @Test
    public void saveRecipe_shouldReturnOk_whenSavedSuccessfully() throws Exception {
        // Arrange
        User user = new User();
        user.setName("testUser");
        setAuthentication(user);
        when(createUserRecipeCommand.execute(any(CreateUserRecipeCommand.Command.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = userRecipeControllerAdapter.save(123L);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(true, response.getBody());
    }

    @Test
    void testGetAll_returnsListOfUserRecipesResponse() {
        // Arrange
        UserRecipe userRecipe = new UserRecipe();
        userRecipe.setId(1L);
        User user = new User();
        user.setName("testUser");
        userRecipe.setUser(user);
        Recipe recipe = new Recipe();
        recipe.setName("Spaghetti");
        userRecipe.setRecipe(recipe);
        userRecipe.setFavorite(true);
        List<UserRecipe> recipes = new ArrayList<>();
        recipes.add(userRecipe);
        when(getAllUserRecipesQuery.execute()).thenReturn(recipes);

        // Act
        ResponseEntity<?> response = userRecipeControllerAdapter.getAll();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List<?>);

        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());

        Object first = body.get(0);
        assertTrue(first instanceof UserRecipesResponse);

        UserRecipesResponse result = (UserRecipesResponse) first;
        assertEquals(1L, result.getId());
        assertEquals("testUser", result.getUser().getName());
        assertEquals("Spaghetti", result.getRecipe().getName());
        assertTrue(result.isFavorite());
    }

    @Test
    void testGetAll_returnsEmptyListWhenNoFavorites() {
        // Arrange
        when(getAllUserRecipesQuery.execute()).thenReturn(List.of());

        // Act
        ResponseEntity<?> response = userRecipeControllerAdapter.getAll();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof List<?>);
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }

    // Utilidad para setear un usuario autenticado en el contexto de seguridad
    private void setAuthentication(User user) {
        TestingAuthenticationToken auth = new TestingAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
