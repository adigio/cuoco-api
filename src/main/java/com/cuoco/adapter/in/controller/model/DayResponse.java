package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.Day;
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
public class DayResponse {
    public Integer id;
    public String description;

    public static DayResponse fromDomain(Day day) {
        return DayResponse.builder()
                .id(day.getId())
                .description(day.getDescription())
                .build();
    }
}
