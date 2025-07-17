package com.cuoco.adapter.out.mercadopago;

import com.cuoco.adapter.exception.UnauthorizedException;
import com.cuoco.application.port.out.ProcessUserPaymentRepository;
import com.cuoco.application.usecase.model.PaymentStatus;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.shared.config.mercadopago.MercadoPagoConfig;
import com.cuoco.shared.model.ErrorDescription;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProcessUserPaymentMercadoPagoRepositoryAdapter implements ProcessUserPaymentRepository {

    private final MercadoPagoConfig config;

    @Override
    public UserPayment execute(String paymentId) {

        com.mercadopago.MercadoPagoConfig.setAccessToken(config.getAccessToken());

        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));

            UserPayment response = UserPayment.builder()
                    .status(PaymentStatus.builder().description(payment.getStatus()).build())
                    .externalReference(payment.getExternalReference())
                    .build();

            log.info("Payment info received with status={} and external_reference={}", response.getStatus(), response.getExternalReference());

            return response;
        } catch (MPException | MPApiException e) {
            log.error("Error fetching payment details from MercadoPago", e);
            throw new UnauthorizedException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }
}
