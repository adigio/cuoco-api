package com.cuoco.shared.config.mercadopago;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mercado-pago.callbacks")
public class MercadoPagoCallbacksConfig {
    private String success;
    private String pending;
    private String failure;
}

