package com.cuoco.application.usecase;

import com.cuoco.application.port.out.UserProPlanPaymentPort;
import org.springframework.stereotype.Component;

@Component
public class IsUserProUseCase {
    private final UserProPlanPaymentPort proPlanPaymentPort;

    public IsUserProUseCase(UserProPlanPaymentPort proPlanPaymentPort) {
        this.proPlanPaymentPort = proPlanPaymentPort;
    }

    public boolean isUserPro(Long userId) {
        return proPlanPaymentPort.isUserPro(userId);
    }
} 