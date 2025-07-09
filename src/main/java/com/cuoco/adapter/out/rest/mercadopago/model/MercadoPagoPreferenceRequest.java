package com.cuoco.adapter.out.rest.mercadopago.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoPreferenceRequest {
    
    private List<ItemRequest> items;
    
    @JsonProperty("back_urls")
    private BackUrlsRequest backUrls;
    
    @JsonProperty("external_reference")
    private String externalReference;
    
    @JsonProperty("payment_methods")
    private PaymentMethodsRequest paymentMethods;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemRequest {
        private String title;
        private String description;
        private Integer quantity;
        
        @JsonProperty("unit_price")
        private BigDecimal unitPrice;
        
        @JsonProperty("currency_id")
        private String currencyId;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BackUrlsRequest {
        private String success;
        private String pending;
        private String failure;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethodsRequest {
        
        @JsonProperty("excluded_payment_methods")
        private List<ExcludedPaymentMethod> excludedPaymentMethods;
        
        @JsonProperty("excluded_payment_types")
        private List<ExcludedPaymentType> excludedPaymentTypes;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ExcludedPaymentMethod {
            private String id;
        }
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ExcludedPaymentType {
            private String id;
        }
    }
} 