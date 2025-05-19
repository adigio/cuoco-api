package com.cuoco.infrastructure.repository.rest;

import com.cuoco.domain.port.repository.GetRecipesFromIngredientsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Qualifier('getRecipesFromIngredientsProvider')
public class GetRecipesFromIngredientsGeminiRestRepository implements GetRecipesFromIngredientsRepository {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GetRecipesFromIngredientsGeminiRestRepository(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public String execute(List<String> ingredients) {
        return "";
    }
}
