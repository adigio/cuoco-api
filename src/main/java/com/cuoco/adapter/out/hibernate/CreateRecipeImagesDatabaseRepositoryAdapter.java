package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeStepsHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeImagesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateRecipeImagesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CreateRecipeImagesDatabaseRepositoryAdapter implements CreateRecipeImagesRepository {

    private final CreateRecipeImagesHibernateRepositoryAdapter createRecipeImagesHibernateRepositoryAdapter;

    public CreateRecipeImagesDatabaseRepositoryAdapter(CreateRecipeImagesHibernateRepositoryAdapter createRecipeImagesHibernateRepositoryAdapter) {
        this.createRecipeImagesHibernateRepositoryAdapter = createRecipeImagesHibernateRepositoryAdapter;
    }

    @Override
    public List<Step> execute(Recipe recipe) {
        log.info("Executing recipe images creation in database for recipe with ID {} and with {} images", recipe.getId(), recipe.getImages().size());

        RecipeHibernateModel recipeHibernateModel = buildRecipeHibernateModel(recipe);

        List<RecipeStepsHibernateModel> recipeImages = recipe.getImages().stream()
                .map(recipeImage -> buildRecipeImagesHibernateModel(recipeHibernateModel, recipeImage))
                .toList();

        List<RecipeStepsHibernateModel> savedImages = createRecipeImagesHibernateRepositoryAdapter.saveAll(recipeImages);

        log.info("Successfully saved recipe images");

        return savedImages.stream().map(RecipeStepsHibernateModel::toDomain).toList();
    }

    private RecipeHibernateModel buildRecipeHibernateModel(Recipe recipe) {
        return RecipeHibernateModel.builder()
                .id(recipe.getId())
                .build();
    }

    private RecipeStepsHibernateModel buildRecipeImagesHibernateModel(RecipeHibernateModel recipe, Step step) {
        return RecipeStepsHibernateModel.builder()
                .recipe(recipe)
                .imageName(step.getImageName())
                .build();
    }
}
