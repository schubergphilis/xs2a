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

package de.adorsys.aspsp.xs2a.spi.mapper;

import de.adorsys.aspsp.xs2a.spi.domain.SpiResponse;
import de.adorsys.aspsp.xs2a.spi.domain.common.SpiTransactionStatus;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiSinglePayment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.CREATED;

@Component
public class SpiLayerMapper {
    public SpiPaymentInitialisationResponse mapToSpiPaymentResponse(SpiSinglePayment spiSinglePayment) {
        SpiPaymentInitialisationResponse paymentResponse = new SpiPaymentInitialisationResponse();
        paymentResponse.setSpiTransactionFees(null);
        paymentResponse.setSpiTransactionFeeIndicator(false);
        paymentResponse.setScaMethods(null);
        paymentResponse.setTppRedirectPreferred(false);
        if (spiSinglePayment.getPaymentId() == null) {
            paymentResponse.setTransactionStatus(SpiTransactionStatus.RJCT);
            paymentResponse.setPaymentId(spiSinglePayment.getEndToEndIdentification());
            paymentResponse.setPsuMessage(null);
            paymentResponse.setTppMessages(new String[]{"PAYMENT_FAILED"});
        } else {
            paymentResponse.setTransactionStatus(SpiTransactionStatus.RCVD);
            paymentResponse.setPaymentId(spiSinglePayment.getPaymentId());
        }
        return paymentResponse;
    }

    public SpiResponse<SpiPaymentInitialisationResponse> mapToSpiPaymentInitiationResponse(SpiSinglePayment spiSinglePayment, ResponseEntity<SpiSinglePayment> responseEntity, AspspConsentData aspspConsentData) {
        SpiPaymentInitialisationResponse response =
            responseEntity.getStatusCode() == CREATED
                ? mapToSpiPaymentResponse(responseEntity.getBody())
                : mapToSpiPaymentResponse(spiSinglePayment);
        return new SpiResponse<>(response, aspspConsentData);
    }
}
