package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllUserRecipesByUserIdRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetUserRecipesUseCaseTest {

    private GetAllUserRecipesByUserIdRepository repository;
    private GetAllUserRecipesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(GetAllUserRecipesByUserIdRepository.class);
        useCase = new GetAllUserRecipesUseCase(repository);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();

    }

    @Test
    void shouldReturnUserRecipesWhenUserIsAuthenticated() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        List<UserRecipe> recipes = prepareUserRecipes();

        when(repository.execute(1L)).thenReturn(recipes);

        // Simular usuario autenticado
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, null)
        );

        // Act
        List<UserRecipe> result = useCase.execute();

        // Assert
        assertEquals(recipes, result);
        verify(repository).execute(1L);
    }

    private List<UserRecipe> prepareUserRecipes() {
        User user = new User();
        Recipe recipe = new Recipe();
        UserRecipe userRecipe = new UserRecipe();
        user.setId(1L);
        user.setName("Test User");
        recipe.setId(1L);
        recipe.setName("Pasta");
        userRecipe.setId(1L);
        userRecipe.setUser(user);
        userRecipe.setRecipe(recipe);

        User user2 = new User();
        Recipe recipe2 = new Recipe();
        UserRecipe userRecipe2 = new UserRecipe();
        user2.setId(1L);
        user2.setName("Test User");
        recipe2.setId(2L);
        recipe2.setName("Pasta");
        userRecipe2.setId(2L);
        userRecipe2.setUser(user2);
        userRecipe2.setRecipe(recipe2);
        return Arrays.asList(userRecipe, userRecipe2);
    }


}
