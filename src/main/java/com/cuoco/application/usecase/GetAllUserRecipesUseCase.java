package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllUserRecipesQuery;
import com.cuoco.application.port.out.GetAllUserRecipesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GetAllUserRecipesUseCase implements GetAllUserRecipesQuery {

    private GetAllUserRecipesRepository getAllUserRecipesRepository;

    public GetAllUserRecipesUseCase(GetAllUserRecipesRepository getAllUserRecipesRepository) {
        this.getAllUserRecipesRepository = getAllUserRecipesRepository;
    }

    @Override
    public List<Recipe> execute() {
        log.info("Executing get all user recipes user case");

        User user = getUser();

        List<UserRecipe> userRecipes = getAllUserRecipesRepository.execute(user.getId());

        return userRecipes.stream().map(UserRecipe::getRecipe).toList();
    }

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
