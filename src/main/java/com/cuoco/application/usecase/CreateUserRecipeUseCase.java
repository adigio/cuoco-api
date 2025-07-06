package com.cuoco.application.usecase;


import com.cuoco.adapter.exception.ConflictException;
import com.cuoco.application.port.in.CreateUserRecipeCommand;
import com.cuoco.application.port.out.CreateUserRecipeRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.ExistsUserRecipeByUserIdAndRecipeIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserRecipe;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateUserRecipeUseCase implements CreateUserRecipeCommand {

    private final UserDomainService userDomainService;
    private final CreateUserRecipeRepository createUserRecipeRepository;
    private final ExistsUserRecipeByUserIdAndRecipeIdRepository existsUserRecipeByUserIdAndRecipeIdRepository;
    private final GetRecipeByIdRepository getRecipeByIdRepository;

    public CreateUserRecipeUseCase(
            UserDomainService userDomainService,
            CreateUserRecipeRepository createUserRecipeRepository,
            ExistsUserRecipeByUserIdAndRecipeIdRepository existsUserRecipeByUserIdAndRecipeIdRepository,
            GetRecipeByIdRepository getRecipeByIdRepository
    ) {
        this.userDomainService = userDomainService;
        this.createUserRecipeRepository = createUserRecipeRepository;
        this.existsUserRecipeByUserIdAndRecipeIdRepository = existsUserRecipeByUserIdAndRecipeIdRepository;
        this.getRecipeByIdRepository = getRecipeByIdRepository;
    }

    @Override
    public void execute(Command command) {
        log.info("Executing create user recipe use case with command {}", command);

        User user = userDomainService.getCurrentUser();

        Recipe recipe = getRecipeByIdRepository.execute(command.getRecipeId());

        UserRecipe userRecipe = buildUserRecipe(user, recipe);

        if(existsUserRecipeByUserIdAndRecipeIdRepository.execute(userRecipe)){
            log.info("Recipe already saved by user with ID {} ", userRecipe.getUser().getId());
            throw new ConflictException(ErrorDescription.DUPLICATED.getValue());
        }

        createUserRecipeRepository.execute(userRecipe);
    }

    private UserRecipe buildUserRecipe(User user, Recipe recipe) {
        return UserRecipe.builder()
                .user(user)
                .recipe(recipe)
                .build();
    }
}
