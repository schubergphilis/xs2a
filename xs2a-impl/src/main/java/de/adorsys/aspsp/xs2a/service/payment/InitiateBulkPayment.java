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
import de.adorsys.aspsp.xs2a.exception.MessageError;
import de.adorsys.aspsp.xs2a.service.ValidatePaymentService;
import de.adorsys.aspsp.xs2a.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.adorsys.aspsp.xs2a.domain.MessageErrorCode.FORMAT_ERROR;
import static de.adorsys.aspsp.xs2a.domain.MessageErrorCode.PAYMENT_FAILED;
import static de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus.RJCT;

@Component
@RequiredArgsConstructor
public class InitiateBulkPayment implements InitiatePayment {
    private final ValidatePaymentService validatePaymentService;
    private final PaymentMapper paymentMapper;
    private final ScaPaymentService scaPaymentService;

    @Override
    public ResponseObject createPayment(Xs2aCommonPaymentInitialisationRequest request, String tppSignatureCertificate) {
        BulkPayment bulkPayment = request.getBulkPayment();

        if (CollectionUtils.isEmpty(bulkPayment.getPayments())) {
            return ResponseObject.<List<PaymentInitialisationResponse>>builder()
                       .fail(new MessageError(FORMAT_ERROR))
                       .build();
        }

        Map<SinglePayment, MessageErrorCode> validationErrorMap = validatePaymentService.getPaymentValidationErrorForPaymentList(bulkPayment.getPayments());

        List<SinglePayment> validPayments = new ArrayList<>(CollectionUtils.subtract(bulkPayment.getPayments(), validationErrorMap.keySet()));
        List<PaymentInitialisationResponse> paymentResponses = createBulkPayment(validPayments, tppSignatureCertificate, request.getPaymentProduct());

        if (hasAnyValidResponse(paymentResponses)) {
            paymentResponses.addAll(getErrorResponseListFromMap(validationErrorMap));

            return ResponseObject.<List<PaymentInitialisationResponse>>builder()
                       .body(paymentResponses)
                       .build();
        }

        return ResponseObject.<List<PaymentInitialisationResponse>>builder()
                   .fail(new MessageError(PAYMENT_FAILED))
                   .build();
    }

    private List<PaymentInitialisationResponse> createBulkPayment(List<SinglePayment> validPayments, String tppSignatureCertificate, PaymentProduct paymentProduct) {
        TppInfo tppInfo = paymentMapper.mapToTppInfo(tppSignatureCertificate);
        return scaPaymentService.createBulkPayment(validPayments, tppInfo, paymentProduct.getCode());
    }

    private boolean hasAnyValidResponse(List<PaymentInitialisationResponse> paymentResponses) {
        return paymentResponses.stream().anyMatch(pr -> pr.getTransactionStatus() != RJCT);
    }

    private List<PaymentInitialisationResponse> getErrorResponseListFromMap(Map<SinglePayment, MessageErrorCode> validationErrorMap) {
        return validationErrorMap.entrySet().stream()
                   .map(entr -> paymentMapper.mapToPaymentInitResponseFailedPayment(entr.getKey(), entr.getValue()))
                   .collect(Collectors.toList());
    }
}
