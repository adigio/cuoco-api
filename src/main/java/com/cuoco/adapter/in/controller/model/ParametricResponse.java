package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.Parametric;
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
public class ParametricResponse {
    private Integer id;
    private String description;

    public static ParametricResponse fromDomain(Parametric domain) {
        return ParametricResponse.builder()
                .id(domain.getId())
                .description(domain.getDescription())
                .build();
    }
}
