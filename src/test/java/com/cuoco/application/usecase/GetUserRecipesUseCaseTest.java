package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllUserRecipesByUserIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import com.cuoco.factory.domain.RecipeFactory;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetUserRecipesUseCaseTest {

    private UserDomainService userDomainService;
    private GetAllUserRecipesByUserIdRepository repository;
    private GetAllUserRecipesUseCase useCase;

    @BeforeEach
    void setUp() {
        userDomainService = mock(UserDomainService.class);
        repository = mock(GetAllUserRecipesByUserIdRepository.class);
        useCase = new GetAllUserRecipesUseCase(userDomainService, repository);
    }

    @Test
    void shouldReturnUserRecipesWhenUserIsAuthenticated() {
        User user = UserFactory.create();
        List<UserRecipe> userRecipes = prepareUserRecipes();
        List<Recipe> expectedRecipes = userRecipes.stream().map(UserRecipe::getRecipe).toList();

        when(userDomainService.getCurrentUser()).thenReturn(user);
        when(repository.execute(user.getId())).thenReturn(userRecipes);

        List<Recipe> result = useCase.execute();

        assertEquals(expectedRecipes, result);
        verify(repository).execute(user.getId());
    }

    private List<UserRecipe> prepareUserRecipes() {
        User user = UserFactory.create();
        Recipe recipe1 = RecipeFactory.create();
        Recipe recipe2 = RecipeFactory.create();
        
        UserRecipe userRecipe1 = UserRecipe.builder()
                .user(user)
                .recipe(recipe1)
                .build();
        
        UserRecipe userRecipe2 = UserRecipe.builder()
                .user(user)
                .recipe(recipe2)
                .build();
        
        return Arrays.asList(userRecipe1, userRecipe2);
    }
}
