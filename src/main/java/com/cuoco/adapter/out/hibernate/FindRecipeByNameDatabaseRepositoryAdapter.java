package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.CreateRecipeHibernateRepositoryAdapter;
import com.cuoco.application.port.out.FindRecipeByNameRepository;
import com.cuoco.application.usecase.model.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class FindRecipeByNameDatabaseRepositoryAdapter implements FindRecipeByNameRepository {

    private final CreateRecipeHibernateRepositoryAdapter createRecipeHibernateRepositoryAdapter;

    public FindRecipeByNameDatabaseRepositoryAdapter(
            CreateRecipeHibernateRepositoryAdapter createRecipeHibernateRepositoryAdapter
    ) {
        this.createRecipeHibernateRepositoryAdapter = createRecipeHibernateRepositoryAdapter;
    }

    @Override
    public Recipe execute(String recipeName) {
        log.info("Searching recipe in database by name: {}", recipeName);

        Optional<RecipeHibernateModel> recipeModel = createRecipeHibernateRepositoryAdapter.findByNameIgnoreCase(recipeName.trim());

        if (recipeModel.isPresent()) {
            log.info("Recipe found in database: {}", recipeModel.get().getName());
            return recipeModel.get().toDomain();
        }

        log.info("Cannot find recipe in database with name: {}", recipeName);
        return null;
    }
} 