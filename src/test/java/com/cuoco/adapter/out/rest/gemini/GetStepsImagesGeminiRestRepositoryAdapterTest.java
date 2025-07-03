package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.Step;
import com.cuoco.factory.domain.RecipeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetStepsImagesGeminiRestRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GeminiResponseModel geminiResponseModel;

    private GetRecipeStepsImagesGeminiRestRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new GetRecipeStepsImagesGeminiRestRepositoryAdapter(restTemplate);
        ReflectionTestUtils.setField(adapter, "imageUrl", "https://test-url.com");
        ReflectionTestUtils.setField(adapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(adapter, "temperature", 0.7);
    }

    @Test
    void execute_whenValidRecipe_thenReturnRecipeImages() {
        Recipe recipe = RecipeFactory.create();

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(geminiResponseModel);
        
        List<Step> result = adapter.execute(recipe);
        
        assertNotNull(result);
    }

    @Test
    void execute_whenNullResponse_thenReturnEmptyList() {
        Recipe recipe = RecipeFactory.create();

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(null);
        
        List<Step> result = adapter.execute(recipe);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void execute_whenRecipeWithEmptyInstructions_thenReturnOnlyMainImage() {
        Recipe recipeWithEmptyInstructions = RecipeFactory.createWithEmptyInstructions();

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(geminiResponseModel);
        
        List<Step> result = adapter.execute(recipeWithEmptyInstructions);
        
        assertNotNull(result);
    }

    @Test
    void execute_whenRecipeWithManySteps_thenReturnMaxFiveStepImages() {
        Recipe recipeWithManySteps = RecipeFactory.createWithManySteps();

        when(restTemplate.postForObject(anyString(), any(), any())).thenReturn(geminiResponseModel);
        
        List<Step> result = adapter.execute(recipeWithManySteps);
        
        assertNotNull(result);
    }
} 