package com.cuoco.infrastructure.repository.rest;

import com.cuoco.domain.port.repository.GetRecipesFromIngredientsRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetRecipesFromIngredientsGeminiRestRepository implements GetRecipesFromIngredientsRepository {

    @Override
    public String execute(List<String> ingredients) {
        return "";
    }
}
