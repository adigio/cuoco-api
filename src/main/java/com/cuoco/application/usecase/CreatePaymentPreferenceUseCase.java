package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreatePaymentPreferenceCommand;
import com.cuoco.application.port.out.PaymentServicePort;
import com.cuoco.application.usecase.model.PaymentPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreatePaymentPreferenceUseCase implements CreatePaymentPreferenceCommand {

    private final PaymentServicePort paymentServicePort;

    public CreatePaymentPreferenceUseCase(PaymentServicePort paymentServicePort) {
        this.paymentServicePort = paymentServicePort;
    }

    @Override
    public PaymentPreference execute(Command command) {
        log.info("Creating payment preference for user {} and plan {}", command.getUserId(), command.getPlanId());
        
        validateCommand(command);
        
        return paymentServicePort.createPreference(command.getUserId(), command.getPlanId());
    }
    
    private void validateCommand(Command command) {
        if (command.getUserId() == null) {
            throw new BadRequestException("User ID is required");
        }
        if (command.getPlanId() == null) {
            throw new BadRequestException("Plan ID is required");
        }
    }
}
