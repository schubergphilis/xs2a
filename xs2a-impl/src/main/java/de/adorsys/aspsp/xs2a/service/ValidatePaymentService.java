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

package de.adorsys.aspsp.xs2a.service;

import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.account.AccountReference;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static de.adorsys.aspsp.xs2a.domain.MessageErrorCode.EXECUTION_DATE_INVALID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidatePaymentService {
    private final AccountReferenceValidationService referenceValidationService;

    public Optional<MessageErrorCode> getPaymentValidationError(Set<AccountReference> references, boolean areValidDates) {
        MessageErrorCode error = null;

        if (!areValidDates) {
            error = EXECUTION_DATE_INVALID;
        } else {
            ResponseObject validationErrorObject = referenceValidationService.validateAccountReferences(references);

            if (validationErrorObject.hasError()) {
                error = validationErrorObject.getError().getTppMessage().getMessageErrorCode();
            }
        }
        return Optional.ofNullable(error);
    }

    public Map<SinglePayment, MessageErrorCode> getPaymentValidationErrorForPaymentList(List<SinglePayment> payments) {
        Map<SinglePayment, MessageErrorCode> invalidPayments = new HashMap();

        for (SinglePayment payment : payments) {
            getPaymentValidationError(payment.getAccountReferences(), payment.isValidExecutionDateAndTime())
                .ifPresent(messageErrorCode -> invalidPayments.put(payment, messageErrorCode));
        }

        return invalidPayments;
    }

}
