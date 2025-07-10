package com.cuoco.adapter.in.controller;

import com.cuoco.adapter.in.controller.model.CreatePaymentRequest;
import com.cuoco.adapter.in.controller.model.PaymentPreferenceResponse;
import com.cuoco.application.exception.BusinessException;
import com.cuoco.application.port.in.CreateUserPaymentCommand;
import com.cuoco.application.port.in.ProcessPaymentCallbackCommand;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.application.usecase.model.PaymentResult;
import com.cuoco.application.usecase.model.PaymentStatus;
import com.cuoco.application.usecase.model.User;
import com.cuoco.factory.domain.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.view.RedirectView;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class PaymentControllerAdapterTest {

    @Mock
    private CreateUserPaymentCommand createUserPaymentCommand;

    @Mock
    private ProcessPaymentCallbackCommand processPaymentCallbackCommand;


    private PaymentControllerAdapter controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User mockUser = UserFactory.create();
        Authentication mockAuth = mock(Authentication.class);
        SecurityContext mockSecurityContext = mock(SecurityContext.class);

        when(mockAuth.getPrincipal()).thenReturn(mockUser);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuth);
        SecurityContextHolder.setContext(mockSecurityContext);

        controller = new PaymentControllerAdapter(createUserPaymentCommand, processPaymentCallbackCommand);
    }

    @Test
    void GIVEN_valid_payment_request_WHEN_createPayment_THEN_return_payment_preference() {

        // Arrange
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .planId(2)
                .build();

        UserPayment mockPreference = UserPayment.builder()
                .preferenceId("test_preference_id")
                .checkoutUrl("https://test.checkout.url")
                .externalReference("test_ref")
                .userId(1L)
                .planId(2)
                .build();
        when(createUserPaymentCommand.execute(any())).thenReturn(mockPreference);

        // Act  
        ResponseEntity<PaymentPreferenceResponse> response = controller.createPayment(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void GIVEN_successful_payment_callback_WHEN_processCallback_THEN_redirect_to_success() {
        // Arrange
        ProcessPaymentCallbackCommand.Command command = ProcessPaymentCallbackCommand.Command.builder()
                .collectionId("12345")
                .collectionStatus("approved")
                .externalReference("CUOCO_PRO_UPGRADE_1_abc123")
                .paymentType("credit_card")
                .merchantOrderId("order_789")
                .preferenceId("pref_123")
                .build();

        PaymentResult mockResult = PaymentResult.builder()
                .collectionId("12345")
                .status(PaymentStatus.APPROVED)
                .externalReference("CUOCO_PRO_UPGRADE_1_abc123")
                .userId(1L)
                .message("¡Felicitaciones! Tu pago fue procesado exitosamente.")
                .success(true)
                .build();

        when(processPaymentCallbackCommand.execute(any())).thenReturn(mockResult);

        // Act
        RedirectView view = controller.processPaymentCallback(
                "12345", "approved", "CUOCO_PRO_UPGRADE_1_abc123", "credit_card", "order_789", "pref_123"
        );

        // Assert
        assertThat(view.getUrl()).contains("/payments/success");
    }

    @Test
    void GIVEN_failed_payment_callback_WHEN_processCallback_THEN_redirect_to_failure() {
        // Arrange
        ProcessPaymentCallbackCommand.Command command = ProcessPaymentCallbackCommand.Command.builder()
                .collectionId("12345")
                .collectionStatus("rejected")
                .externalReference("CUOCO_PRO_UPGRADE_1_abc123")
                .paymentType("credit_card")
                .merchantOrderId("order_789")
                .preferenceId("pref_123")
                .build();

        PaymentResult mockResult = PaymentResult.builder()
                .collectionId("12345")
                .status(PaymentStatus.REJECTED)
                .externalReference("CUOCO_PRO_UPGRADE_1_abc123")
                .userId(1L)
                .message("Hubo un problema procesando tu pago. Por favor intenta nuevamente.")
                .success(false)
                .build();

        when(processPaymentCallbackCommand.execute(any())).thenReturn(mockResult);

        // Act
        RedirectView view = controller.processPaymentCallback(
                "12345", "rejected", "CUOCO_PRO_UPGRADE_1_abc123", "credit_card", "order_789", "pref_123"
        );

        // Assert
        assertThat(view.getUrl()).contains("/payments/failure");
    }

    @Test
    void GIVEN_invalid_plan_id_WHEN_createPayment_THEN_throw_exception() {
        // Arrange
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .planId(99) // inválido
                .build();

        when(createUserPaymentCommand.execute(any()))
                .thenThrow(new BusinessException("Invalid plan ID. Only PRO plan (id=2) is supported.", null));

        // Act & Assert
        try {
            controller.createPayment(request);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertThat(ex.getMessage()).contains("Invalid plan ID");
        }
    }

}