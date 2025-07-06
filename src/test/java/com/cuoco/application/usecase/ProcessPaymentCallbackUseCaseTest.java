package com.cuoco.application.usecase;

import com.cuoco.application.port.in.ProcessPaymentCallbackCommand;
import com.cuoco.application.port.out.UserProPlanPaymentPort;
import com.cuoco.application.usecase.model.PaymentResult;
import com.cuoco.application.usecase.model.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

class ProcessPaymentCallbackUseCaseTest {

    private ProcessPaymentCallbackUseCase useCase;
    private UserProPlanPaymentPort userProPlanPaymentPort;

    @BeforeEach
    void setUp() {
        userProPlanPaymentPort = mock(UserProPlanPaymentPort.class);
        useCase = new ProcessPaymentCallbackUseCase(userProPlanPaymentPort);
    }

    @Test
    void GIVEN_approved_payment_WHEN_execute_THEN_return_success_result_and_save_upgrade() {
        // Arrange
        ProcessPaymentCallbackCommand.Command command = ProcessPaymentCallbackCommand.Command.builder()
                .collectionId("12345")
                .collectionStatus("approved")
                .externalReference("CUOCO_PRO_UPGRADE_123_abc456")
                .paymentType("credit_card")
                .merchantOrderId("order_789")
                .preferenceId("pref_123")
                .build();

        // Act
        PaymentResult result = useCase.execute(command);

        // Assert
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getUserId()).isEqualTo(123L);

        // Verifica que se guardó el upgrade
        verify(userProPlanPaymentPort).saveProPlanPayment(any(Long.class), any(String.class), any(PaymentStatus.class));
    }

    @Test
    void GIVEN_rejected_payment_WHEN_execute_THEN_return_failure_result() {
        // Arrange
        ProcessPaymentCallbackCommand.Command command = ProcessPaymentCallbackCommand.Command.builder()
                .collectionId("12345")
                .collectionStatus("rejected")
                .externalReference("CUOCO_PRO_UPGRADE_456_def789")
                .paymentType("credit_card")
                .merchantOrderId("order_789")
                .preferenceId("pref_123")
                .build();

        // Act
        PaymentResult result = useCase.execute(command);

        // Assert
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.REJECTED);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getUserId()).isEqualTo(456L);
    }
} 