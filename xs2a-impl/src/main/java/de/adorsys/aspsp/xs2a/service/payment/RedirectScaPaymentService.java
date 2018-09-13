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
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitiationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayment;
import de.adorsys.aspsp.xs2a.domain.pis.TppInfo;
import de.adorsys.aspsp.xs2a.service.consent.PisConsentService;
import de.adorsys.aspsp.xs2a.service.mapper.PaymentMapper;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPaymentInitiationResponse;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPeriodicPayment;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiSinglePayment;
import de.adorsys.aspsp.xs2a.spi.service.PaymentSpi;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static de.adorsys.aspsp.xs2a.domain.MessageErrorCode.PAYMENT_FAILED;

@Service
@RequiredArgsConstructor
public class RedirectScaPaymentService implements ScaPaymentService {
    private final PisConsentService pisConsentService;
    private final PaymentMapper paymentMapper;
    private final PaymentSpi paymentSpi;

    @Override
    public PaymentInitiationResponse createSinglePayment(SinglePayment singlePayment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here

        PaymentInitiationResponse response = createSinglePaymentAndGetResponse(singlePayment, aspspConsentData);
        return response.getTransactionStatus() != Xs2aTransactionStatus.RJCT
                   ? createConsentForSinglePaymentAndExtendPaymentResponse(new CreatePisConsentData(singlePayment, tppInfo, paymentProduct, aspspConsentData), response)
                   : response;
    }

    @Override
    public PaymentInitiationResponse createPeriodicPayment(PeriodicPayment periodicPayment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here

        PaymentInitiationResponse response = createPeriodicPaymentAndGetResponse(periodicPayment, aspspConsentData);
        return response.getTransactionStatus() != Xs2aTransactionStatus.RJCT
                   ? createConsentForPeriodicPaymentAndExtendPaymentResponse(new CreatePisConsentData(periodicPayment, tppInfo, paymentProduct, aspspConsentData), response)
                   : response;
    }

    private PaymentInitiationResponse createPeriodicPaymentAndGetResponse(PeriodicPayment periodicPayment, AspspConsentData aspspConsentData) {
        SpiPeriodicPayment spiPeriodicPayment = paymentMapper.mapToSpiPeriodicPayment(periodicPayment);
        return paymentMapper.mapToPaymentInitializationResponse(paymentSpi.initiatePeriodicPayment(spiPeriodicPayment, aspspConsentData).getPayload());
    }

    private PaymentInitiationResponse createConsentForPeriodicPaymentAndExtendPaymentResponse(CreatePisConsentData createPisConsentData, PaymentInitiationResponse response) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForPeriodicPayment(createPisConsentData, response.getPaymentId());
        return extendPaymentResponseFields(response, cmsResponse);
    }

    @Override
    public List<PaymentInitiationResponse> createBulkPayment(List<SinglePayment> payments, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here
        Map<SinglePayment, PaymentInitiationResponse> paymentIdentifierMap = createBulkPaymentAndGetResponseMap(payments, aspspConsentData);

        return MapUtils.isNotEmpty(paymentIdentifierMap)
                   ? createConsentForBulkPaymentAndExtendPaymentResponses(new CreatePisConsentData(paymentIdentifierMap, tppInfo, paymentProduct, aspspConsentData))
                   : Collections.emptyList();
    }

    private Map<SinglePayment, PaymentInitiationResponse> createBulkPaymentAndGetResponseMap(List<SinglePayment> payments, AspspConsentData aspspConsentData) {
        HashMap<SinglePayment, PaymentInitiationResponse> paymentIdentifierMap = new HashMap<>();

        for (SinglePayment payment : payments) {
            PaymentInitiationResponse paymentInitiationResponse = createSinglePaymentAndGetResponse(payment, aspspConsentData);
            paymentIdentifierMap.put(payment, paymentInitiationResponse);
        }

        paymentIdentifierMap.forEach((sp, resp) -> {
            if (StringUtils.isBlank(resp.getPaymentId())
                    || resp.getTransactionStatus() == Xs2aTransactionStatus.RJCT) {
                resp.setTppMessages(new MessageErrorCode[]{PAYMENT_FAILED});
                resp.setTransactionStatus(Xs2aTransactionStatus.RJCT);
            }
        });
        return paymentIdentifierMap;
    }

    private List<PaymentInitiationResponse> createBulkPaymentAndGetResponse(List<SinglePayment> payments) {  // NOPMD return when we make storing payment info with payment ID
        List<SpiSinglePayment> spiPayments = paymentMapper.mapToSpiSinglePaymentList(payments);
        List<SpiPaymentInitiationResponse> spiPaymentInitiations = paymentSpi.createBulkPayments(spiPayments, new AspspConsentData()).getPayload(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here

        List<PaymentInitiationResponse> paymentResponses = spiPaymentInitiations.stream()
                                                                   .map(paymentMapper::mapToPaymentInitializationResponse)
                                                                   .collect(Collectors.toList());

        for (PaymentInitiationResponse resp : paymentResponses) {
            if (StringUtils.isBlank(resp.getPaymentId())
                    || resp.getTransactionStatus() == Xs2aTransactionStatus.RJCT) {
                resp.setTppMessages(new MessageErrorCode[]{PAYMENT_FAILED});
                resp.setTransactionStatus(Xs2aTransactionStatus.RJCT);
            }
        }

        return paymentResponses;
    }

    private List<PaymentInitiationResponse> createConsentForBulkPaymentAndExtendPaymentResponses(CreatePisConsentData createPisConsentData) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForBulkPayment(createPisConsentData);

        return Optional.ofNullable(cmsResponse)
                   .filter(c -> StringUtils.isNotBlank(c.getConsentId()))
                   .map(c -> enrichBulkResponse(c, createPisConsentData.getPaymentIdentifierMap()))
                   .orElse(Collections.emptyList());
    }

    private List<PaymentInitiationResponse> enrichBulkResponse(CreatePisConsentResponse cmsResponse, Map<SinglePayment, PaymentInitiationResponse> paymentIdentifierMap) {
        paymentIdentifierMap.forEach((k, v) -> {
            v.setPaymentId(cmsResponse.getPaymentId());
            v.setPisConsentId(cmsResponse.getConsentId());
            v.setTransactionStatus(Xs2aTransactionStatus.RCVD);
        });
        return new ArrayList<>(paymentIdentifierMap.values());
    }

    private PaymentInitiationResponse createSinglePaymentAndGetResponse(SinglePayment singlePayment, AspspConsentData aspspConsentData) {
        SpiSinglePayment spiSinglePayment = paymentMapper.mapToSpiSinglePayment(singlePayment);
        SpiPaymentInitiationResponse spiSinglePaymentResp = paymentSpi.createPaymentInitiation(spiSinglePayment, aspspConsentData).getPayload();
        return paymentMapper.mapToPaymentInitializationResponse(spiSinglePaymentResp);
    }

    private PaymentInitiationResponse createConsentForSinglePaymentAndExtendPaymentResponse(CreatePisConsentData createPisConsentData, PaymentInitiationResponse response) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForSinglePayment(createPisConsentData, response.getPaymentId());

        return extendPaymentResponseFields(response, cmsResponse);
    }

    private PaymentInitiationResponse extendPaymentResponseFields(PaymentInitiationResponse response, CreatePisConsentResponse cmsResponse) {
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
