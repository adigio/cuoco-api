package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GenerateRecipeImagesCommand;
import com.cuoco.application.port.out.GetRecipeStepsImagesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GenerateRecipeImagesUseCase implements GenerateRecipeImagesCommand {

    private final GetRecipeStepsImagesRepository getRecipeStepsImagesRepository;

    public GenerateRecipeImagesUseCase(GetRecipeStepsImagesRepository getRecipeStepsImagesRepository) {
        this.getRecipeStepsImagesRepository = getRecipeStepsImagesRepository;
    }

    @Override
    public List<RecipeImage> execute(Command command) {
        Recipe recipe = command.getRecipe();
        log.info("Executing recipe images generation for recipe: {}", recipe.getName());
        
        try {
            List<RecipeImage> generatedImages = getRecipeStepsImagesRepository.execute(recipe);
            
            if (generatedImages == null) {
                generatedImages = List.of();
            }
            
            log.info("Successfully generated {} images for recipe: {}", 
                    generatedImages.size(), recipe.getName());
            
            return generatedImages;
        } catch (Exception e) {
            log.error("Error generating images for recipe: {}", recipe.getName(), e);
            // Return empty list instead of throwing exception to not break recipe generation flow
            return List.of();
        }
    }
} 