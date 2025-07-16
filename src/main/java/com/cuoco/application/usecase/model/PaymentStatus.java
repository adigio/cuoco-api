package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentStatus implements Parametric {
    private Integer id;
    private String description;
}
