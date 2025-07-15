package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MealPrepConfiguration {
    private Integer size;
    private List<MealPrep> notInclude;
}
