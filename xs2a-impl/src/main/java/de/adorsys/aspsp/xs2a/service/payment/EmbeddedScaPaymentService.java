/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.aspsp.xs2a.service.payment;

import com.google.common.collect.Lists;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.CreatePisConsentResponse;
import de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus;
import de.adorsys.aspsp.xs2a.domain.consent.CreatePisConsentData;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayment;
import de.adorsys.aspsp.xs2a.domain.pis.TppInfo;
import de.adorsys.aspsp.xs2a.service.consent.PisConsentService;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus.RCVD;
import static de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus.RJCT;
import static de.adorsys.aspsp.xs2a.domain.consent.Xs2aAuthorisationStartType.IMPLICIT;

@Service
@RequiredArgsConstructor
public class EmbeddedScaPaymentService implements ScaPaymentService {
    private final ConsentSpi consentSpi;
    private final Xs2aPisConsentMapper pisConsentMapper;
    private final AspspProfileService profileService;
    private final PisAuthorizationService pisAuthorizationService;
    private final PaymentSpi paymentSpi;
    private final PaymentMapper paymentMapper;
    private final PisConsentService pisConsentService;

    @Override
    public PaymentInitialisationResponse createPeriodicPayment(PeriodicPayment payment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here
        CreatePisConsentData pisConsentData = new CreatePisConsentData(payment, tppInfo, paymentProduct, aspspConsentData);
        return createConsentForPeriodicPaymentAndExtendPaymentResponse(pisConsentData, new PaymentInitialisationResponse());
    }

    @Override
    public List<PaymentInitialisationResponse> createBulkPayment(List<SinglePayment> payments, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here
        Map<SinglePayment, PaymentInitialisationResponse> paymentMap = new HashMap<>();
        for (SinglePayment payment : payments) {
            paymentMap.put(payment, new PaymentInitialisationResponse());
        }
        CreatePisConsentData pisConsentData = new CreatePisConsentData(paymentMap, tppInfo, paymentProduct, aspspConsentData);
        return createConsentForBulkPaymentAndExtendPaymentResponses(pisConsentData);
    }

    @Override
    public PaymentInitialisationResponse createSinglePayment(SinglePayment payment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here
        SpiPaymentInitialisationResponse aspspResponse = paymentSpi.createPaymentInitiation(paymentMapper.mapToSpiSinglePayment(payment), aspspConsentData).getPayload();
        PaymentInitialisationResponse xs2aResponse = paymentMapper.mapToPaymentInitializationResponse(aspspResponse);
        if (RJCT == xs2aResponse.getTransactionStatus()){
            return xs2aResponse;
        }
            payment.setPaymentId(xs2aResponse.getPaymentId());
        CreatePisConsentData pisConsentData = new CreatePisConsentData(payment, tppInfo, paymentProduct, aspspConsentData);
        return createConsentForSinglePaymentAndExtendPaymentResponse(pisConsentData, xs2aResponse);
    }

    private PaymentInitialisationResponse createConsentForSinglePaymentAndExtendPaymentResponse(CreatePisConsentData createPisConsentData, PaymentInitialisationResponse response) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForSinglePayment(createPisConsentData, response.getPaymentId());

        return extendPaymentResponseFields(response, cmsResponse, PaymentType.SINGLE);
    }

    private PaymentInitialisationResponse createConsentForPeriodicPaymentAndExtendPaymentResponse(CreatePisConsentData createPisConsentData, PaymentInitialisationResponse response) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForPeriodicPayment(createPisConsentData, response.getPaymentId());
        return extendPaymentResponseFields(response, cmsResponse);
    }

    private List<PaymentInitialisationResponse> createConsentForBulkPaymentAndExtendPaymentResponses(CreatePisConsentData createPisConsentData) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForBulkPayment(createPisConsentData);

        List<PaymentInitialisationResponse> responses = Lists.newArrayList(createPisConsentData.getPaymentIdentifierMap().values());
        return responses.stream()
                   .map(resp -> extendPaymentResponseFields(resp, cmsResponse, PaymentType.BULK))
                   .collect(Collectors.toList());
    }

    private PaymentInitialisationResponse extendPaymentResponseFields(PaymentInitialisationResponse response, CreatePisConsentResponse cmsResponse, PaymentType paymentType) {
        Optional.ofNullable(cmsResponse)
            .filter(c -> StringUtils.isNoneBlank(c.getConsentId(), c.getPaymentId()))
            .ifPresent(c -> {
                response.setPaymentId(c.getPaymentId());
                response.setTransactionStatus(RCVD);
                response.setPisConsentId(c.getConsentId());
                response.setPaymentType(paymentType.name());
            });
        return IMPLICIT == profileService.getAuthorisationStartType()
                   ? createPisAuthorisationForImplicitApproach(response, paymentType)
                   : response;
    }

    private PaymentInitialisationResponse createPisAuthorisationForImplicitApproach(PaymentInitialisationResponse response, PaymentType paymentType) {
        pisAuthorizationService.createConsentAuthorisation(response.getPaymentId(), paymentType)
            .ifPresent(a -> {
                response.setAuthorizationId(a.getAuthorizationId());
                response.setScaStatus(a.getScaStatus());
                response.setLinks(a.getLinks());
            });
        return response;
    }
}
