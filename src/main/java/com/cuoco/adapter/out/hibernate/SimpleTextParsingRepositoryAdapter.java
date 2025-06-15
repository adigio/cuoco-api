package com.cuoco.adapter.out.hibernate;

import com.cuoco.application.port.out.GetIngredientsFromTextRepository;
import com.cuoco.application.usecase.model.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SimpleTextParsingRepositoryAdapter implements GetIngredientsFromTextRepository {

    static final Logger log = LoggerFactory.getLogger(SimpleTextParsingRepositoryAdapter.class);

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
                .map(ingredient -> new Ingredient(ingredient, "text", false))
                .collect(Collectors.toList());

        log.info("Successfully parsed {} ingredients from text", ingredients.size());

        return ingredients;
    }
}