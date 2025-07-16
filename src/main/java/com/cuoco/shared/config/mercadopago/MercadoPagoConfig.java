package com.cuoco.shared.config.mercadopago;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mercado-pago")
public class MercadoPagoConfig {

    private String accessToken;
    private MercadoPagoCallbacksConfig callbacks;
    private MercadoPagoBrandingConfig branding;

}