package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetUserRecipeCommand;
import com.cuoco.application.port.out.GetUserRecipesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetUserRecipesUseCase implements GetUserRecipeCommand {

    static final Logger log = LoggerFactory.getLogger(GetUserRecipesUseCase.class);

    private GetUserRecipesRepository getUserRecipesRepository;

    public GetUserRecipesUseCase(GetUserRecipesRepository getUserRecipesRepository) {
        this.getUserRecipesRepository = getUserRecipesRepository;
    }

    @Override
    public List<UserRecipe> execute() {
        User user=null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User){
            user = (User) principal;
            log.info("User: {}", user.getName());
        }
        return getUserRecipesRepository.execute(user.getId());
    }
}
