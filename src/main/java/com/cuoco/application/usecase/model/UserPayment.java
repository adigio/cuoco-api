package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserPayment {
    private Long userId;
    private Integer planId;
    private String preferenceId;
    private String checkoutUrl;
    private String externalReference;
} 