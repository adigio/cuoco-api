package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.Unit;
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
public class UnitResponse {
    private Integer id;
    private String description;
    private String symbol;

    public static UnitResponse fromDomain(Unit domain) {
        return UnitResponse.builder()
                .id(domain.getId())
                .description(domain.getDescription())
                .symbol(domain.getSymbol())
                .build();
    }
}
