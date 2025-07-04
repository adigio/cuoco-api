package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllUserRecipesQuery;
import com.cuoco.application.port.out.GetAllUserRecipesByUserIdRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllUserRecipesUseCase implements GetAllUserRecipesQuery {

    private GetAllUserRecipesByUserIdRepository getAllUserRecipesByUserIdRepository;

    public GetAllUserRecipesUseCase(GetAllUserRecipesByUserIdRepository getAllUserRecipesByUserIdRepository) {
        this.getAllUserRecipesByUserIdRepository = getAllUserRecipesByUserIdRepository;
    }

    @Override
    public List<Recipe> execute() {
        log.info("Executing get all user recipes use case");

        User user = getUser();

        List<UserRecipe> userRecipes = getAllUserRecipesByUserIdRepository.execute(user.getId());

        return userRecipes.stream().map(UserRecipe::getRecipe).toList();
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
