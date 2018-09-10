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
import de.adorsys.aspsp.xs2a.domain.Xs2aChallengeData;
import de.adorsys.aspsp.xs2a.domain.Xs2aTransactionStatus;
import de.adorsys.aspsp.xs2a.domain.account.Xs2aAccountReference;
import de.adorsys.aspsp.xs2a.domain.code.Xs2aFrequencyCode;
import de.adorsys.aspsp.xs2a.domain.code.Xs2aPurposeCode;
import de.adorsys.aspsp.xs2a.domain.consent.AuthenticationObject;
import de.adorsys.aspsp.xs2a.domain.pis.*;
import de.adorsys.aspsp.xs2a.service.message.MessageService;
import de.adorsys.aspsp.xs2a.service.validator.ValueValidatorService;
import de.adorsys.psd2.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.PERIODIC;
import static de.adorsys.aspsp.xs2a.domain.pis.PaymentType.SINGLE;
import static de.adorsys.aspsp.xs2a.service.mapper.AccountModelMapper.*;
import static de.adorsys.aspsp.xs2a.service.mapper.AmountModelMapper.mapToAmount;

@Component
@RequiredArgsConstructor
public class PaymentModelMapper {
    private final ObjectMapper mapper;
    private final ValueValidatorService validationService;
    private final MessageService messageService;

    // Mappers into xs2a domain classes
    public Object mapToXs2aPayment(Object payment, PaymentType type, PaymentProduct product) {
        if (type == SINGLE) {
            return mapToXs2aSinglePayment(validatePayment(payment, PaymentInitiationSctJson.class));
        } else if (type == PERIODIC) {
            return mapToXs2aPeriodicPayment(validatePayment(payment, PeriodicPaymentInitiationSctJson.class));
        } else {
            return mapToXs2aBulkPayment(validatePayment(payment, BulkPaymentInitiationSctJson.class));
        }
    }

    private <R> R validatePayment(Object payment, Class<R> clazz) {
        R result = mapper.convertValue(payment, clazz);
        validationService.validate(result);
        return result;
    }

    private SinglePayment mapToXs2aSinglePayment(PaymentInitiationSctJson paymentRequest) {
        SinglePayment payment = new SinglePayment();

        payment.setEndToEndIdentification(paymentRequest.getEndToEndIdentification());
        payment.setDebtorAccount(mapToXs2aAccountReference(paymentRequest.getDebtorAccount()));
        payment.setUltimateDebtor("NOT SUPPORTED");
        payment.setInstructedAmount(mapToXs2aAmount(paymentRequest.getInstructedAmount()));
        payment.setCreditorAccount(mapToXs2aAccountReference(paymentRequest.getCreditorAccount()));
        payment.setCreditorAgent(paymentRequest.getCreditorAgent());
        payment.setCreditorName(paymentRequest.getCreditorName());
        payment.setCreditorAddress(mapToXs2aAddress(paymentRequest.getCreditorAddress()));
        payment.setUltimateCreditor(paymentRequest.getCreditorName());
        payment.setPurposeCode(new Xs2aPurposeCode("N/A"));
        payment.setRemittanceInformationUnstructured(paymentRequest.getRemittanceInformationUnstructured());
        payment.setRemittanceInformationStructured(new Remittance());
        payment.setRequestedExecutionDate(LocalDate.now());
        payment.setRequestedExecutionTime(LocalDateTime.now().plusHours(1));
        return payment;
    }

    private Xs2aAccountReference mapToXs2aAccountReference(Object reference12) {
        return mapper.convertValue(reference12, Xs2aAccountReference.class);
    }

    private PeriodicPayment mapToXs2aPeriodicPayment(PeriodicPaymentInitiationSctJson paymentRequest) {
        PeriodicPayment payment = new PeriodicPayment();

        payment.setEndToEndIdentification(paymentRequest.getEndToEndIdentification());
        payment.setDebtorAccount(mapToXs2aAccountReference(paymentRequest.getDebtorAccount()));
        payment.setUltimateDebtor("NOT SUPPORTED");
        payment.setInstructedAmount(mapToXs2aAmount(paymentRequest.getInstructedAmount()));
        payment.setCreditorAccount(mapToXs2aAccountReference(paymentRequest.getCreditorAccount()));
        payment.setCreditorAgent(paymentRequest.getCreditorAgent());
        payment.setCreditorName(paymentRequest.getCreditorName());
        payment.setCreditorAddress(mapToXs2aAddress(paymentRequest.getCreditorAddress()));
        payment.setUltimateCreditor(paymentRequest.getCreditorName());
        payment.setPurposeCode(new Xs2aPurposeCode("N/A"));
        payment.setRemittanceInformationUnstructured(paymentRequest.getRemittanceInformationUnstructured());
        payment.setRemittanceInformationStructured(new Remittance());
        payment.setRequestedExecutionDate(LocalDate.now());
        payment.setRequestedExecutionTime(LocalDateTime.now().plusHours(1));

        payment.setStartDate(paymentRequest.getStartDate());
        payment.setExecutionRule(Optional.ofNullable(paymentRequest.getExecutionRule()).map(ExecutionRule::toString).orElse(null));
        payment.setEndDate(paymentRequest.getEndDate());
        payment.setFrequency(mapToXs2aFrequencyCode(paymentRequest.getFrequency()));
        payment.setDayOfExecution(Integer.parseInt(paymentRequest.getDayOfExecution().toString()));
        return payment;
    }

