package com.cuoco.adapter.out.rest.mercadopago;

import com.cuoco.adapter.out.rest.mercadopago.model.MercadoPagoPreferenceRequest;
import com.cuoco.adapter.out.rest.mercadopago.model.MercadoPagoPreferenceResponse;
import com.cuoco.application.exception.BusinessException;
import com.cuoco.application.port.out.PaymentServicePort;
import com.cuoco.application.usecase.model.MessageError;
import com.cuoco.application.usecase.model.PaymentPreference;
import com.cuoco.shared.config.MercadoPagoConfiguration;
import com.cuoco.shared.utils.PaymentConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class MercadoPagoPaymentAdapter implements PaymentServicePort {

    private final RestTemplate restTemplate;
    private final MercadoPagoConfiguration config;

    public MercadoPagoPaymentAdapter(RestTemplate restTemplate, MercadoPagoConfiguration config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public PaymentPreference createPreference(Long userId, Integer planId) {
        log.info("Creating MercadoPago preference for user {} and plan {}", userId, planId);

        validatePlanId(planId);
        
        String externalReference = generateExternalReference(userId);
        MercadoPagoPreferenceRequest request = buildPreferenceRequest(externalReference);
        
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<MercadoPagoPreferenceRequest> entity = new HttpEntity<>(request, headers);
            
            String url = config.getBaseUrl() + "/checkout/preferences";
            ResponseEntity<MercadoPagoPreferenceResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, MercadoPagoPreferenceResponse.class
            );
            
            MercadoPagoPreferenceResponse mpResponse = response.getBody();
            
            return PaymentPreference.builder()
                    .preferenceId(mpResponse.getId())
                    .checkoutUrl(mpResponse.getInitPoint())
                    .externalReference(externalReference)
                    .userId(userId)
                    .planId(planId)
                    .build();
                    
        } catch (RestClientException e) {
            log.error("Error communicating with MercadoPago API", e);
            throw new BusinessException("Error communicating with payment service", List.of());
        }
    }

    private void validatePlanId(Integer planId) {
        if (planId == null || planId != 2) { // Solo plan PRO
            throw new BusinessException("Invalid plan ID. Only PRO plan (id=2) is supported.", List.of());
        }
    }

    private String generateExternalReference(Long userId) {
        return PaymentConstants.EXTERNAL_REFERENCE_PREFIX.getStringValue() + 
               userId + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private MercadoPagoPreferenceRequest buildPreferenceRequest(String externalReference) {
        MercadoPagoConfiguration.PlanConfig.ProPlanConfig proPlan = config.getPlan().getPro();
        
        MercadoPagoPreferenceRequest.ItemRequest item = MercadoPagoPreferenceRequest.ItemRequest.builder()
                .title(proPlan.getTitle())
                .description(proPlan.getDescription())
                .quantity(PaymentConstants.PAYMENT_QUANTITY.getIntValue())
                .unitPrice(proPlan.getPrice())
                .currencyId(proPlan.getCurrency())
                .build();

        MercadoPagoPreferenceRequest.BackUrlsRequest backUrls = MercadoPagoPreferenceRequest.BackUrlsRequest.builder()
                .success(config.getCallback().getBaseUrl() + config.getCallback().getSuccessPath())
                .pending(config.getCallback().getBaseUrl() + config.getCallback().getPendingPath())
                .failure(config.getCallback().getBaseUrl() + config.getCallback().getFailurePath())
                .build();

        return MercadoPagoPreferenceRequest.builder()
                .items(List.of(item))
                .backUrls(backUrls)
                .externalReference(externalReference)
                .build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + config.getAccessToken());
        headers.set("Content-Type", "application/json");
        return headers;
    }
} 