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

package de.adorsys.aspsp.xs2a.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.domain.consent.Xs2aAuthenticationObject;
import de.adorsys.aspsp.xs2a.domain.Xs2aChallengeData;
import de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus;
import de.adorsys.aspsp.xs2a.domain.pis.*;
import de.adorsys.aspsp.xs2a.service.message.MessageService;
import de.adorsys.psd2.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.PERIODIC;
import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.SINGLE;
import static de.adorsys.aspsp.xs2a.service.mapper.AccountModelMapper.*;
import static de.adorsys.aspsp.xs2a.service.mapper.AmountModelMapper.mapToAmount;

@Component
@RequiredArgsConstructor
public class PaymentModelMapperPsd2 {
    private final ObjectMapper mapper;
    private final MessageService messageService;

    public Object mapToGetPaymentResponse12(Object payment, PaymentType type, PaymentProduct product) {
        if (type == SINGLE) {
            SinglePayment xs2aPayment = (SinglePayment) payment;
            PaymentInitiationTarget2WithStatusResponse paymentResponse = new PaymentInitiationTarget2WithStatusResponse();
            paymentResponse.setEndToEndIdentification(xs2aPayment.getEndToEndIdentification());
            paymentResponse.setDebtorAccount(mapToAccountReference12(xs2aPayment.getDebtorAccount()));
            paymentResponse.setInstructedAmount(mapToAmount(xs2aPayment.getInstructedAmount()));
            paymentResponse.setCreditorAccount(mapToAccountReference12(xs2aPayment.getCreditorAccount()));
            paymentResponse.setCreditorAgent(xs2aPayment.getCreditorAgent());
            paymentResponse.setCreditorName(xs2aPayment.getCreditorName());
            paymentResponse.setCreditorAddress(mapToAddress12(xs2aPayment.getCreditorAddress()));
            paymentResponse.setRemittanceInformationUnstructured(xs2aPayment.getRemittanceInformationUnstructured());
            paymentResponse.setTransactionStatus(mapToTransactionStatus12(xs2aPayment.getTransactionStatus()));
            return paymentResponse;
        } else if (type == PERIODIC) {
            PeriodicPayment xs2aPayment = (PeriodicPayment) payment;
            PeriodicPaymentInitiationTarget2WithStatusResponse paymentResponse = new PeriodicPaymentInitiationTarget2WithStatusResponse();

            paymentResponse.setEndToEndIdentification(xs2aPayment.getEndToEndIdentification());
            paymentResponse.setDebtorAccount(mapToAccountReference12(xs2aPayment.getDebtorAccount()));
            paymentResponse.setInstructedAmount(mapToAmount(xs2aPayment.getInstructedAmount()));
            paymentResponse.setCreditorAccount(mapToAccountReference12(xs2aPayment.getCreditorAccount()));
            paymentResponse.setCreditorAgent(xs2aPayment.getCreditorAgent());
            paymentResponse.setCreditorName(xs2aPayment.getCreditorName());
            paymentResponse.setCreditorAddress(mapToAddress12(xs2aPayment.getCreditorAddress()));
            paymentResponse.setRemittanceInformationUnstructured(xs2aPayment.getRemittanceInformationUnstructured());
            paymentResponse.setStartDate(xs2aPayment.getStartDate());
            paymentResponse.setEndDate(xs2aPayment.getEndDate());
            paymentResponse.setExecutionRule(ExecutionRule.valueOf(xs2aPayment.getExecutionRule()));
            paymentResponse.setFrequency(FrequencyCode.valueOf(xs2aPayment.getFrequency().name()));
            String executionDateString = String.format("%02d", xs2aPayment.getDayOfExecution());
            paymentResponse.setDayOfExecution(DayOfExecution.fromValue(executionDateString));
            paymentResponse.setTransactionStatus(mapToTransactionStatus12(xs2aPayment.getTransactionStatus()));
            return paymentResponse;
        } else {
            BulkPayment xs2aPayment = (BulkPayment) payment;
            BulkPaymentInitiationTarget2WithStatusResponse paymentResponse = new BulkPaymentInitiationTarget2WithStatusResponse();

            paymentResponse.setBatchBookingPreferred(xs2aPayment.getBatchBookingPreferred());
            paymentResponse.setRequestedExecutionDate(xs2aPayment.getRequestedExecutionDate());
            paymentResponse.setDebtorAccount(mapToAccountReference12(xs2aPayment.getDebtorAccount()));
            paymentResponse.setPayments(mapToBulkPartList12(xs2aPayment.getPayments()));
            paymentResponse.setTransactionStatus(mapToTransactionStatus12(Xs2aTransactionStatus.RCVD)); //TODO add field to xs2a entity https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/243
            return paymentResponse;
        }
    }

    public static PaymentInitiationStatusResponse200Json mapToStatusResponse12(Xs2aTransactionStatus status) {
        return new PaymentInitiationStatusResponse200Json().transactionStatus(mapToTransactionStatus12(status));
    }

    public static TransactionStatus mapToTransactionStatus12(Xs2aTransactionStatus responseObject) {
        return TransactionStatus.valueOf(responseObject.name());
    }

