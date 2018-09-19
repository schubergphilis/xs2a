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

package de.adorsys.aspsp.xs2a.domain.pis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@Value
@ApiModel(description = "Common payment initialisation request", value = "CommonPaymentObject")
public class Xs2aCommonPaymentInitialisationRequest {

    @ApiModelProperty(value = "Single Payment Initialisation Request")
    private SinglePayment singlePayment;

    @ApiModelProperty(value = "Bulk Payment Initialisation Request")
    private BulkPayment bulkPayment;

    @ApiModelProperty(value = "Periodic Payment Initialisation Request")
    private PeriodicPayment periodicPayment;

    @ApiModelProperty(value = "Payment type")
    private PaymentType paymentType;

    @ApiModelProperty(value = "Payment product")
    private PaymentProduct paymentProduct;

    public Xs2aCommonPaymentInitialisationRequest(SinglePayment singlePayment, PaymentProduct paymentProduct) {
        this.singlePayment = singlePayment;
        this.paymentType = PaymentType.SINGLE;
        this.paymentProduct = paymentProduct;

        this.bulkPayment = null;
        this.periodicPayment = null;
    }

    public Xs2aCommonPaymentInitialisationRequest(BulkPayment bulkPayment, PaymentProduct paymentProduct) {
        this.bulkPayment = bulkPayment;
        this.paymentType = PaymentType.BULK;
        this.paymentProduct = paymentProduct;

        this.singlePayment = null;
        this.periodicPayment = null;
    }

    public Xs2aCommonPaymentInitialisationRequest(PeriodicPayment periodicPayment, PaymentProduct paymentProduct) {
        this.periodicPayment = periodicPayment;
        this.paymentType = PaymentType.PERIODIC;
        this.paymentProduct = paymentProduct;

        this.bulkPayment = null;
        this.singlePayment = null;
    }
}
