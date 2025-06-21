package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.GetRecipeFromIDRepositoryAdapter;
import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetRecipeByIDHibernateRepositoryAdapter;
import com.cuoco.application.usecase.model.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetRecipeFromIDRepositoryAdapterTest {

    private GetRecipeByIDHibernateRepositoryAdapter hibernateRepository;
    private GetRecipeFromIDRepositoryAdapter adapter;

    @BeforeEach
    public void setUp() {
        hibernateRepository = mock(GetRecipeByIDHibernateRepositoryAdapter.class);
        adapter = new GetRecipeFromIDRepositoryAdapter(hibernateRepository);
    }

    @Test
    public void shouldReturnRecipeWhenFound() {
        // Arrange
        long recipeId = 123L;

        // Simulamos una entidad que tiene el método toDomain()
        Recipe expectedRecipe = new Recipe();
        RecipeHibernateModel mockEntity = mock(RecipeHibernateModel.class); // reemplazar con la clase real, ej: RecipeEntity

        when(hibernateRepository.findById(recipeId)).thenReturn(Optional.of(mockEntity));
        when(mockEntity.toDomain()).thenReturn(expectedRecipe);

        // Act
        Recipe result = adapter.execute(recipeId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedRecipe, result);
    }

    @Test
    public void shouldThrowExceptionIfRecipeNotFound() {
        // Arrange
        long recipeId = 456L;
        when(hibernateRepository.findById(recipeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(java.util.NoSuchElementException.class, () -> adapter.execute(recipeId));
    }
}
