package com.cuoco.application.usecase;

import com.cuoco.application.port.in.ProcessPaymentCallbackCommand;
import com.cuoco.application.port.out.UserProPlanPaymentPort;
import com.cuoco.application.usecase.model.PaymentResult;
import com.cuoco.application.usecase.model.PaymentStatus;
import com.cuoco.shared.utils.PaymentConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessPaymentCallbackUseCase implements ProcessPaymentCallbackCommand {

    private final UserProPlanPaymentPort proPlanPaymentPort;

    public ProcessPaymentCallbackUseCase(UserProPlanPaymentPort proPlanPaymentPort) {
        this.proPlanPaymentPort = proPlanPaymentPort;
    }

    @Override
    public PaymentResult execute(Command command) {
        log.info("Processing payment callback for collection_id: {}", command.getCollectionId());
        
        PaymentStatus status = mapCollectionStatusToPaymentStatus(command.getCollectionStatus());
        String message = getMessageForStatus(status);
        boolean success = status == PaymentStatus.APPROVED;

        // Si el pago fue aprobado, guardar el upgrade a PRO
        if (success) {
            Long userId = extractUserIdFromExternalReference(command.getExternalReference());
            if (userId != null) {
                proPlanPaymentPort.saveProPlanPayment(userId, command.getExternalReference(), status);
                log.info("Upgrade a PRO guardado para userId {} hasta dentro de 1 mes", userId);
            } else {
                log.warn("No se pudo extraer el userId del externalReference: {}", command.getExternalReference());
            }
        }
        
        log.info("Payment processed: status={}, success={}, external_reference={}", 
                status, success, command.getExternalReference());
        
        return PaymentResult.builder()
                .collectionId(command.getCollectionId())
                .status(status)
                .externalReference(command.getExternalReference())
                .message(message)
                .success(success)
                .build();
    }
    
    private Long extractUserIdFromExternalReference(String externalReference) {
        try {
            String[] parts = externalReference.split("_");
            return Long.parseLong(parts[3]);
        } catch (Exception e) {
            return null;
        }
    }
    
    private PaymentStatus mapCollectionStatusToPaymentStatus(String collectionStatus) {
        if (collectionStatus == null) {
            return PaymentStatus.UNKNOWN;
        }
        
        return switch (collectionStatus.toLowerCase()) {
            case "approved" -> PaymentStatus.APPROVED;
            case "pending" -> PaymentStatus.PENDING;
            case "rejected" -> PaymentStatus.REJECTED;
            case "cancelled" -> PaymentStatus.CANCELLED;
            case "in_process" -> PaymentStatus.IN_PROCESS;
            default -> PaymentStatus.UNKNOWN;
        };
    }
    
    private String getMessageForStatus(PaymentStatus status) {
        return switch (status) {
            case APPROVED -> PaymentConstants.PAYMENT_SUCCESS_MESSAGE.getStringValue();
            case PENDING, IN_PROCESS -> PaymentConstants.PAYMENT_PENDING_MESSAGE.getStringValue();
            case REJECTED, CANCELLED -> PaymentConstants.PAYMENT_FAILURE_MESSAGE.getStringValue();
            default -> "Estado de pago desconocido. Por favor contacta con soporte.";
        };
    }
} 