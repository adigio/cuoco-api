package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Plan implements Parametric {
    private Integer id;
    private String description;

    private PlanConfiguration configuration;
}
