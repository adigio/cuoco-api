package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetRecipeByIdQuery;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.usecase.domainservice.RecipeDomainService;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetRecipeByIdUseCase implements GetRecipeByIdQuery {

    private final UserDomainService userDomainService;
    private final RecipeDomainService recipeDomainService;
    private final GetRecipeByIdRepository getRecipeByIdRepository;

    @Override
    public Recipe execute(Long id, Integer servings) {
        log.info("Executing get recipe by id use case with ID: {}", id);

        Recipe recipe = getRecipeByIdRepository.execute(id);

        isFavorite(recipe);

        recipeDomainService.adjustIngredientsByServings(recipe, servings);

        return recipe;
    }

    private void isFavorite(Recipe recipe) {
        User user = userDomainService.getCurrentUser();
        recipe.setFavorite(user.getRecipes().contains(recipe));
    }
}
