package com.cuoco.application.usecase;

import com.cuoco.application.port.in.ProcessUserPaymentCommand;
import com.cuoco.application.port.out.GetAllPaymentStatusRepository;
import com.cuoco.application.port.out.GetUserByIdRepository;
import com.cuoco.application.port.out.GetUserPaymentByExternalReferenceRepository;
import com.cuoco.application.port.out.ProcessUserPaymentRepository;
import com.cuoco.application.port.out.UpdateUserPaymentRepository;
import com.cuoco.application.port.out.UpdateUserRepository;
import com.cuoco.application.usecase.model.PaymentStatus;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.shared.utils.PaymentConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessUserPaymentUseCase implements ProcessUserPaymentCommand {

    private static final String PAYMENT = "payment";
    private static final String APPROVED = "approved";

    private final ProcessUserPaymentRepository processPaymentProvider;
    private final GetUserPaymentByExternalReferenceRepository getUserPaymentByExternalReferenceRepository;
    private final UpdateUserPaymentRepository updateUserPaymentRepository;
    private final UpdateUserRepository updateUserRepository;
    private final GetUserByIdRepository getUserByIdRepository;

    @Override
    public void execute(Command command) {
        log.info("Execute user process payment with command {}", command);

        String type = (String) command.getPayload().get("type");
        String paymentId = getPaymentId(command.getPayload());

        if (PAYMENT.equalsIgnoreCase(type) && (paymentId != null)) {
            UserPayment receivedPayment = processPaymentProvider.execute(paymentId);

            if (receivedPayment.getStatus().getDescription().equalsIgnoreCase(APPROVED)) {

                UserPayment userPayment = getUserPaymentByExternalReferenceRepository.execute(receivedPayment.getExternalReference());

                User user = getUserByIdRepository.execute(userPayment.getUser().getId());

                userPayment.setUser(user);

                userPayment.setStatus(PaymentStatus.builder().id(PaymentConstants.STATUS_APPROVED.getValue()).build());

                updateUserPlan(userPayment);

                updateUserPaymentRepository.execute(userPayment);
            }
        }
    }

    private void updateUserPlan(UserPayment userPayment) {
        User user = userPayment.getUser();
        user.setPlan(userPayment.getPlan());

        updateUserRepository.execute(user);
    }

    private String getPaymentId(Map<String, Object> payload) {
        if(payload.containsKey("data") && payload.get("data") != null) {
            return String.valueOf(((Map<?, ?>) payload.get("data")).get("id"));
        }

        return null;
    }
}
