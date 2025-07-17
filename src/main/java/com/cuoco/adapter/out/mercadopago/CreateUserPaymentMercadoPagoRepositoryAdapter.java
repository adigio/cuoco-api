package com.cuoco.adapter.out.mercadopago;

import com.cuoco.adapter.exception.NotAvailableException;
import com.cuoco.application.port.out.CreateUserPaymentRepository;
import com.cuoco.application.usecase.model.PaymentStatus;
import com.cuoco.application.usecase.model.Plan;
import com.cuoco.application.usecase.model.PlanConfiguration;
import com.cuoco.application.usecase.model.User;
import com.cuoco.application.usecase.model.UserPayment;
import com.cuoco.shared.config.mercadopago.MercadoPagoConfig;
import com.cuoco.shared.model.ErrorDescription;
import com.cuoco.shared.utils.Constants;
import com.cuoco.shared.utils.PaymentConstants;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Qualifier("provider")
@RequiredArgsConstructor
public class CreateUserPaymentMercadoPagoRepositoryAdapter implements CreateUserPaymentRepository {

    private static final String EXTERNAL_REFERENCE_PREFIX = "CUOCO_PRO_UPGRADE";

    @Value("${shared.base-url}")
    private String baseUrl;

    private final HttpServletRequest request;
    private final MercadoPagoConfig config;

    @Override
    public UserPayment execute(UserPayment userPayment) {

        com.mercadopago.MercadoPagoConfig.setAccessToken(config.getAccessToken());

        User user = userPayment.getUser();
        Plan plan = userPayment.getPlan();

        log.info("Executing MercadoPago preference payment for user ID {} and plan ID {}", user.getId(), plan.getId());

        try {
            PreferenceRequest request = buildPreference(userPayment);

            PreferenceClient client = new PreferenceClient();

            Preference preference = client.create(request);

            UserPayment response = buildResponse(userPayment, preference);

            log.info("Payment created with external ID {}", response.getExternalId());

            return response;

        } catch (MPException e) {
            log.error("Error while creating preference in MercadoPago SDK: {}", e.getMessage());
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        } catch (MPApiException e) {
            log.error("Failed to create preference payment with MercadoPago API: {}", e.getApiResponse());
            throw new NotAvailableException(ErrorDescription.NOT_AVAILABLE.getValue());
        }
    }

    private String generateExternalReference(Long userId) {
        return EXTERNAL_REFERENCE_PREFIX
                .concat(Constants.UNDERSCORE.getValue())
                .concat(userId.toString())
                .concat(Constants.UNDERSCORE.getValue())
                .concat(UUID.randomUUID().toString().substring(0, 8));
    }

    private PreferenceRequest buildPreference(UserPayment userPayment) {
        return PreferenceRequest.builder()
                .items(List.of(buildItems(userPayment.getPlan())))
                .externalReference(generateExternalReference(userPayment.getUser().getId()))
                .backUrls(buildBackUrls())
                .autoReturn("approved")
                .build();
    }

    private PreferenceBackUrlsRequest buildBackUrls() {
        log.info("Creating payment preference backs using URL {}", baseUrl);
        
        return PreferenceBackUrlsRequest.builder()
                .success(baseUrl + config.getCallbacks().getSuccess())
                .pending(baseUrl + config.getCallbacks().getPending())
                .failure(baseUrl + config.getCallbacks().getFailure())
                .build();
    }

    private PreferenceItemRequest buildItems(Plan plan) {
        PlanConfiguration planConfiguration = plan.getConfiguration();

        return PreferenceItemRequest.builder()
                .title(planConfiguration.getTitle())
                .description(planConfiguration.getDescription())
                .quantity(planConfiguration.getQuantity())
                .unitPrice(planConfiguration.getPrice())
                .currencyId(planConfiguration.getCurrency())
                .build();
    }

    private UserPayment buildResponse(UserPayment request, Preference preference) {
        return UserPayment.builder()
                .user(request.getUser())
                .plan(request.getPlan())
                .status(PaymentStatus.builder().id(PaymentConstants.STATUS_PENDING.getValue()).build())
                .externalId(preference.getId())
                .checkoutUrl(preference.getInitPoint())
                .externalReference(preference.getExternalReference())
                .build();

    }
}
