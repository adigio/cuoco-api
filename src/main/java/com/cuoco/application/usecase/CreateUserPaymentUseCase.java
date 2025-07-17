package com.cuoco.application.usecase;

import com.cuoco.application.exception.BadRequestException;
import com.cuoco.application.port.in.CreateUserPaymentCommand;
import com.cuoco.application.port.out.CreateUserPaymentRepository;
import com.cuoco.application.port.out.GetPlanByIdRepository;
import com.cuoco.application.usecase.domainservice.UserDomainService;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.PlanConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateUserPaymentUseCase implements CreateUserPaymentCommand {

    private final UserDomainService userDomainService;
    private final GetPlanByIdRepository getPlanByIdRepository;
    private final CreateUserPaymentRepository createUserPaymentProvider;
    private final CreateUserPaymentRepository createUserPaymentRepository;

    public CreateUserPaymentUseCase(
            UserDomainService userDomainService,
            GetPlanByIdRepository getPlanByIdRepository,
            @Qualifier("provider") CreateUserPaymentRepository createUserPaymentProvider,
            @Qualifier("repository") CreateUserPaymentRepository createUserPaymentRepository
    ) {
        this.userDomainService = userDomainService;
        this.getPlanByIdRepository = getPlanByIdRepository;
        this.createUserPaymentProvider = createUserPaymentProvider;
        this.createUserPaymentRepository = createUserPaymentRepository;
    }

    @Override
    public UserPayment execute() {
        User user = userDomainService.getCurrentUser();

        if(user.getPlan().getId() == PlanConstants.PRO.getValue()) {
            log.info("User already has a PRO plan");
            throw new BadRequestException(ErrorDescription.USER_HAS_PRO_PLAN.getValue());
        }

        Plan plan = getPlanByIdRepository.execute(PlanConstants.PRO.getValue());

        UserPayment userPayment = createUserPaymentProvider.execute(buildUserPayment(user, plan));

        createUserPaymentRepository.execute(userPayment);

        return userPayment;
    }

    private UserPayment buildUserPayment(User user, Plan plan) {
        return UserPayment.builder()
                .user(user)
                .plan(plan)
                .build();
    }
}
