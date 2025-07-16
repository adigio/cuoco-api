package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanConfiguration {

    private String title;
    private String description;
    private Integer quantity;
    private BigDecimal price;
    private String currency;

}
