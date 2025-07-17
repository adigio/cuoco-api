package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.UserPaymentResponse;
import com.cuoco.application.port.in.CreateUserPaymentCommand;
import com.cuoco.application.port.in.ProcessUserPaymentCommand;
import com.cuoco.application.usecase.model.UserPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class UserPaymentsControllerAdapter {

    private final CreateUserPaymentCommand createUserPaymentCommand;
    private final ProcessUserPaymentCommand processUserPaymentCommand;

    @PostMapping
    public ResponseEntity<UserPaymentResponse> init() {
        log.info("Executing POST for init user subscription payment");

        UserPayment userPayment = createUserPaymentCommand.execute();
        UserPaymentResponse response = UserPaymentResponse.fromDomain(userPayment);

        log.info("User subscription payment response: {}", response);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/webhook")
    public ResponseEntity<Void> processPayment(@RequestBody Map<String, Object> payload) {
        log.info("Executing POST for payment webhook: {}", payload);

        ProcessUserPaymentCommand.Command command = ProcessUserPaymentCommand.Command.builder()
                .payload(payload)
                .build();

        processUserPaymentCommand.execute(command);

        log.info("User payment webhook processed successfully");
        return ResponseEntity.ok().build();
    }
}
