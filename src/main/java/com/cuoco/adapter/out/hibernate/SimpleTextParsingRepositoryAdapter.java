package com.cuoco.adapter.out.hibernate;

import com.cuoco.application.port.out.GetIngredientsFromTextRepository;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class SimpleTextParsingRepositoryAdapter implements GetIngredientsFromTextRepository {

    @Override
    public List<Ingredient> execute(String text) {
        log.info("Processing text for ingredients: {}", text);

        if (text == null || text.trim().isEmpty()) {
            log.info("Empty text provided, returning empty list");
            return Collections.emptyList();
        }

        List<Ingredient> ingredients = Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(ingredient -> !ingredient.isEmpty())
                .map(ingredient -> Ingredient.builder()
                        .name(ingredient)
                        .source("text")
                        .confirmed(false)
                        .build())
                .collect(Collectors.toList());

        log.info("Successfully parsed {} ingredients from text", ingredients.size());

        return ingredients;
    }
}