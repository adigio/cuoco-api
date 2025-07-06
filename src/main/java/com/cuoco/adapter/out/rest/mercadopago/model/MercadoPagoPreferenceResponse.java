package com.cuoco.adapter.out.rest.mercadopago.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoPreferenceResponse {
    
    private String id;
    
    @JsonProperty("init_point")
    private String initPoint;
    
    @JsonProperty("external_reference")
    private String externalReference;
    
    private String status;
    
    @JsonProperty("date_created")
    private String dateCreated;
} 