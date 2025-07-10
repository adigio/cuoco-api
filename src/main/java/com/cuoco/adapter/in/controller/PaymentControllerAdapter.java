package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.CreatePaymentRequest;
import com.cuoco.adapter.in.controller.model.PaymentPreferenceResponse;
import com.cuoco.application.port.in.CreateUserPaymentCommand;
import com.cuoco.application.port.in.ProcessPaymentCallbackCommand;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.application.usecase.model.PaymentResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequestMapping("/payments")
public class PaymentControllerAdapter {

    private final CreateUserPaymentCommand createUserPaymentCommand;
    private final ProcessPaymentCallbackCommand processPaymentCallbackCommand;

    public PaymentControllerAdapter(
            CreateUserPaymentCommand createUserPaymentCommand,
            ProcessPaymentCallbackCommand processPaymentCallbackCommand
    ) {
        this.createUserPaymentCommand = createUserPaymentCommand;
        this.processPaymentCallbackCommand = processPaymentCallbackCommand;
    }

    @PostMapping
    public ResponseEntity<PaymentPreferenceResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        log.info("Executing POST for user payment");
        
        UserPayment preference = createUserPaymentCommand.execute(buildCreatePaymentCommand(request));

        PaymentPreferenceResponse response = PaymentPreferenceResponse.fromDomain(preference);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public RedirectView processPaymentCallback(
            @RequestParam("collection_id") String collectionId,
            @RequestParam("collection_status") String collectionStatus,
            @RequestParam("external_reference") String externalReference,
            @RequestParam("payment_type") String paymentType,
            @RequestParam("merchant_order_id") String merchantOrderId,
            @RequestParam("preference_id") String preferenceId
    ) {
        log.info("Processing payment callback for collection_id: {}", collectionId);

        PaymentResult result = processPaymentCallbackCommand.execute(
                ProcessPaymentCallbackCommand.Command.builder()
                        .collectionId(collectionId)
                        .collectionStatus(collectionStatus)
                        .externalReference(externalReference)
                        .paymentType(paymentType)
                        .merchantOrderId(merchantOrderId)
                        .preferenceId(preferenceId)
                        .build()
        );

        String redirectUrl = result.isSuccess() 
                ? "/payment/success?message=" + result.getMessage()
                : "/payment/failure?message=" + result.getMessage();

        return new RedirectView(redirectUrl);
    }

    private static CreateUserPaymentCommand.Command buildCreatePaymentCommand(CreatePaymentRequest request) {
        return CreateUserPaymentCommand.Command.builder()
                .planId(request.getPlanId())
                .build();
    }
} 