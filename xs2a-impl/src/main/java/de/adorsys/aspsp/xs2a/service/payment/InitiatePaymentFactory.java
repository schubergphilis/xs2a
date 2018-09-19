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


import de.adorsys.aspsp.xs2a.domain.pis.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitiatePaymentFactory {
    @Qualifier("initiateSinglePayment")
    private final InitiateSinglePayment initiateSinglePayment;

    @Qualifier("initiatePeriodicPayment")
    private final InitiatePeriodicPayment initiatePeriodicPayment;

    @Qualifier("initiateBulkPayment")
    private final InitiateBulkPayment initiateBulkPayment;

    public InitiatePayment getInitialService(PaymentType paymentType) {
        if (paymentType == PaymentType.SINGLE) {
            return initiateSinglePayment;
        } else if (paymentType == PaymentType.PERIODIC) {
            return initiatePeriodicPayment;
        } else {
            return initiateBulkPayment;
        }
    }
}
