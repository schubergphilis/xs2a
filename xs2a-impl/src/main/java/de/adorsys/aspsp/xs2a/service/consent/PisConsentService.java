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

package de.adorsys.aspsp.xs2a.service.consent;

import de.adorsys.aspsp.xs2a.config.rest.consent.PisConsentRemoteUrls;
import de.adorsys.aspsp.xs2a.consent.api.CmsAspspConsentData;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.GetPisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.UpdatePisConsentPsuDataRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.UpdatePisConsentPsuDataResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.CreatePisConsentResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.PisConsentRequest;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.consent.CreatePisConsentData;
import de.adorsys.aspsp.xs2a.domain.consent.Xsa2CreatePisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.*;
import de.adorsys.aspsp.xs2a.service.authorization.pis.PisScaAuthorisationService;
import de.adorsys.aspsp.xs2a.service.mapper.consent.Xs2aPisConsentMapper;
import de.adorsys.aspsp.xs2a.spi.domain.SpiResponse;
import de.adorsys.aspsp.xs2a.spi.domain.authorisation.SpiAuthorisationStatus;
import de.adorsys.aspsp.xs2a.service.profile.AspspProfileService;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus.RCVD;
import static de.adorsys.aspsp.xs2a.domain.consent.Xs2aAuthorisationStartType.IMPLICIT;
import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.*;

@Service
@RequiredArgsConstructor
public class PisConsentService {
    @Qualifier("consentRestTemplate")
    private final RestTemplate consentRestTemplate;
    private final PisConsentRemoteUrls remotePisConsentUrls;
    private final Xs2aPisConsentMapper pisConsentMapper;
    private final AspspProfileService profileService;
    private final PisScaAuthorisationService pisScaAuthorisationService;

    public ResponseObject createPisConsent(Object payment, Object xs2aResponse, PaymentRequestParameters requestParameters, TppInfo tppInfo) {
        CreatePisConsentData consentData = getPisConsentData(payment, xs2aResponse, tppInfo, requestParameters, new AspspConsentData());

        PisConsentRequest pisConsentRequest;
        if (requestParameters.getPaymentType() == SINGLE) {
            pisConsentRequest = pisConsentMapper.mapToCmsPisConsentRequestForSinglePayment(consentData);
        } else if (requestParameters.getPaymentType() == PERIODIC) {
            pisConsentRequest = pisConsentMapper.mapToCmsPisConsentRequestForPeriodicPayment(consentData);
        } else {
            pisConsentRequest = pisConsentMapper.mapToCmsPisConsentRequestForBulkPayment(consentData);
        }
        CreatePisConsentResponse consentResponse = consentRestTemplate.postForEntity(remotePisConsentUrls.createPisConsent(), pisConsentRequest, CreatePisConsentResponse.class).getBody();

        return ResponseObject.builder().body(extendPaymentResponseFields(xs2aResponse, consentResponse, requestParameters.getPaymentType())).build();
    }

    private <T> Object extendPaymentResponseFields(T response, CreatePisConsentResponse cmsResponse, PaymentType paymentType) {
        Object extendedResponse = EnumSet.of(SINGLE, PERIODIC).contains(paymentType)
                                      ? extendPaymentResponseFieldsSimple((PaymentInitialisationResponse) response, cmsResponse, paymentType)
                                      : extendPaymentResponseFieldsBulk((List<PaymentInitialisationResponse>) response, cmsResponse);

        return IMPLICIT == profileService.getAuthorisationStartType()
                   ? createPisAuthorisationForImplicitApproach(extendedResponse, paymentType)
                   : extendedResponse;
    }

    private List<PaymentInitialisationResponse> extendPaymentResponseFieldsBulk(List<PaymentInitialisationResponse> responses, CreatePisConsentResponse cmsResponse) {
        return responses.stream()
                   .map(resp -> extendPaymentResponseFieldsSimple(resp, cmsResponse, BULK))
                   .collect(Collectors.toList());
    }

