package com.cuoco.application.usecase;

import com.cuoco.application.exception.RecipeGenerationException;
import com.cuoco.application.port.in.FindOrGenerateRecipeCommand;
import com.cuoco.application.port.in.GetRecipesFromIngredientsCommand;
import com.cuoco.application.port.out.FindRecipeByNameRepository;
import com.cuoco.application.port.out.CreateRecipeRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeFilter;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FindOrGenerateRecipeUseCase implements FindOrGenerateRecipeCommand {

    private final FindRecipeByNameRepository findRecipeByNameRepository;
    private final GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand;
    private final CreateRecipeRepository createRecipeRepository;

    public FindOrGenerateRecipeUseCase(
            FindRecipeByNameRepository findRecipeByNameRepository,
            GetRecipesFromIngredientsCommand getRecipesFromIngredientsCommand,
            CreateRecipeRepository createRecipeRepository
    ) {
        this.findRecipeByNameRepository = findRecipeByNameRepository;
        this.getRecipesFromIngredientsCommand = getRecipesFromIngredientsCommand;
        this.createRecipeRepository = createRecipeRepository;
    }

    @Override
    public Recipe execute(Command command) {
        log.info("Executing find or generate recipe use case with name: {}", command.getRecipeName());

        // Primero intentamos buscar en la base de datos por nombre exacto
        Optional<Recipe> existingRecipe = findRecipeByNameRepository.execute(command.getRecipeName());
        
        if (existingRecipe.isPresent()) {
            log.info("Recipe found in database: {}", existingRecipe.get().getName());
            return existingRecipe.get();
        }

        log.info("Recipe not found in database. Generating new recipe for: {}", command.getRecipeName());
        
        // Si no encontramos la receta, generamos una nueva específica usando el nombre completo
        Ingredient recipeAsIngredient = Ingredient.builder()
                .name(command.getRecipeName())
                .build();

        GetRecipesFromIngredientsCommand.Command generateCommand = GetRecipesFromIngredientsCommand.Command.builder()
                .ingredients(List.of(recipeAsIngredient))  // Pasamos el nombre como ingrediente principal
                .filters(RecipeFilter.builder()
                        .maxRecipes(1)
                        .enable(false)
                        .build())
                .build();

        List<Recipe> generatedRecipes = getRecipesFromIngredientsCommand.execute(generateCommand);
        
        if (generatedRecipes.isEmpty()) {
            throw new RecipeGenerationException("Could not generate recipe for: " + command.getRecipeName());
        }

        // Tomamos la primera receta generada (que debería ser específica para nuestro request)
        Recipe generatedRecipe = generatedRecipes.get(0);
        
        // Aseguramos que el nombre sea exactamente lo que pidió el usuario
        generatedRecipe.setName(command.getRecipeName());

        // Guardamos la receta generada en la base de datos para futuras consultas
        Recipe savedRecipe = createRecipeRepository.execute(generatedRecipe);

        log.info("Successfully generated and saved new recipe: {}", savedRecipe.getName());
        return savedRecipe;
    }
} 