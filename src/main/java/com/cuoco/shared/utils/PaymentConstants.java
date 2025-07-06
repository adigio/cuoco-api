package com.cuoco.shared.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum PaymentConstants {
    
    // Plan pricing
    PRO_PLAN_PRICE(BigDecimal.valueOf(500.00)),
    PRO_PLAN_CURRENCY("ARS"),
    PRO_PLAN_TITLE("Cuoco PRO - Plan Premium"),
    PRO_PLAN_DESCRIPTION("Upgrade to Premium: Unlimited recipes, advanced filters, meal planning and more!"),
    
    // MercadoPago configuration
    PAYMENT_QUANTITY(1),
    PAYMENT_SUCCESS_MESSAGE("¡Felicitaciones! Tu pago fue procesado exitosamente."),
    PAYMENT_FAILURE_MESSAGE("Hubo un problema procesando tu pago. Por favor intenta nuevamente."),
    PAYMENT_PENDING_MESSAGE("Tu pago está siendo procesado. Te notificaremos cuando esté confirmado."),
    
    // External references
    EXTERNAL_REFERENCE_PREFIX("CUOCO_PRO_UPGRADE_"),
    
    // Callback URLs (relative paths)
    CALLBACK_SUCCESS_PATH("/payments/success"),
    CALLBACK_PENDING_PATH("/payments/pending"),
    CALLBACK_FAILURE_PATH("/payments/failure");
    
    private final Object value;
    
    public BigDecimal getDecimalValue() {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        throw new ClassCastException("Value is not a BigDecimal: " + value.getClass());
    }
    
    public String getStringValue() {
        return value.toString();
    }
    
    public Integer getIntValue() {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        throw new ClassCastException("Value is not an Integer: " + value.getClass());
    }
} 