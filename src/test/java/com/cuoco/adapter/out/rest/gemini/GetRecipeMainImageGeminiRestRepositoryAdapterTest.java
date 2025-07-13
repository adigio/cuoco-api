package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.CandidateGeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.utils.ImageUtils;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class GetRecipeMainImageGeminiRestRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ImageUtils imageUtils;

    private GetRecipeMainImageGeminiRestRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new GetRecipeMainImageGeminiRestRepositoryAdapter(restTemplate, imageUtils);
    }

    @Test
    void shouldGenerateRecipeMainImageSuccessfully() throws Exception {
        // Given
        Recipe recipe = RecipeFactory.create();

        when(imageUtils.imageExists(any(), any())).thenReturn(false);
        when(imageUtils.buildPromptBody(any())).thenReturn(null);
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(GeminiResponseModel.builder().build());
        when(imageUtils.extractImageFromResponse(any())).thenReturn(null);

        // When
        boolean result = adapter.execute(recipe);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenGeminiResponseIsNull() {
        // Given
        Recipe recipe = RecipeFactory.create();
        when(imageUtils.imageExists(any(), any())).thenReturn(false);
        when(imageUtils.buildPromptBody(any())).thenReturn(null);
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(null);

        // When & Then
        assertThrows(Exception.class, () -> adapter.execute(recipe));
    }
} 