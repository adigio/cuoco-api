package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.UserPaymentResponse;
import com.cuoco.application.port.in.CreateUserPaymentCommand;
import com.cuoco.application.usecase.model.UserPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class UserPaymentsControllerAdapter {

    private final CreateUserPaymentCommand createUserPaymentCommand;

    @PostMapping()
    public ResponseEntity<UserPaymentResponse> subscribe() {
        log.info("Executing POST for user subscription payment");

        UserPayment userPayment = createUserPaymentCommand.execute();
        UserPaymentResponse response = UserPaymentResponse.fromDomain(userPayment);

        log.info("User subscription payment response: {}", response);
        return ResponseEntity.ok(response);
    }


}
