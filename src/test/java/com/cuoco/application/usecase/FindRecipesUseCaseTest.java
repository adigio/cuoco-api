package com.cuoco.application.usecase;

import com.cuoco.application.port.in.FindRecipesCommand;
import com.cuoco.application.port.out.FindRecipesByFiltersRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.SearchFilters;
import com.cuoco.factory.domain.RecipeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindRecipesUseCaseTest {

    private FindRecipesByFiltersRepository findRecipesByFiltersRepository;
    private FindRecipesUseCase useCase;

    @BeforeEach
    void setUp() {
        findRecipesByFiltersRepository = mock(FindRecipesByFiltersRepository.class);
        useCase = new FindRecipesUseCase(findRecipesByFiltersRepository);
    }

    @Test
    void GIVEN_null_size_and_random_WHEN_execute_THEN_use_default_values() {
        List<Recipe> expected = List.of(RecipeFactory.create());
        when(findRecipesByFiltersRepository.execute(any())).thenReturn(expected);
        FindRecipesCommand.Command command = FindRecipesCommand.Command.builder().build();
        List<Recipe> result = useCase.execute(command);
        assertEquals(expected, result);
        ArgumentCaptor<SearchFilters> captor = ArgumentCaptor.forClass(SearchFilters.class);
        verify(findRecipesByFiltersRepository).execute(captor.capture());
        assertEquals(5, captor.getValue().getSize());
        assertEquals(false, captor.getValue().getRandom());
    }

    @Test
    void GIVEN_non_null_size_and_random_WHEN_execute_THEN_use_provided_values() {
        List<Recipe> expected = List.of(RecipeFactory.create());
        when(findRecipesByFiltersRepository.execute(any())).thenReturn(expected);
        FindRecipesCommand.Command command = FindRecipesCommand.Command.builder().size(10).random(true).build();
        List<Recipe> result = useCase.execute(command);
        assertEquals(expected, result);
        ArgumentCaptor<SearchFilters> captor = ArgumentCaptor.forClass(SearchFilters.class);
        verify(findRecipesByFiltersRepository).execute(captor.capture());
        assertEquals(10, captor.getValue().getSize());
        assertEquals(true, captor.getValue().getRandom());
    }
} 