    private Xs2aFrequencyCode mapToXs2aFrequencyCode(FrequencyCode frequency) {
        return Xs2aFrequencyCode.valueOf(frequency.name());
    }

    private BulkPayment mapToXs2aBulkPayment(BulkPaymentInitiationSctJson paymentRequest) {
        BulkPayment bulkPayment = new BulkPayment();
        bulkPayment.setBatchBookingPreferred(paymentRequest.getBatchBookingPreferred());
        bulkPayment.setDebtorAccount(mapToXs2aAccountReference(paymentRequest.getDebtorAccount()));
        bulkPayment.setRequestedExecutionDate(paymentRequest.getRequestedExecutionDate());

        List<SinglePayment> payments = paymentRequest.getPayments().stream()
                                           .map(p -> {
                                               SinglePayment payment = new SinglePayment();
                                               payment.setDebtorAccount(
                                                   mapToXs2aAccountReference(paymentRequest.getDebtorAccount()));
                                               payment.setRequestedExecutionDate(
                                                   paymentRequest.getRequestedExecutionDate());
                                               payment.setEndToEndIdentification(p.getEndToEndIdentification());
                                               payment.setUltimateDebtor("NOT SUPPORTED");
                                               payment.setInstructedAmount(mapToXs2aAmount(p.getInstructedAmount()));
                                               payment.setCreditorAccount(
                                                   mapToXs2aAccountReference(p.getCreditorAccount()));
                                               payment.setCreditorAgent(p.getCreditorAgent());
                                               payment.setCreditorName(p.getCreditorName());
                                               payment.setCreditorAddress(mapToXs2aAddress(p.getCreditorAddress()));
                                               payment.setUltimateCreditor(null);
                                               payment.setPurposeCode(new Xs2aPurposeCode(null));
                                               payment.setRemittanceInformationUnstructured(
                                                   p.getRemittanceInformationUnstructured());
                                               payment.setRemittanceInformationStructured(new Remittance());
                                               payment.setRequestedExecutionTime(LocalDateTime.now().plusHours(1));
                                               return payment;
                                           })
                                           .collect(Collectors.toList());
        bulkPayment.setPayments(payments);

        return bulkPayment;
    }

    //Mappers into PSD2 generated API model classes
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
            paymentResponse.setTransactionStatus(mapToTransactionStatus12(Xs2aTransactionStatus.RCVD)); //TODO add field to xs2a entity https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/243
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
            paymentResponse.setTransactionStatus(mapToTransactionStatus12(Xs2aTransactionStatus.RCVD)); //TODO add field to xs2a entity https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/243
            return paymentResponse;
        } else {
            List<SinglePayment> xs2aPayment = (List<SinglePayment>) payment;
            BulkPaymentInitiationTarget2WithStatusResponse paymentResponse = new BulkPaymentInitiationTarget2WithStatusResponse();

            paymentResponse.setBatchBookingPreferred(false); //TODO create entity and add value! https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/243
            paymentResponse.setRequestedExecutionDate(LocalDate.now()); //TODO create entity and add field! https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/243
            paymentResponse.setDebtorAccount(mapToAccountReference12(xs2aPayment.get(0).getDebtorAccount())); //TODO create entity and add field! https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/243
            paymentResponse.setPayments(mapToBulkPartList12(xs2aPayment));
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
            response201.setChosenScaMethod(mapToChosenScaMethod(specificResponse.getChosenScaMethod()));
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
                   .map(PaymentModelMapper::mapToBulkPart12)
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

    private ScaMethods mapToScaMethods(AuthenticationObject... authenticationObjects) {
        return Optional.ofNullable(authenticationObjects)
                   .map(objects -> (ScaMethods) Arrays.stream(objects)
                                       .map(this::mapToAuthenticationObject)
                                       .collect(Collectors.toCollection(ArrayList::new)))
                   .orElse(null);
    }

    private ChosenScaMethod mapToChosenScaMethod(AuthenticationObject authenticationObject) { //NOPMD
        return null; // TODO Properly map AuthenticationObject to ChosenScaMethod
    }

    private de.adorsys.psd2.model.AuthenticationObject mapToAuthenticationObject(AuthenticationObject xs2aAuthenticationObject) {
        de.adorsys.psd2.model.AuthenticationObject authenticationObject = new de.adorsys.psd2.model.AuthenticationObject();
        authenticationObject.setAuthenticationType(AuthenticationType.fromValue(xs2aAuthenticationObject.getAuthenticationType().name()));
        authenticationObject.setAuthenticationVersion(xs2aAuthenticationObject.getAuthenticationVersion());
        authenticationObject.setAuthenticationMethodId(xs2aAuthenticationObject.getAuthenticationMethodId());
        authenticationObject.setName(xs2aAuthenticationObject.getName());
        authenticationObject.setExplanation(xs2aAuthenticationObject.getExplanation());
        return authenticationObject;
    }

    private ChallengeData mapToChallengeData(Xs2aChallengeData xs2aChallengeData) {
        ChallengeData challengeData = new ChallengeData();
        challengeData.setImage(xs2aChallengeData.getImage());
        challengeData.setData(xs2aChallengeData.getData());
        challengeData.setImageLink(xs2aChallengeData.getImageLink());
        challengeData.setOtpMaxLength(xs2aChallengeData.getOtpMaxLength());
        challengeData.setOtpFormat(ChallengeData.OtpFormatEnum.fromValue(xs2aChallengeData.getOtpFormat().getValue()));
        challengeData.setAdditionalInformation(xs2aChallengeData.getAdditionalInformation());
        return challengeData;
    }
}