    private PaymentInitialisationResponse extendPaymentResponseFieldsSimple(PaymentInitialisationResponse response, CreatePisConsentResponse cmsResponse, PaymentType paymentType) {
        return Optional.ofNullable(cmsResponse)
                   .filter(c -> StringUtils.isNoneBlank(c.getConsentId(), c.getPaymentId()))
                   .map(c -> {
                       response.setPaymentId(c.getPaymentId());
                       response.setTransactionStatus(RCVD);
                       response.setPisConsentId(c.getConsentId());
                       response.setPaymentType(paymentType.name());
                       return response;
                   })
                   .orElseGet(() -> response);
    }

    private <T> Object createPisAuthorisationForImplicitApproach(T response, PaymentType paymentType) {
        if (EnumSet.of(SINGLE, PERIODIC).contains(paymentType)) {
            PaymentInitialisationResponse resp = (PaymentInitialisationResponse) response;
            return pisScaAuthorisationService.createConsentAuthorisation(resp.getPaymentId(), paymentType)
                       .map(r -> extendResponseFieldsWithAuthData(r, resp))
                       .orElseGet(() -> resp);
        } else {
            List<PaymentInitialisationResponse> responses = (List<PaymentInitialisationResponse>) response;
            return pisScaAuthorisationService.createConsentAuthorisation(responses.get(0).getPaymentId(), paymentType)
                       .map(r -> responses.stream()
                                     .map(pr -> extendResponseFieldsWithAuthData(r, pr))
                                     .collect(Collectors.toList()))
                       .orElseGet(() -> responses);
        }
    }

    private PaymentInitialisationResponse extendResponseFieldsWithAuthData(Xsa2CreatePisConsentAuthorisationResponse authorisationResponse, PaymentInitialisationResponse response) {
        response.setAuthorizationId(authorisationResponse.getAuthorizationId());
        response.setScaStatus(authorisationResponse.getScaStatus());
        return response;
    }

