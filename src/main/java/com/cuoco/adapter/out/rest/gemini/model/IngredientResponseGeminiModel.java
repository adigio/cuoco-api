package com.cuoco.adapter.out.rest.gemini.model;

import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.Unit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientResponseGeminiModel {

    private String name;
    private Double quantity;
    private Unit unit;
    private Boolean optional;

    public Ingredient toDomain() {
        return Ingredient.builder()
                .name(name)
                .quantity(quantity)
                .unit(Unit.builder()
                        .id(unit.getId())
                        .description(unit.getDescription())
                        .symbol(unit.getSymbol())
                        .build()
                )
                .optional(optional)
                .build();
    }
}
