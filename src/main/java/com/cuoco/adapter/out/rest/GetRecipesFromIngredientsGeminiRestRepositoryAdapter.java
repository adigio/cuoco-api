package com.cuoco.adapter.out.rest;

import com.cuoco.application.port.out.GetRecipesFromIngredientsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GetRecipesFromIngredientsGeminiRestRepositoryAdapter implements GetRecipesFromIngredientsRepository {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GetRecipesFromIngredientsGeminiRestRepositoryAdapter(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public String execute(List<String> ingredients) {
        return "";
    }
}
