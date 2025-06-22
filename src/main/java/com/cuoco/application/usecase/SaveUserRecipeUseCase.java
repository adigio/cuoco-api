package com.cuoco.application.usecase;


import com.cuoco.application.port.in.SaveUserRecipeCommand;
import com.cuoco.application.port.out.FavRecipeRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.SavedRecipeExistByUsernameRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.UserRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaveUserRecipeUseCase implements SaveUserRecipeCommand {

    static final Logger log = LoggerFactory.getLogger(SaveUserRecipeUseCase.class);

    private final FavRecipeRepository favRecipeRepository;

    private final SavedRecipeExistByUsernameRepository savedRecipeExistByUsernameRepository;

    private final GetRecipeByIdRepository getRecipeByIdRepository;



    public SaveUserRecipeUseCase(FavRecipeRepository favRecipeRepository,
                                 SavedRecipeExistByUsernameRepository savedRecipeExistByUsernameRepository, GetRecipeByIdRepository getRecipeByIdRepository) {
        this.favRecipeRepository = favRecipeRepository;
        this.savedRecipeExistByUsernameRepository = savedRecipeExistByUsernameRepository;
        this.getRecipeByIdRepository = getRecipeByIdRepository;
    }

    @Override
    public Boolean execute(Command command) {

        UserRecipe userRecipe = buildUserRecipe(command);

        if(savedRecipeExistByUsernameRepository.execute(userRecipe)){
            log.info("Recipe already saved by user {} ", userRecipe.getUser().getName());
            return true;
        }

        try{
            favRecipeRepository.execute(userRecipe);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    private UserRecipe buildUserRecipe(Command command) {
        return new UserRecipe(null,command.getUser(),
                getRecipe(command.getRecipeId()),
                false);

    }

    private Recipe getRecipe(Long id) {
        return getRecipeByIdRepository.execute(id);
    }
}
