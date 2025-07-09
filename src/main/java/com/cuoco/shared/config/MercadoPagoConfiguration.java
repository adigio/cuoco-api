package com.cuoco.shared.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
@ConfigurationProperties(prefix = "mercadopago")
public class MercadoPagoConfiguration {
    
    private String baseUrl;
    private String accessToken;
    private CallbackConfig callback;
    private PlanConfig plan;
    private BrandingConfig branding;
    
    @Data
    public static class CallbackConfig {
        private String baseUrl;
        private String successPath;
        private String pendingPath;
        private String failurePath;
    }
    
    @Data
    public static class PlanConfig {
        private ProPlanConfig pro;
        
        @Data
        public static class ProPlanConfig {
            private BigDecimal price;
            private String currency;
            private String title;
            private String description;
        }
    }
    
    @Data
    public static class BrandingConfig {
        private String primaryColor;
        private String secondaryColor;
        private boolean showMercadoPagoBranding;
    }
} 