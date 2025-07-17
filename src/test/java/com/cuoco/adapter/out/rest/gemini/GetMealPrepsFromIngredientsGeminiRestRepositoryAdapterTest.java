package com.cuoco.adapter.out.rest.gemini;

import com.cuoco.adapter.out.rest.gemini.model.MealPrepResponseGeminiModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.CandidateGeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.factory.domain.MealPrepFactory;
import com.cuoco.factory.gemini.MealPrepResponseGeminiModelFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMealPrepsFromIngredientsGeminiRestRepositoryAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;
    private GetMealPrepsFromIngredientsGeminiRestRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        adapter = new GetMealPrepsFromIngredientsGeminiRestRepositoryAdapter(objectMapper, restTemplate);
    }

    @Test
    void shouldGenerateMealPrepsSuccessfully() throws Exception {
        // Given
        MealPrep mealPrep = MealPrepFactory.create();
        List<MealPrepResponseGeminiModel> expectedMealPreps = List.of(MealPrepResponseGeminiModelFactory.create());
        String expectedResponse = objectMapper.writeValueAsString(expectedMealPreps);

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
        List<MealPrep> result = adapter.execute(mealPrep);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenGeminiResponseIsNull() {
        // Given
        MealPrep mealPrep = MealPrepFactory.create();
        when(restTemplate.postForObject(anyString(), any(), eq(GeminiResponseModel.class)))
                .thenReturn(null);

        // When & Then
        assertThrows(Exception.class, () -> adapter.execute(mealPrep));
    }
} 