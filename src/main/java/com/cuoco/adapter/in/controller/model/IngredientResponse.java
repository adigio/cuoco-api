package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.Ingredient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientResponse {

    private Long id;
    private String name;
    private Double quantity;
    private UnitResponse unit;
    private Boolean optional;
    private String source;
    private Boolean confirmed;

    public static IngredientResponse fromDomain(Ingredient domain) {
        return IngredientResponse.builder()
                .name(domain.getName())
                .quantity(domain.getQuantity())
                .unit(UnitResponse.fromDomain(domain.getUnit()))
                .build();
    }

}
