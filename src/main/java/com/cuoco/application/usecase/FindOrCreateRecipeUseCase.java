package com.cuoco.application.usecase;

import com.cuoco.application.exception.RecipeGenerationException;
import com.cuoco.application.port.in.FindOrCreateRecipeCommand;
import com.cuoco.application.port.out.CreateRecipeByNameRepository;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.port.out.FindRecipeByNameRepository;
import com.cuoco.application.usecase.domainservice.RecipeDomainService;
import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.shared.model.ErrorDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FindOrCreateRecipeUseCase implements FindOrCreateRecipeCommand {

    private final CreateRecipeByNameRepository createRecipeByNameRepository;
    private final FindRecipeByNameRepository findRecipeByNameRepository;
    private final CreateRecipeRepository createRecipeRepository;
    private final RecipeDomainService recipeDomainService;

    public FindOrCreateRecipeUseCase(
            CreateRecipeByNameRepository createRecipeByNameRepository,
            FindRecipeByNameRepository findRecipeByNameRepository,
            CreateRecipeRepository createRecipeRepository,
            RecipeDomainService recipeDomainService
    ) {
        this.createRecipeByNameRepository = createRecipeByNameRepository;
        this.findRecipeByNameRepository = findRecipeByNameRepository;
        this.createRecipeRepository = createRecipeRepository;
        this.recipeDomainService = recipeDomainService;
    }

    @Override
    public Recipe execute(Command command) {
        log.info("Executing find or generate recipe use case with name: {}", command.getRecipeName());

        Recipe existingRecipe = findRecipeByNameRepository.execute(command.getRecipeName());
        
        if (existingRecipe != null) {
            log.info("Recipe found in database: {}", existingRecipe.getName());
            return existingRecipe;
        }

        log.info("Recipe not found in database. Generating new recipe for: {}", command.getRecipeName());
        ParametricData parametricData = recipeDomainService.buildParametricData();
        Recipe generatedRecipe = createRecipeByNameRepository.execute(command.getRecipeName(), parametricData);
        
        if (generatedRecipe == null) {
            throw new RecipeGenerationException(ErrorDescription.CANNOT_GENERATE_RECIPE.getValue());
        }

        Recipe savedRecipe = createRecipeRepository.execute(generatedRecipe);

        log.info("Successfully generated and saved new recipe: {}", savedRecipe.getName());
        return savedRecipe;
    }
} 