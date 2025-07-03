package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealType implements Parametric {
    private Integer id;
    private String description;
}
