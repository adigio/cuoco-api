package com.cuoco.adapter.in.controller.model;

import com.cuoco.application.usecase.model.UserPayment;
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
public class UserPaymentResponse {

    private String externalId;
    private String externalReference;
    private String checkoutUrl;

    public static UserPaymentResponse fromDomain(UserPayment userPayment) {
        return UserPaymentResponse.builder()
                .externalId(userPayment.getExternalId())
                .externalReference(userPayment.getExternalReference())
                .checkoutUrl(userPayment.getCheckoutUrl())
                .build();
    }
}
