package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.Step;
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
public class StepResponse {
    private Long id;
    private Integer stepNumber;
    private String title;
    private String description;
    private String time;
    private String imageName;

    public static StepResponse fromDomain(Step domain) {
        return StepResponse.builder()
                .id(domain.getId())
                .stepNumber(domain.getNumber())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .time(domain.getTime())
                .imageName(domain.getImageName())
                .build();
    }
} 