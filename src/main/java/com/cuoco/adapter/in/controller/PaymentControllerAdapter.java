package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.CreatePaymentRequest;
import com.cuoco.adapter.in.controller.model.PaymentPreferenceResponse;
import com.cuoco.application.port.in.CreatePaymentPreferenceCommand;
import com.cuoco.application.port.in.ProcessPaymentCallbackCommand;
import com.cuoco.application.usecase.model.PaymentPreference;
import com.cuoco.application.usecase.model.PaymentResult;
import com.cuoco.application.usecase.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequestMapping("/payments")
public class PaymentControllerAdapter {

    private final CreatePaymentPreferenceCommand createPaymentPreferenceCommand;
    private final ProcessPaymentCallbackCommand processPaymentCallbackCommand;

    public PaymentControllerAdapter(
            CreatePaymentPreferenceCommand createPaymentPreferenceCommand,
            ProcessPaymentCallbackCommand processPaymentCallbackCommand
    ) {
        this.createPaymentPreferenceCommand = createPaymentPreferenceCommand;
        this.processPaymentCallbackCommand = processPaymentCallbackCommand;
    }

    @PostMapping
    public ResponseEntity<PaymentPreferenceResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        log.info("Creating payment preference for plan upgrade");

        User user = getUser();
        
        PaymentPreference preference = createPaymentPreferenceCommand.execute(
                CreatePaymentPreferenceCommand.Command.builder()
                        .userId(user.getId())
                        .planId(request.getPlanId())
                        .build()
        );

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

    private User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
} 