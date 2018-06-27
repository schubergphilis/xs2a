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

import de.adorsys.aspsp.xs2a.component.JsonConverter;
import de.adorsys.aspsp.xs2a.domain.Amount;
import de.adorsys.aspsp.xs2a.domain.Links;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.TransactionStatus;
import de.adorsys.aspsp.xs2a.domain.account.AccountReference;
import de.adorsys.aspsp.xs2a.domain.code.BICFI;
import de.adorsys.aspsp.xs2a.domain.code.PurposeCode;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentProduct;
import de.adorsys.aspsp.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;
import de.adorsys.aspsp.xs2a.service.consent.pis.PisConsentService;
import de.adorsys.aspsp.xs2a.spi.domain.common.SpiTransactionStatus;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.spi.service.PaymentSpi;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static de.adorsys.aspsp.xs2a.domain.MessageErrorCode.*;
import static de.adorsys.aspsp.xs2a.spi.domain.common.SpiTransactionStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentServiceTest {

    private final String PERIODIC_PAYMENT_DATA = "/json/PeriodicPaymentTestData.json";
    private final Charset UTF_8 = Charset.forName("utf-8");
    private static final String PAYMENT_ID = "12345";
    private static final String PAYMENT_CONSENT_ID = "12345678";
    private static final String WRONG_PAYMENT_ID = "0";
    private static final String IBAN = "DE123456789";
    private static final String WRONG_IBAN = "wrong_iban";
    private static final String AMOUNT = "100";
    private static final String EXCESSIVE_AMOUNT = "10000";
    private static final Currency CURRENCY = Currency.getInstance("EUR");

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private JsonConverter jsonConverter;
    @Autowired
    PaymentMapper paymentMapper;

    @MockBean(name = "paymentSpi")
    private PaymentSpi paymentSpi;
    @MockBean(name = "accountService")
    private AccountService accountService;

    @MockBean(name = "pisConsentService")
    private PisConsentService pisConsentService;

    @MockBean(name = "aspspProfileService")
    private AspspProfileService aspspProfileService;

    @Before
    public void setUp() throws IOException {
        List<SpiPaymentInitialisationResponse> responseList = new ArrayList<>();
        responseList.add(getSpiPaymentResponse(ACCP));
        when(paymentSpi.initiatePeriodicPayment(any(), any(), anyBoolean()))
            .thenReturn(getSpiPaymentResponse(ACCP));
        //SinglePayment
        when(paymentSpi.createPaymentInitiation(paymentMapper.mapToSpiSinglePayments(getPaymentInitiationRequest(IBAN, AMOUNT)), "sepa-credit-transfers", false))
            .thenReturn(getSpiPaymentResponse(RCVD));
        when(paymentSpi.createPaymentInitiation(paymentMapper.mapToSpiSinglePayments(getPaymentInitiationRequest(IBAN, EXCESSIVE_AMOUNT)), "sepa-credit-transfers", false))
            .thenReturn(null);
        //Bulk
        when(paymentSpi.createBulkPayments(paymentMapper.mapToSpiSinglePaymentList(Collections.singletonList(getPaymentInitiationRequest(IBAN, AMOUNT))), "sepa-credit-transfers", false))
            .thenReturn(responseList);
        when(paymentSpi.createBulkPayments(paymentMapper.mapToSpiSinglePaymentList(Collections.singletonList(getPaymentInitiationRequest(IBAN, EXCESSIVE_AMOUNT))), "sepa-credit-transfers", false))
            .thenReturn(Collections.emptyList());
        when(paymentSpi.getPaymentStatusById(PAYMENT_ID, PaymentProduct.SCT.getCode()))
            .thenReturn(ACCP);
        when(paymentSpi.getPaymentStatusById(WRONG_PAYMENT_ID, PaymentProduct.SCT.getCode()))
            .thenReturn(RJCT);
        when(accountService.isAccountExists(readPeriodicPayment().getDebtorAccount()))
            .thenReturn(true);
        when(accountService.isAccountExists(readPeriodicPayment().getCreditorAccount()))
            .thenReturn(true);
        when(accountService.isAccountExists(getReference(IBAN)))
            .thenReturn(true);
        when(accountService.isAccountExists(getReference(WRONG_IBAN)))
            .thenReturn(false);
        when(accountService.getPaymentProductsAllowedToPsuByReference(getPaymentInitiationRequest(IBAN, AMOUNT).getDebtorAccount()))
            .thenReturn(Collections.singletonList("sepa-credit-transfers"));
        when(accountService.getPaymentProductsAllowedToPsuByReference(readPeriodicPayment().getDebtorAccount()))
            .thenReturn(Collections.singletonList("sepa-credit-transfers"));
        when(pisConsentService.createPisConsentForSinglePaymentAndGetId(any()))
            .thenReturn(PAYMENT_CONSENT_ID);
        when(pisConsentService.createPisConsentForBulkPaymentAndGetId(any()))
            .thenReturn(PAYMENT_CONSENT_ID);
        when(pisConsentService.createPisConsentForPeriodicPaymentAndGetId(any()))
            .thenReturn(PAYMENT_CONSENT_ID);
        when(aspspProfileService.isRedirectMode())
            .thenReturn(true);
    }

    @Test
    public void getPaymentStatusById_successesResult() {
        //Given
        TransactionStatus expectedTransactionStatus = TransactionStatus.ACCP;
        PaymentProduct paymentProduct = PaymentProduct.SCT;

        //When:
        ResponseObject<TransactionStatus> actualResponse = paymentService.getPaymentStatusById(PAYMENT_ID, paymentProduct.getCode());

        //Then:
        assertThat(actualResponse.getBody()).isEqualTo(expectedTransactionStatus);
    }

    @Test
    public void getPaymentStatusById_wrongId() {
        //Given
        TransactionStatus expectedTransactionStatus = TransactionStatus.RJCT;
        PaymentProduct paymentProduct = PaymentProduct.SCT;

        //When:
        ResponseObject<TransactionStatus> actualResponse = paymentService.getPaymentStatusById(WRONG_PAYMENT_ID, paymentProduct.getCode());

        //Then:
        assertThat(actualResponse.getBody()).isEqualTo(expectedTransactionStatus);
    }

    @Test
    public void createBulkPayments_Success() {
        // Given
        List<SinglePayments> payments = Collections.singletonList(getPaymentInitiationRequest(IBAN, AMOUNT));
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<List<PaymentInitialisationResponse>> actualResponse = paymentService.createBulkPayments(payments, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody().get(0).getTransactionStatus()).isEqualTo(TransactionStatus.ACCP);
    }

    @Test
    public void createBulkPayments_Failure_account_does_not_exist() {
        // Given
        List<SinglePayments> payments = Collections.singletonList(getPaymentInitiationRequest(WRONG_IBAN, AMOUNT));
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<List<PaymentInitialisationResponse>> actualResponse = paymentService.createBulkPayments(payments, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getError().getTppMessage().getCode()).isEqualTo(RESOURCE_UNKNOWN_400);
    }

    @Test
    public void createBulkPayments_Failure_ASPSP_rejected() {
        // Given
        List<SinglePayments> payments = Collections.singletonList(getPaymentInitiationRequest(IBAN, EXCESSIVE_AMOUNT));
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<List<PaymentInitialisationResponse>> actualResponse = paymentService.createBulkPayments(payments, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getError().getTppMessage().getCode()).isEqualTo(PAYMENT_FAILED);
    }

    @Test
    public void initiatePeriodicPayment() throws IOException { //TODO MORE TESTS SHOULD BE PRESENT FOR EACH USECASE
        //Given:
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;
        PeriodicPayment periodicPayment = readPeriodicPayment();
        ResponseObject<PaymentInitialisationResponse> expectedResult = readResponseObject();

        //When:
        ResponseObject<PaymentInitialisationResponse> result = paymentService.initiatePeriodicPayment(periodicPayment, paymentProduct.getCode(), tppRedirectPreferred);

        //Than:
        assertThat(result.getError()).isEqualTo(expectedResult.getError());
        assertThat(result.getBody().getTransactionStatus()).isEqualTo(expectedResult.getBody().getTransactionStatus());
    }

    @Test
    public void createPaymentInitiation_Success() {
        // Given
        SinglePayments payment = getPaymentInitiationRequest(IBAN, AMOUNT);
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<PaymentInitialisationResponse> actualResponse = paymentService.createPaymentInitiation(payment, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.getBody()).isNotNull();
        assertThat(actualResponse.getBody().getTransactionStatus()).isEqualTo(TransactionStatus.RCVD);
    }

    @Test
    public void createPaymentInitiation_Failure_nullPayment() {
        // Given
        SinglePayments payment = null;
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<PaymentInitialisationResponse> actualResponse = paymentService.createPaymentInitiation(payment, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getError().getTppMessage().getCode()).isEqualTo(FORMAT_ERROR);
    }

    @Test
    public void createPaymentInitiation_Failure_account_does_not_exist() {
        // Given
        SinglePayments payment = getPaymentInitiationRequest(WRONG_IBAN, AMOUNT);
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<PaymentInitialisationResponse> actualResponse = paymentService.createPaymentInitiation(payment, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getError().getTppMessage().getCode()).isEqualTo(RESOURCE_UNKNOWN_400);
    }

    @Test
    public void createPaymentInitiation_Failure_payment_not_allowed_to_psu() {
        // Given
        SinglePayments payment = getPaymentInitiationRequest(IBAN, AMOUNT);
        PaymentProduct paymentProduct = PaymentProduct.CBCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<PaymentInitialisationResponse> actualResponse = paymentService.createPaymentInitiation(payment, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getError().getTppMessage().getCode()).isEqualTo(PRODUCT_INVALID);
    }

    @Test
    public void createPaymentInitiation_Failure_ASPSP_rejects_due_to_excessive_amount() {
        // Given
        SinglePayments payment = getPaymentInitiationRequest(IBAN, EXCESSIVE_AMOUNT);
        PaymentProduct paymentProduct = PaymentProduct.SCT;
        boolean tppRedirectPreferred = false;

        //When:
        ResponseObject<PaymentInitialisationResponse> actualResponse = paymentService.createPaymentInitiation(payment, paymentProduct.getCode(), tppRedirectPreferred);

        //Then:
        assertThat(actualResponse.hasError()).isTrue();
        assertThat(actualResponse.getError().getTppMessage().getCode()).isEqualTo(PAYMENT_FAILED);
    }

    private SpiPaymentInitialisationResponse getSpiPaymentResponse(SpiTransactionStatus status) {
        SpiPaymentInitialisationResponse spiPaymentInitialisationResponse = new SpiPaymentInitialisationResponse();
        spiPaymentInitialisationResponse.setTransactionStatus(status);
        spiPaymentInitialisationResponse.setPaymentId(PAYMENT_ID);
        return spiPaymentInitialisationResponse;
    }

    private SinglePayments getPaymentInitiationRequest(String iban, String amountToPay) {
        Amount amount = new Amount();
        amount.setCurrency(CURRENCY);
        amount.setContent(amountToPay);
        BICFI bicfi = new BICFI();
        bicfi.setCode("vnldkvn");
        SinglePayments singlePayments = new SinglePayments();
        singlePayments.setInstructedAmount(amount);
        singlePayments.setDebtorAccount(getReference(iban));
        singlePayments.setCreditorName("Merchant123");
        singlePayments.setPurposeCode(new PurposeCode("BEQNSD"));
        singlePayments.setCreditorAgent(bicfi);
        singlePayments.setCreditorAccount(getReference(iban));
        singlePayments.setPurposeCode(new PurposeCode("BCENECEQ"));
        singlePayments.setRemittanceInformationUnstructured("Ref Number Merchant");

        return singlePayments;
    }

    private AccountReference getReference(String iban) {
        AccountReference reference = new AccountReference();
        reference.setIban(iban);
        reference.setCurrency(CURRENCY);

        return reference;
    }


    private ResponseObject<PaymentInitialisationResponse> readResponseObject() {

        return ResponseObject.<PaymentInitialisationResponse>builder()
                   .body(getPaymentInitializationResponse()).build();
    }

    private PeriodicPayment readPeriodicPayment() throws IOException {
        return jsonConverter.toObject(IOUtils.resourceToString(PERIODIC_PAYMENT_DATA, UTF_8), PeriodicPayment.class).get();
    }

    private PaymentInitialisationResponse getPaymentInitializationResponse() {
        PaymentInitialisationResponse resp = new PaymentInitialisationResponse();
        resp.setTransactionStatus(TransactionStatus.ACCP);
        resp.setLinks(new Links());
        return resp;
    }
}
