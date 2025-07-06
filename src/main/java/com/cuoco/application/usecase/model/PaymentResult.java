package com.cuoco.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentResult {
    private String collectionId;
    private PaymentStatus status;
    private String externalReference;
    private Long userId;
    private String message;
    private boolean success;
} 