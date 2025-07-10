package com.cuoco.adapter.out.rest.mercadopago;

import com.cuoco.adapter.exception.BadRequestException;
import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.adapter.out.rest.mercadopago.model.BackUrlsRequestMercadoPagoModel;
import com.cuoco.adapter.out.rest.mercadopago.model.ItemRequestMercadoPagoModel;
import com.cuoco.adapter.out.rest.mercadopago.model.PreferenceRequestMercadoPagoModel;
import com.cuoco.adapter.out.rest.mercadopago.model.PreferenceResponseMercadoPagoModel;
import com.cuoco.application.port.out.CreatePaymentRepository;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.shared.config.MercadoPagoConfiguration;
import com.cuoco.shared.utils.Constants;
import com.cuoco.shared.utils.PaymentConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CreatePaymentMercadoPagoRepositoryAdapter implements CreatePaymentRepository {

    private final RestTemplate restTemplate;
    private final MercadoPagoConfiguration config;

    public CreatePaymentMercadoPagoRepositoryAdapter(RestTemplate restTemplate, MercadoPagoConfiguration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public UserPayment execute(Long userId, Integer planId) {
        log.info("Creating MercadoPago preference for user {} and plan {}", userId, planId);
        
        String externalReference = generateExternalReference(userId);
        PreferenceRequestMercadoPagoModel request = buildPreferenceRequest(externalReference);
        
        try {
            HttpHeaders headers = buildHeaders();
            HttpEntity<PreferenceRequestMercadoPagoModel> entity = new HttpEntity<>(request, headers);
            
            String url = config.getBaseUrl() + "/checkout/preferences";

            ResponseEntity<PreferenceResponseMercadoPagoModel> httpResponse = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    PreferenceResponseMercadoPagoModel.class
            );

            if (!httpResponse.hasBody()) {
                throw new BadRequestException("Error communicating with payment service");
            }

            PreferenceResponseMercadoPagoModel response = httpResponse.getBody();

            return buildResponse(userId, planId, response, externalReference);

        } catch (HttpClientErrorException e) {
            log.error("Failed to create payment in Mercado Pago API: ", e);
            throw new NotAvailableException("Error communicating with payment service");
        } catch (HttpServerErrorException e) {
            log.error("Internal error in Mercado Pago API: ", e);
            throw new NotAvailableException("Error communicating with payment service");
        } catch (RestClientException e) {
            log.error("Failed to create payment with MercadoPago API: ", e);
            throw new NotAvailableException("Error communicating with payment service");
        }
    }

    private String generateExternalReference(Long userId) {
        return PaymentConstants.EXTERNAL_REFERENCE_PREFIX.getStringValue()
                .concat(userId.toString())
                .concat(Constants.UNDERSCORE.getValue())
                .concat(UUID.randomUUID().toString().substring(0, 8));
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + config.getAccessToken());
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private PreferenceRequestMercadoPagoModel buildPreferenceRequest(String externalReference) {
        MercadoPagoConfiguration.PlanConfig.ProPlanConfig proPlan = config.getPlan().getPro();

        return PreferenceRequestMercadoPagoModel.builder()
                .items(List.of(buildItemRequestMercadoPagoModel(proPlan)))
                .backUrls(buildBackUrlsRequestMercadoPagoModel())
                .externalReference(externalReference)
                .build();
    }

    private BackUrlsRequestMercadoPagoModel buildBackUrlsRequestMercadoPagoModel() {
        return BackUrlsRequestMercadoPagoModel.builder()
                .success(config.getCallback().getBaseUrl() + config.getCallback().getSuccessPath())
                .pending(config.getCallback().getBaseUrl() + config.getCallback().getPendingPath())
                .failure(config.getCallback().getBaseUrl() + config.getCallback().getFailurePath())
                .build();
    }

    private static ItemRequestMercadoPagoModel buildItemRequestMercadoPagoModel(MercadoPagoConfiguration.PlanConfig.ProPlanConfig proPlan) {
        return ItemRequestMercadoPagoModel.builder()
                .title(proPlan.getTitle())
                .description(proPlan.getDescription())
                .quantity(PaymentConstants.PAYMENT_QUANTITY.getIntValue())
                .unitPrice(proPlan.getPrice())
                .currencyId(proPlan.getCurrency())
                .build();
    }

    private static UserPayment buildResponse(
            Long userId,
            Integer planId,
            PreferenceResponseMercadoPagoModel response,
            String externalReference
    ) {
        return UserPayment.builder()
                .preferenceId(response.getId())
                .checkoutUrl(response.getInitPoint())
                .externalReference(externalReference)
                .userId(userId)
                .planId(planId)
                .build();
    }
} 