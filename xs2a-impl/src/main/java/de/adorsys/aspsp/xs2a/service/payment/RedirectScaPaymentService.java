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

import de.adorsys.aspsp.xs2a.consent.api.pis.proto.CreatePisConsentResponse;
import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus;
import de.adorsys.aspsp.xs2a.domain.consent.CreatePisConsentData;
import de.adorsys.aspsp.xs2a.domain.pis.*;
import de.adorsys.aspsp.xs2a.service.consent.PisConsentService;
import de.adorsys.aspsp.xs2a.service.mapper.PaymentMapper;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPeriodicPayment;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiSinglePayment;
import de.adorsys.aspsp.xs2a.spi.service.PaymentSpi;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.adorsys.aspsp.xs2a.domain.MessageErrorCode.PAYMENT_FAILED;

@Service
@RequiredArgsConstructor
public class RedirectScaPaymentService implements ScaPaymentService {
    private final PisConsentService pisConsentService;
    private final PaymentMapper paymentMapper;
    private final PaymentSpi paymentSpi;

    @Override
    public PaymentInitialisationResponse createSinglePayment(SinglePayment singlePayment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here

        PaymentInitialisationResponse response = createSinglePaymentAndGetResponse(singlePayment, aspspConsentData);
        return response.getTransactionStatus() != Xs2aTransactionStatus.RJCT
                   ? createConsentForSinglePaymentAndExtendPaymentResponse(new CreatePisConsentData(singlePayment, tppInfo, paymentProduct, aspspConsentData), response)
                   : response;
    }

    @Override
    public PaymentInitialisationResponse createPeriodicPayment(PeriodicPayment periodicPayment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here

        PaymentInitialisationResponse response = createPeriodicPaymentAndGetResponse(periodicPayment, aspspConsentData);
        return response.getTransactionStatus() != Xs2aTransactionStatus.RJCT
                   ? createConsentForPeriodicPaymentAndExtendPaymentResponse(new CreatePisConsentData(periodicPayment, tppInfo, paymentProduct, aspspConsentData), response)
                   : response;
    }

    private PaymentInitialisationResponse createPeriodicPaymentAndGetResponse(PeriodicPayment periodicPayment, AspspConsentData aspspConsentData) {
        SpiPeriodicPayment spiPeriodicPayment = paymentMapper.mapToSpiPeriodicPayment(periodicPayment);
        return paymentMapper.mapToPaymentInitializationResponse(paymentSpi.initiatePeriodicPayment(spiPeriodicPayment, aspspConsentData).getPayload());
    }

    private PaymentInitialisationResponse createConsentForPeriodicPaymentAndExtendPaymentResponse(CreatePisConsentData createPisConsentData, PaymentInitialisationResponse response) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForPeriodicPayment(createPisConsentData, response.getPaymentId());
        return extendPaymentResponseFields(response, cmsResponse);
    }

    @Override
    public List<PaymentInitialisationResponse> createBulkPayment(BulkPayment bulkPayment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here
        List<PaymentInitialisationResponse> aspspResponse = paymentSpi.createBulkPayments(paymentMapper.mapToSpiBulkPayment(bulkPayment), aspspConsentData).getPayload()
                                                                .stream()
                                                                .map(paymentMapper::mapToPaymentInitializationResponse)
                                                                .collect(Collectors.toList());
        List<SinglePayment> payments = bulkPayment.getPayments();
        Map<SinglePayment, PaymentInitialisationResponse> paymentMap = IntStream.range(0, payments.size())
                                                                           .boxed()
                                                                           .collect(Collectors.toMap(payments::get, aspspResponse::get));

        CreatePisConsentData pisConsentData = new CreatePisConsentData(paymentMap, tppInfo, paymentProduct, aspspConsentData);
        return createConsentForBulkPaymentAndExtendPaymentResponses(pisConsentData);
    }

    private List<PaymentInitialisationResponse> createConsentForBulkPaymentAndExtendPaymentResponses(CreatePisConsentData createPisConsentData) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForBulkPayment(createPisConsentData);

        return Optional.ofNullable(cmsResponse)
                   .filter(c -> StringUtils.isNotBlank(c.getConsentId()))
                   .map(c -> enrichBulkResponse(c, createPisConsentData.getPaymentIdentifierMap()))
                   .orElse(Collections.emptyList());
    }

    private List<PaymentInitialisationResponse> enrichBulkResponse(CreatePisConsentResponse cmsResponse, Map<SinglePayment, PaymentInitialisationResponse> paymentIdentifierMap) {
        paymentIdentifierMap.forEach((k, v) -> {
            v.setPaymentId(cmsResponse.getPaymentId());
            v.setPisConsentId(cmsResponse.getConsentId());
            v.setTransactionStatus(Xs2aTransactionStatus.RCVD);
        });
        return new ArrayList<>(paymentIdentifierMap.values());
    }

    private PaymentInitialisationResponse createSinglePaymentAndGetResponse(SinglePayment singlePayment, AspspConsentData aspspConsentData) {
        SpiSinglePayment spiSinglePayment = paymentMapper.mapToSpiSinglePayment(singlePayment);
        SpiPaymentInitialisationResponse spiSinglePaymentResp = paymentSpi.createPaymentInitiation(spiSinglePayment, aspspConsentData).getPayload();
        return paymentMapper.mapToPaymentInitializationResponse(spiSinglePaymentResp);
    }

    private PaymentInitialisationResponse createConsentForSinglePaymentAndExtendPaymentResponse(CreatePisConsentData createPisConsentData, PaymentInitialisationResponse response) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForSinglePayment(createPisConsentData, response.getPaymentId());

        return extendPaymentResponseFields(response, cmsResponse);
    }

    private PaymentInitialisationResponse extendPaymentResponseFields(PaymentInitialisationResponse response, CreatePisConsentResponse cmsResponse) {
        return Optional.ofNullable(cmsResponse)
                   .filter(c -> StringUtils.isNotBlank(c.getPaymentId()) && StringUtils.isNotBlank(c.getConsentId()))
                   .map(s -> {
                       response.setPaymentId(s.getPaymentId());
                       response.setPisConsentId(s.getConsentId());
                       return response;
                   })
                   .orElse(null);
    }
}
