package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.exception.UnprocessableException;
import com.cuoco.adapter.out.rest.gemini.model.RecipeResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.CandidateGeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.application.usecase.model.ParametricData;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.factory.domain.ParametricDataFactory;
import com.cuoco.factory.gemini.RecipeResponseGeminiModelFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRecipeByNameGeminiRestRespositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;
    private CreateRecipeByNameGeminiRestRespositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        adapter = new CreateRecipeByNameGeminiRestRespositoryAdapter(objectMapper, restTemplate);
    }

    @Test
    void shouldCreateRecipeByNameSuccessfully() throws Exception {
        // Given
        String recipeName = "Pasta Carbonara";
        ParametricData parametricData = ParametricDataFactory.create();
        RecipeResponseGeminiModel expectedRecipe = RecipeResponseGeminiModelFactory.create();
        String expectedResponse = objectMapper.writeValueAsString(List.of(expectedRecipe));

        GeminiResponseModel geminiResponse = GeminiResponseModel.builder()
                .candidates(List.of(CandidateGeminiResponseModel.builder()
                        .content(ContentGeminiRequestModel.builder()
                                .parts(List.of(PartGeminiRequestModel.builder().text(expectedResponse).build()))
                                .build())
                        .build()))
                .build();

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponse);

        // When
        Recipe result = adapter.execute(recipeName, parametricData);

        // Then
        assertNotNull(result);
        assertEquals(expectedRecipe.toDomain().getName(), result.getName());
    }

    @Test
    void shouldThrowExceptionWhenGeminiResponseIsNull() {
        // Given
        String recipeName = "Pasta Carbonara";
        ParametricData parametricData = ParametricDataFactory.create();
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(null);

        // When & Then
        assertThrows(UnprocessableException.class, () -> adapter.execute(recipeName, parametricData));
    }

    @Test
    void shouldThrowExceptionWhenEmptyRecipeList() throws Exception {
        // Given
        String recipeName = "Pasta Carbonara";
        ParametricData parametricData = ParametricDataFactory.create();
        String emptyResponse = objectMapper.writeValueAsString(List.of());

        GeminiResponseModel geminiResponse = GeminiResponseModel.builder()
                .candidates(List.of(CandidateGeminiResponseModel.builder()
                        .content(ContentGeminiRequestModel.builder()
                                .parts(List.of(PartGeminiRequestModel.builder().text(emptyResponse).build()))
                                .build())
                        .build()))
                .build();

        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(geminiResponse);

        // When & Then
        assertThrows(UnprocessableException.class, () -> adapter.execute(recipeName, parametricData));
    }
} 