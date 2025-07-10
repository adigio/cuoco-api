package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateUserPaymentCommand;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.port.out.CreatePaymentRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.PlanConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateUserPaymentUseCase implements CreateUserPaymentCommand {

    private final UserDomainService userDomainService;
    private final GetPlanByIdRepository getPlanByIdRepository;
    private final CreatePaymentRepository createPaymentRepository;

    public CreateUserPaymentUseCase(
            UserDomainService userDomainService,
            GetPlanByIdRepository getPlanByIdRepository,
            CreatePaymentRepository createPaymentRepository
    ) {
        this.userDomainService = userDomainService;
        this.getPlanByIdRepository = getPlanByIdRepository;
        this.createPaymentRepository = createPaymentRepository;
    }

    @Override
    public UserPayment execute(Command command) {
        User user = userDomainService.getCurrentUser();

        log.info("Executing create payment for user ID {} and plan ID {}", user.getId(), command.getPlanId());

        Plan plan = getPlanByIdRepository.execute(command.getPlanId());

        validatePlan(plan);

        return createPaymentRepository.execute(user.getId(), plan.getId());
    }

    private void validatePlan(Plan plan) {
        if (plan.getId() != PlanConstants.PRO.getValue()) {
            log.error("Plan id {} is not valid for payment", plan.getId());
            throw new BadRequestException(ErrorDescription.INVALID_PAYMENT_PLAN.getValue());
        }
    }

}
