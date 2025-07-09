package com.cuoco.adapter.out.rest.mercadopago;

import com.cuoco.adapter.out.rest.mercadopago.model.MercadoPagoPreferenceResponse;
import com.cuoco.application.exception.BusinessException;
import com.cuoco.application.usecase.model.PaymentPreference;
import com.cuoco.shared.config.MercadoPagoConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class MercadoPagoPaymentAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MercadoPagoConfiguration mercadoPagoConfig;

    private MercadoPagoPaymentAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup config mocks
        MercadoPagoConfiguration.CallbackConfig callbackConfig = new MercadoPagoConfiguration.CallbackConfig();
        callbackConfig.setBaseUrl("https://test.com");
        callbackConfig.setSuccessPath("/success");
        callbackConfig.setPendingPath("/pending");
        callbackConfig.setFailurePath("/failure");
        
        MercadoPagoConfiguration.PlanConfig.ProPlanConfig proPlan = new MercadoPagoConfiguration.PlanConfig.ProPlanConfig();
        proPlan.setPrice(new BigDecimal("500.00"));
        proPlan.setCurrency("ARS");
        proPlan.setTitle("Cuoco PRO");
        proPlan.setDescription("Premium plan");
        
        MercadoPagoConfiguration.PlanConfig planConfig = new MercadoPagoConfiguration.PlanConfig();
        planConfig.setPro(proPlan);
        
        when(mercadoPagoConfig.getBaseUrl()).thenReturn("https://api.mercadopago.com");
        when(mercadoPagoConfig.getAccessToken()).thenReturn("test_token");
        when(mercadoPagoConfig.getCallback()).thenReturn(callbackConfig);
        when(mercadoPagoConfig.getPlan()).thenReturn(planConfig);
        
        adapter = new MercadoPagoPaymentAdapter(restTemplate, mercadoPagoConfig);
    }

    @Test
    void GIVEN_valid_request_WHEN_createPreference_THEN_return_payment_preference() {
        // Arrange
        MercadoPagoPreferenceResponse mockResponse = MercadoPagoPreferenceResponse.builder()
                .id("pref_123456")
                .initPoint("https://checkout.mercadopago.com/pref_123456")
                .externalReference("CUOCO_PRO_UPGRADE_1_abc123")
                .status("active")
                .build();

        ResponseEntity<MercadoPagoPreferenceResponse> responseEntity = 
                new ResponseEntity<>(mockResponse, HttpStatus.CREATED);

        when(restTemplate.exchange(anyString(), any(), any(), eq(MercadoPagoPreferenceResponse.class)))
                .thenReturn(responseEntity);

        // Act
        PaymentPreference result = adapter.createPreference(1L, 2);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPreferenceId()).isEqualTo("pref_123456");
        assertThat(result.getCheckoutUrl()).isEqualTo("https://checkout.mercadopago.com/pref_123456");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getPlanId()).isEqualTo(2);
    }

    @Test
    void GIVEN_invalid_plan_id_WHEN_createPreference_THEN_throw_business_exception() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                adapter.createPreference(1L, 1) // Plan inválido
        );

        assertThat(exception.getMessage()).isEqualTo("Invalid plan ID. Only PRO plan (id=2) is supported.");
    }

    @Test
    void GIVEN_rest_client_exception_WHEN_createPreference_THEN_throw_business_exception() {
        // Arrange
        when(restTemplate.exchange(anyString(), any(), any(), eq(MercadoPagoPreferenceResponse.class)))
                .thenThrow(new RestClientException("Network error"));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () ->
                adapter.createPreference(1L, 2)
        );

        assertThat(exception.getMessage()).isEqualTo("Error communicating with payment service");
    }
} 