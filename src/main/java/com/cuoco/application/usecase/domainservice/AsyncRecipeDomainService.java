package com.cuoco.application.usecase.domainservice;

import com.cuoco.application.port.out.GenerateRecipeMainImageRepository;
import com.cuoco.application.usecase.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AsyncRecipeDomainService {

    private final GenerateRecipeMainImageRepository generateRecipeMainImageRepository;

    public AsyncRecipeDomainService(GenerateRecipeMainImageRepository generateRecipeMainImageRepository) {
        this.generateRecipeMainImageRepository = generateRecipeMainImageRepository;
    }

    @Async
    public void generateMainImages(List<Recipe> recipes) {

        recipes.forEach(recipe -> {
            log.info("Executing async main image creation for new recipe with ID {}", recipe.getId());

            generateRecipeMainImageRepository.execute(recipe);
        });
    }
}
