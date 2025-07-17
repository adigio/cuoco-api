package com.cuoco.shared.config.mercadopago;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mercado-pago.branding")
public class MercadoPagoBrandingConfig {
    private String primaryColor;
    private String secondaryColor;
    private boolean showMercadoPagoBranding;
}
