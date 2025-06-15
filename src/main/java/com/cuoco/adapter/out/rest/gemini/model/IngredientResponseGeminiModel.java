package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.Ingredient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientResponseGeminiModel {

    private String name;
    private Double quantity;
    private String unit;
    private Boolean optional;

    public Ingredient toDomain() {
        return Ingredient.builder()
                .name(name)
                .quantity(quantity)
                .unit(unit)
                .optional(optional)
                .build();
    }
}
