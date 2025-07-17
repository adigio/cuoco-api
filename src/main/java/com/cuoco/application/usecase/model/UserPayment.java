package com.cuoco.application.usecase.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPayment {

    private Long id;
    private User user;
    private Plan plan;
    private PaymentStatus status;
    private String externalId;
    private String externalReference;
    private String checkoutUrl;

}
