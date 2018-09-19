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

import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.pis.*;
import de.adorsys.aspsp.xs2a.service.ValidatePaymentService;
import de.adorsys.aspsp.xs2a.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InitiateSinglePayment implements InitiatePayment {
    private final ValidatePaymentService validatePaymentService;
    private final PaymentMapper paymentMapper;
    private final ScaPaymentService scaPaymentService;

    @Override
    public ResponseObject createPayment(Xs2aCommonPaymentInitialisationRequest request, String tppSignatureCertificate) {
        SinglePayment singlePayment = request.getSinglePayment();

        Optional<MessageErrorCode> validationError = validatePaymentService.getPaymentValidationError(singlePayment.getAccountReferences(), singlePayment.isValidExecutionDateAndTime());

        if (validationError.isPresent()) {
            return ResponseObject.<PaymentInitialisationResponse>builder()
                       .body(paymentMapper.mapToPaymentInitResponseFailedPayment(singlePayment, validationError.get()))
                       .build();
        }

        return createSinglePayment(singlePayment, tppSignatureCertificate, request.getPaymentProduct());
    }

    private ResponseObject createSinglePayment(SinglePayment singlePayment, String tppSignatureCertificate, PaymentProduct paymentProduct) {
        TppInfo tppInfo = paymentMapper.mapToTppInfo(tppSignatureCertificate);

        return ResponseObject.<PaymentInitialisationResponse>builder()
                   .body(scaPaymentService.createSinglePayment(singlePayment, tppInfo, paymentProduct.getCode()))
                   .build();
    }
}
