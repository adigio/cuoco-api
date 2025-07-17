package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.utils.ImageUtils;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.RecipeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
        Recipe recipe = RecipeFactory.create();

        when(imageUtils.imageExists(any(), any())).thenReturn(false);
        when(imageUtils.buildPromptBody(any())).thenReturn(null);
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(GeminiResponseModel.builder().build());
        when(imageUtils.extractImageFromResponse(any())).thenReturn(null);

        boolean result = adapter.execute(recipe);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenGeminiResponseIsNull() {
        Recipe recipe = RecipeFactory.create();
        when(imageUtils.imageExists(any(), any())).thenReturn(false);
        when(imageUtils.buildPromptBody(any())).thenReturn(null);
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(null);

        assertThrows(Exception.class, () -> adapter.execute(recipe));
    }
} 