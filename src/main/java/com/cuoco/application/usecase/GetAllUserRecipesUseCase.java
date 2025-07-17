package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetAllUserRecipesQuery;
import com.cuoco.application.port.out.GetAllUserRecipesByUserIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllUserRecipesUseCase implements GetAllUserRecipesQuery {

    private final UserDomainService userDomainService;
    private final GetAllUserRecipesByUserIdRepository getAllUserRecipesByUserIdRepository;

    public GetAllUserRecipesUseCase(
            UserDomainService userDomainService,
            GetAllUserRecipesByUserIdRepository getAllUserRecipesByUserIdRepository
    ) {
        this.userDomainService = userDomainService;
        this.getAllUserRecipesByUserIdRepository = getAllUserRecipesByUserIdRepository;
    }

    @Override
    public List<Recipe> execute() {
        log.info("Executing get all user recipes use case");

        User user = userDomainService.getCurrentUser();

        List<UserRecipe> userRecipes = getAllUserRecipesByUserIdRepository.execute(user.getId());

        return userRecipes.stream().map(UserRecipe::getRecipe).toList();
    }

}
