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

import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayment;
import de.adorsys.aspsp.xs2a.domain.pis.TppInfo;
import de.adorsys.aspsp.xs2a.service.mapper.PaymentMapper;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.spi.service.PaymentSpi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedirectAndEmbeddedPaymentService implements ScaPaymentService {
    private final PaymentSpi paymentSpi;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentInitialisationResponse createSinglePayment(SinglePayment payment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here
        SpiPaymentInitialisationResponse aspspResponse = paymentSpi.createPaymentInitiation(paymentMapper.mapToSpiSinglePayment(payment), aspspConsentData).getPayload();
        return paymentMapper.mapToPaymentInitializationResponse(aspspResponse);
    }

    @Override
    public PaymentInitialisationResponse createPeriodicPayment(PeriodicPayment payment, TppInfo tppInfo, String paymentProduct) {
        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/191 Put a real data here
        SpiPaymentInitialisationResponse aspspResponse = paymentSpi.initiatePeriodicPayment(paymentMapper.mapToSpiPeriodicPayment(payment), aspspConsentData).getPayload();
        return paymentMapper.mapToPaymentInitializationResponse(aspspResponse);
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

    private PaymentInitialisationResponse createConsentSinglePaymentAndExtendResponse(SinglePayment payment, TppInfo tppInfo, String paymentProduct, AspspConsentData aspspConsentData, PaymentInitialisationResponse xs2aResponse) {
        payment.setPaymentId(xs2aResponse.getPaymentId());
        CreatePisConsentData pisConsentData = new CreatePisConsentData(payment, tppInfo, paymentProduct, aspspConsentData);
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForSinglePayment(pisConsentData, xs2aResponse.getPaymentId());
        return extendPaymentResponseFields(xs2aResponse, cmsResponse, SINGLE);
    }

    private PaymentInitialisationResponse createConsentPeriodicPaymentAndExtendResponse(PeriodicPayment payment, TppInfo tppInfo, String paymentProduct, AspspConsentData aspspConsentData, PaymentInitialisationResponse xs2aResponse) {
        payment.setPaymentId(xs2aResponse.getPaymentId());
        CreatePisConsentData pisConsentData = new CreatePisConsentData(payment, tppInfo, paymentProduct, aspspConsentData);
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForPeriodicPayment(pisConsentData, xs2aResponse.getPaymentId());
        return extendPaymentResponseFields(xs2aResponse, cmsResponse, PERIODIC);
    }

    private List<PaymentInitialisationResponse> createConsentForBulkPaymentAndExtendPaymentResponses(CreatePisConsentData createPisConsentData) {
        CreatePisConsentResponse cmsResponse = pisConsentService.createPisConsentForBulkPayment(createPisConsentData);

        List<PaymentInitialisationResponse> responses = Lists.newArrayList(createPisConsentData.getPaymentIdentifierMap().values());
        return responses.stream()
                   .map(resp -> extendPaymentResponseFields(resp, cmsResponse, BULK))
        List<SpiSinglePayment> singlePayments = paymentMapper.mapToSpiSinglePaymentList(payments);
        return paymentSpi.createBulkPayments(singlePayments, aspspConsentData).getPayload()
                   .stream()
                   .map(paymentMapper::mapToPaymentInitializationResponse)
                   .collect(Collectors.toList());
    }
}