    public Object mapToPaymentInitiationResponse12(Object response, PaymentType type, PaymentProduct product) {
        PaymentInitationRequestResponse201 response201 = new PaymentInitationRequestResponse201();
        if (type == SINGLE || type == PERIODIC) {
            PaymentInitialisationResponse specificResponse = (PaymentInitialisationResponse) response;
            response201.setTransactionStatus(mapToTransactionStatus12(specificResponse.getTransactionStatus()));
            response201.setPaymentId(specificResponse.getPaymentId());
            response201.setTransactionFees(mapToAmount(specificResponse.getTransactionFees()));
            response201.setTransactionFeeIndicator(specificResponse.isTransactionFeeIndicator());
            response201.setScaMethods(mapToScaMethods(specificResponse.getScaMethods()));
            response201.setChosenScaMethod(null); // TODO add proper mapping
            response201.setChallengeData(mapToChallengeData(specificResponse.getChallengeData()));
            response201.setLinks(mapper.convertValue(((PaymentInitialisationResponse) response).getLinks(), Map.class)); //TODO add new mapper for Links https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/244
            response201.setPsuMessage(specificResponse.getPsuMessage());
            response201.setTppMessages(mapToTppMessages(specificResponse.getTppMessages())); //TODO add new Mapper https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/242
            return response201;
        } else {
            List<PaymentInitialisationResponse> specificResponse = (List<PaymentInitialisationResponse>) response;
            return specificResponse.stream()
                       .map(r -> mapToPaymentInitiationResponse12(r, SINGLE, product))
                       .collect(Collectors.toList());
        }
    }

    private List<PaymentInitiationTarget2Json> mapToBulkPartList12(List<SinglePayment> payments) {
        return payments.stream()
                   .map(PaymentModelMapperPsd2::mapToBulkPart12)
                   .collect(Collectors.toList());
    }

    private static PaymentInitiationTarget2Json mapToBulkPart12(SinglePayment payment) {
        PaymentInitiationTarget2Json bulkPart = new PaymentInitiationTarget2Json().endToEndIdentification(payment.getEndToEndIdentification());
        bulkPart.setDebtorAccount(mapToAccountReference12(payment.getDebtorAccount()));
        bulkPart.setInstructedAmount(mapToAmount(payment.getInstructedAmount()));
        bulkPart.setCreditorAccount(mapToAccountReference12(payment.getCreditorAccount()));
        bulkPart.setCreditorAgent(payment.getCreditorAgent());
        bulkPart.setCreditorName(payment.getCreditorName());
        bulkPart.setCreditorAddress(mapToAddress12(payment.getCreditorAddress()));
        bulkPart.setRemittanceInformationUnstructured(payment.getRemittanceInformationUnstructured());
        return bulkPart;
    }

    private TppMessages mapToTppMessages(MessageErrorCode... tppMessages) {
        return Optional.ofNullable(tppMessages)
                   .map(m -> Arrays.stream(m)
                                 .map(this::mapToGenericError)
                                 .collect(Collectors.toList()))
                   .map(c -> {
                       TppMessages messages = new TppMessages();
                       messages.addAll(c);
                       return messages;
                   })
                   .orElse(null);
    }

    private TppMessageGeneric mapToGenericError(MessageErrorCode code) {
        TppMessageGeneric tppMessage = new TppMessageGeneric();
        tppMessage.setCategory(TppMessageCategory.ERROR);
        tppMessage.setCode(code);
        tppMessage.setPath("N/A"); //TODO set path
        tppMessage.setText(messageService.getMessage(code.name()));
        return tppMessage;
    }

    private ScaMethods mapToScaMethods(Xs2aAuthenticationObject... authenticationObjects) {
        return Optional.ofNullable(authenticationObjects)
                   .map(objects -> {
                       ScaMethods scaMethods = new ScaMethods();
                       Arrays.stream(objects)
                           .map(this::mapToAuthenticationObject)
                           .filter(Objects::nonNull)
                           .forEach(scaMethods::add);
                       return scaMethods;
                   })
                   .orElse(null);
    }

    private AuthenticationObject mapToAuthenticationObject(Xs2aAuthenticationObject xs2aAuthenticationObject) {
        return Optional.ofNullable(xs2aAuthenticationObject)
                   .map(a -> {
                       AuthenticationObject psd2Authentication = new AuthenticationObject();
                       psd2Authentication.setAuthenticationType(AuthenticationType.fromValue(a.getAuthenticationType().name()));
                       psd2Authentication.setAuthenticationVersion(a.getAuthenticationVersion());
                       psd2Authentication.setAuthenticationMethodId(a.getAuthenticationMethodId());
                       psd2Authentication.setName(a.getName());
                       psd2Authentication.setExplanation(a.getExplanation());
                       return psd2Authentication;
                   })
                   .orElse(null);
    }

    private ChallengeData mapToChallengeData(Xs2aChallengeData xs2aChallengeData) {
        return Optional.ofNullable(xs2aChallengeData)
                   .map(xs2aChallenge -> {
                       ChallengeData psd2Challenge = new ChallengeData();
                       psd2Challenge.setImage(xs2aChallengeData.getImage());
                       psd2Challenge.setData(xs2aChallengeData.getData());
                       psd2Challenge.setImageLink(xs2aChallengeData.getImageLink());
                       psd2Challenge.setOtpMaxLength(xs2aChallengeData.getOtpMaxLength());
                       psd2Challenge.setOtpFormat(ChallengeData.OtpFormatEnum.fromValue(xs2aChallengeData.getOtpFormat().getValue()));
                       psd2Challenge.setAdditionalInformation(xs2aChallengeData.getAdditionalInformation());
                       return psd2Challenge;
                   })
                   .orElse(null);
    }
}
