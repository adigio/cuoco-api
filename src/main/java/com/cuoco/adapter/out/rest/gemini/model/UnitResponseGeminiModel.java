package com.cuoco.adapter.out.rest.gemini.model;

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
public class UnitResponseGeminiModel {

    private Integer id;
    private String description;
    private String symbol;

    public Unit toDomain() {
        return Unit.builder()
                .id(id)
                .description(description)
                .symbol(symbol)
                .build();
    }
}
