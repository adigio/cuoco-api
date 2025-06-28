package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.model.RecipeStepsImagesHibernateModel;
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

        List<RecipeStepsImagesHibernateModel> recipeImages = recipe.getImages().stream()
                .map(recipeImage -> buildRecipeImagesHibernateModel(recipeHibernateModel, recipeImage))
                .toList();

        List<RecipeStepsImagesHibernateModel> savedImages = createRecipeImagesHibernateRepositoryAdapter.saveAll(recipeImages);

        log.info("Successfully saved recipe images");

        return savedImages.stream().map(RecipeStepsImagesHibernateModel::toDomain).toList();
    }

    private RecipeHibernateModel buildRecipeHibernateModel(Recipe recipe) {
        return RecipeHibernateModel.builder()
                .id(recipe.getId())
                .build();
    }

    private RecipeStepsImagesHibernateModel buildRecipeImagesHibernateModel(RecipeHibernateModel recipe, Step step) {
        return RecipeStepsImagesHibernateModel.builder()
                .recipe(recipe)
                .imageName(step.getImageName())
                .build();
    }
}
