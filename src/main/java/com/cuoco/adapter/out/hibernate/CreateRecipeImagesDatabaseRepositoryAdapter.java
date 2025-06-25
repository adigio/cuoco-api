package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeImagesHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeImagesHibernateRepositoryAdapter;
import com.cuoco.application.port.out.CreateRecipeImagesRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.RecipeImage;
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
    public List<RecipeImage> execute(Recipe recipe) {
        log.info("Executing recipe images creation in database for recipe with ID {} and with {} images", recipe.getId(), recipe.getImages().size());

        List<RecipeImagesHibernateModel> recipeImages = recipe.getImages().stream().map(this::buildRecipeImagesHibernateModel).toList();

        List<RecipeImagesHibernateModel> savedImages = createRecipeImagesHibernateRepositoryAdapter.saveAll(recipeImages);

        log.info("Successfully saved recipe images");

        return savedImages.stream().map(RecipeImagesHibernateModel::toDomain).toList();
    }

    private RecipeImagesHibernateModel buildRecipeImagesHibernateModel(RecipeImage recipeImage) {
        return RecipeImagesHibernateModel.builder()
                .imageType(recipeImage.getImageType())
                .imageName(recipeImage.getImageName())
                .imagePath(recipeImage.getImagePath())
                .stepNumber(recipeImage.getStepNumber())
                .stepDescription(recipeImage.getStepDescription())
                .imageUrl(recipeImage.getImageUrl())
                .build();
    }
}
