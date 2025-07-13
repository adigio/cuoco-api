package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetRecipeByIdHibernateRepositoryAdapter;
import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.hibernate.RecipeHibernateModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetRecipeByIdDatabaseRepositoryAdapterTest {

    @Mock
    private GetRecipeByIdHibernateRepositoryAdapter hibernateRepository;

    @InjectMocks
    private GetRecipeByIdDatabaseRepositoryAdapter adapter;

    @Test
    public void shouldReturnRecipeWhenFound() {
        long recipeId = 123L;
        RecipeHibernateModel mockEntity = RecipeHibernateModelFactory.create();
        Recipe expectedRecipe = mockEntity.toDomain();

        when(hibernateRepository.findById(recipeId)).thenReturn(Optional.of(mockEntity));

        Recipe result = adapter.execute(recipeId);

        assertNotNull(result);
        assertEquals(expectedRecipe.getId(), result.getId());
    }

    @Test
    public void shouldThrowExceptionIfRecipeNotFound() {
        long recipeId = 456L;
        when(hibernateRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> adapter.execute(recipeId));
    }
}