    private CreatePisConsentData getPisConsentData(Object payment, Object xs2aResponse, TppInfo tppInfo, PaymentRequestParameters requestParameters, AspspConsentData aspspConsentData) {
        CreatePisConsentData pisConsentData;
        if (requestParameters.getPaymentType() == SINGLE) {
            SinglePayment singlePayment = (SinglePayment) payment;
            PaymentInitialisationResponse response = (PaymentInitialisationResponse) xs2aResponse;
            singlePayment.setPaymentId(response.getPaymentId());
            pisConsentData = new CreatePisConsentData(singlePayment, tppInfo, requestParameters.getPaymentProduct().getCode(), aspspConsentData);
        } else if (requestParameters.getPaymentType() == PERIODIC) {
            PeriodicPayment periodicPayment = (PeriodicPayment) payment;
            PaymentInitialisationResponse response = (PaymentInitialisationResponse) xs2aResponse;
            periodicPayment.setPaymentId(response.getPaymentId());
            pisConsentData = new CreatePisConsentData(periodicPayment,tppInfo, requestParameters.getPaymentProduct().getCode(), aspspConsentData);
        } else {
            List<SinglePayment> payments = (List<SinglePayment>) payment;
            List<PaymentInitialisationResponse> responses = (List<PaymentInitialisationResponse>) xs2aResponse;

            Map<SinglePayment, PaymentInitialisationResponse> paymentMap = IntStream.range(0, payments.size())
                                                                               .boxed()
                                                                               .collect(Collectors.toMap(payments::get, responses::get));
            paymentMap.forEach((k, v) -> k.setPaymentId(v.getPaymentId()));
            pisConsentData = new CreatePisConsentData(paymentMap, tppInfo, requestParameters.getPaymentProduct().getCode(), aspspConsentData);
        }
        return pisConsentData;
    }
    /**
     * Updates PIS consent authorization according to psu's sca methods
     *
     * @param request Provides transporting data when updating consent authorization
     * @return sca status
     */
    public UpdatePisConsentPsuDataResponse updatePisConsentAuthorisation(UpdatePisConsentPsuDataRequest request) {
        GetPisConsentAuthorisationResponse pisConsentAuthorisation = consentRestTemplate.exchange(remotePisConsentUrls.getPisConsentAuthorisationById(), HttpMethod.GET, new HttpEntity<>(request), GetPisConsentAuthorisationResponse.class, request.getAuthorizationId())
                                                                         .getBody();

        if (STARTED == pisConsentAuthorisation.getScaStatus()) {
            SpiResponse<SpiAuthorisationStatus> authorisationStatusSpiResponse = paymentSpi.authorisePsu(request.getPsuId(), request.getPassword(), new AspspConsentData());

            if (SpiAuthorisationStatus.FAILURE == authorisationStatusSpiResponse.getPayload()) {
                return new UpdatePisConsentPsuDataResponse(FAILED);
            }
            request.setCmsAspspConsentData(new CmsAspspConsentData(authorisationStatusSpiResponse.getAspspConsentData().getBody()));
            List<SpiScaMethod> spiScaMethods = paymentSpi.readAvailableScaMethod(request.getPsuId(), authorisationStatusSpiResponse.getAspspConsentData()).getPayload();

            if (CollectionUtils.isEmpty(spiScaMethods)) {
                request.setScaStatus(FINALISED);
                paymentSpi.executePayment(pisConsentAuthorisation.getPaymentType(), pisConsentAuthorisation.getPayments(), authorisationStatusSpiResponse.getAspspConsentData());
                return doUpdatePisConsentAuthorisation(request);

            } else if (isSingleScaMethod(spiScaMethods)) {
                request.setScaStatus(SCAMETHODSELECTED);
                request.setAuthenticationMethodId(spiScaMethods.get(0).name());
                paymentSpi.performStrongUserAuthorisation(request.getPsuId(), new AspspConsentData());
                return doUpdatePisConsentAuthorisation(request);

            } else if (isMultipleScaMethods(spiScaMethods)) {
                request.setScaStatus(PSUAUTHENTICATED);
                UpdatePisConsentPsuDataResponse response = doUpdatePisConsentAuthorisation(request);
                response.setAvailableScaMethods(pisConsentMapper.mapToCmsScaMethods(spiScaMethods));
                return response;

            }
        } else if (SCAMETHODSELECTED == pisConsentAuthorisation.getScaStatus()) {
            request.setScaStatus(FINALISED);
            paymentSpi.applyStrongUserAuthorisation(buildSpiPaymentConfirmation(request, pisConsentAuthorisation.getConsentId()), new AspspConsentData());
            paymentSpi.executePayment(pisConsentAuthorisation.getPaymentType(), pisConsentAuthorisation.getPayments(), new AspspConsentData());
            return doUpdatePisConsentAuthorisation(request);

        } else if (PSUAUTHENTICATED == pisConsentAuthorisation.getScaStatus()) {
            request.setScaStatus(SCAMETHODSELECTED);
            paymentSpi.performStrongUserAuthorisation(request.getPsuId(), new AspspConsentData());
            return doUpdatePisConsentAuthorisation(request);
        }
        return new UpdatePisConsentPsuDataResponse(null);
    }

    private UpdatePisConsentPsuDataResponse doUpdatePisConsentAuthorisation(UpdatePisConsentPsuDataRequest request) {
        return consentRestTemplate.exchange(remotePisConsentUrls.updatePisConsentAuthorisation(), HttpMethod.PUT, new HttpEntity<>(request),
            UpdatePisConsentPsuDataResponse.class, request.getAuthorizationId()).getBody();
    }

    private SpiPaymentConfirmation buildSpiPaymentConfirmation(UpdatePisConsentPsuDataRequest request, String consentId) {
        SpiPaymentConfirmation paymentConfirmation = new SpiPaymentConfirmation();
        paymentConfirmation.setTanNumber(request.getPassword());
        paymentConfirmation.setPaymentId(request.getPaymentId());
        paymentConfirmation.setConsentId(consentId);
        paymentConfirmation.setPsuId(request.getPsuId());
        return paymentConfirmation;
    }

    private boolean isSingleScaMethod(List<SpiScaMethod> spiScaMethods) {
        return spiScaMethods.size() == 1;
    }

    private boolean isMultipleScaMethods(List<SpiScaMethod> spiScaMethods) {
        return spiScaMethods.size() > 1;
    }
}
