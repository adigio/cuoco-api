package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.UserPayment;
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
public class PaymentPreferenceResponse {
    
    private String preferenceId;
    private String checkoutUrl;
    private String externalReference;
    
    public static PaymentPreferenceResponse fromDomain(UserPayment userPayment) {
        return PaymentPreferenceResponse.builder()
                .preferenceId(userPayment.getPreferenceId())
                .checkoutUrl(userPayment.getCheckoutUrl())
                .externalReference(userPayment.getExternalReference())
                .build();
    }
} 