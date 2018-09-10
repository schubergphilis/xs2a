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

import de.adorsys.aspsp.xs2a.domain.*;
import de.adorsys.aspsp.xs2a.domain.account.*;
import de.adorsys.aspsp.xs2a.domain.code.BankTransactionCode;
import de.adorsys.aspsp.xs2a.domain.code.Xs2aPurposeCode;
import de.adorsys.aspsp.xs2a.spi.domain.account.*;
import de.adorsys.aspsp.xs2a.spi.domain.common.SpiAmount;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AccountMapper {
    public Xs2aAccountDetails mapToAccountDetails(SpiAccountDetails accountDetails) {
        return Optional.ofNullable(accountDetails)
                   .map(ad -> new Xs2aAccountDetails(
                           ad.getId(),
                           ad.getIban(),
                           ad.getBban(),
                           ad.getPan(),
                           ad.getMaskedPan(),
                           ad.getMsisdn(),
                           ad.getCurrency(),
                           ad.getName(),
                           ad.getProduct(),
                           mapToAccountType(ad.getCashSpiAccountType()),
                           mapToAccountStatus(ad.getSpiAccountStatus()),
                           ad.getBic(),
                           ad.getLinkedAccounts(),
                           mapToUsageEnum(ad.getUsageType()),
                           ad.getDetails(),
                           mapToBalancesList(ad.getBalances())
                       )
                   )
                   .orElse(null);
    }

    public Xs2aAmount mapToAmount(SpiAmount spiAmount) {
        return Optional.ofNullable(spiAmount)
                   .map(a -> {
                       Xs2aAmount amount = new Xs2aAmount();
                       amount.setAmount(a.getAmount().toString());
                       amount.setCurrency(a.getCurrency());
                       return amount;
                   })
                   .orElse(null);
    }

    public Optional<Xs2aAccountReport> mapToAccountReport(List<SpiTransaction> spiTransactions) {

        if (spiTransactions.isEmpty()) {
            return Optional.empty();
        }

        Transactions[] booked = spiTransactions
                                    .stream()
                                    .filter(transaction -> transaction.getBookingDate() != null)
                                    .map(this::mapToTransaction)
                                    .toArray(Transactions[]::new);

        Transactions[] pending = spiTransactions
                                     .stream()
                                     .filter(transaction -> transaction.getBookingDate() == null)
                                     .map(this::mapToTransaction)
                                     .toArray(Transactions[]::new);

        return Optional.of(new Xs2aAccountReport(booked, pending));
    }

    public Xs2aAccountReference mapToAccountReference(SpiAccountReference spiAccountReference) {
        return Optional.ofNullable(spiAccountReference)
                   .map(ar -> getAccountReference(ar.getIban(), ar.getBban(), ar.getPan(), ar.getMaskedPan(), ar.getMsisdn(), ar.getCurrency()))
                   .orElse(null);

    }

    public List<SpiAccountReference> mapToSpiAccountReferences(List<Xs2aAccountReference> references) {
        return Optional.ofNullable(references)
                   .map(ref -> ref.stream()
                                   .map(this::mapToSpiAccountReference)
                                   .collect(Collectors.toList()))
                   .orElseGet(Collections::emptyList);
    }

    public SpiAccountReference mapToSpiAccountReference(Xs2aAccountReference account) {
        return Optional.ofNullable(account)
                   .map(ac -> new SpiAccountReference(
                       ac.getIban(),
                       ac.getBban(),
                       ac.getPan(),
                       ac.getMaskedPan(),
                       ac.getMsisdn(),
                       ac.getCurrency()))
                   .orElse(null);
    }

    public List<Xs2aAccountReference> mapToAccountReferences(List<SpiAccountReference> references) {
        return Optional.ofNullable(references)
                   .map(ref -> ref.stream()
                                   .map(this::mapToAccountReference)
                                   .collect(Collectors.toList()))
                   .orElseGet(Collections::emptyList);
    }

    private Transactions mapToTransaction(SpiTransaction spiTransaction) {
        return Optional.ofNullable(spiTransaction)
                   .map(t -> {
                       Transactions transactions = new Transactions();
                       transactions.setTransactionId(t.getTransactionId());
                       transactions.setEntryReference(t.getEntryReference());
                       transactions.setEndToEndId(t.getEndToEndId());
                       transactions.setMandateId(t.getMandateId());
                       transactions.setCheckId(t.getCheckId());
                       transactions.setCreditorId(t.getCreditorId());
                       transactions.setBookingDate(t.getBookingDate());
                       transactions.setValueDate(t.getValueDate());
                       transactions.setAmount(mapToAmount(t.getSpiAmount()));
                       transactions.setExchangeRate(mapToExchangeRateList(t.getExchangeRate()));
                       transactions.setCreditorName(t.getCreditorName());
                       transactions.setCreditorAccount(mapToAccountReference(t.getCreditorAccount()));
                       transactions.setUltimateCreditor(t.getUltimateCreditor());
                       transactions.setDebtorName(t.getDebtorName());
                       transactions.setDebtorAccount(mapToAccountReference(t.getDebtorAccount()));
                       transactions.setUltimateDebtor(t.getUltimateDebtor());
                       transactions.setRemittanceInformationUnstructured(t.getRemittanceInformationUnstructured());
                       transactions.setRemittanceInformationStructured(t.getRemittanceInformationStructured());
                       transactions.setPurposeCode(new Xs2aPurposeCode(t.getPurposeCode()));
                       transactions.setBankTransactionCodeCode(new BankTransactionCode(t.getBankTransactionCodeCode()));
                       transactions.setProprietaryBankTransactionCode(t.getProprietaryBankTransactionCode());
                       return transactions;
                   })
                   .orElse(null);
    }

    public List<Xs2aAccountReference> mapToAccountReferencesFromDetails(List<SpiAccountDetails> details) {
        return Optional.ofNullable(details)
                   .map(det -> det.stream()
                                   .map(this::mapToAccountDetails)
                                   .map(this::mapToAccountReference)
                                   .collect(Collectors.toList()))
                   .orElseGet(Collections::emptyList);
    }

    public List<SpiAccountReference> mapToSpiAccountReferencesFromDetails(List<SpiAccountDetails> details) {
        return Optional.ofNullable(details)
                   .map(det -> det.stream()
                                   .map(spiDetail -> new SpiAccountReference(
                                       spiDetail.getIban(),
                                       spiDetail.getBban(),
                                       spiDetail.getPan(),
                                       spiDetail.getMaskedPan(),
                                       spiDetail.getMsisdn(),
                                       spiDetail.getCurrency()
                                   ))
                                   .collect(Collectors.toList()))
                   .orElseGet(Collections::emptyList);
    }

    private List<Xs2aBalance> mapToBalancesList(List<SpiAccountBalance> spiBalances) {
        if (CollectionUtils.isEmpty(spiBalances)) {
            return new ArrayList<>();
        }

        return spiBalances.stream()
                   .map(this::mapToBalance)
                   .collect(Collectors.toList());
    }

    private Xs2aAccountReference mapToAccountReference(Xs2aAccountDetails details) {
        return Optional.ofNullable(details)
                   .map(det -> getAccountReference(det.getIban(), det.getBban(), det.getPan(), det.getMaskedPan(), det.getMsisdn(), det.getCurrency()))
                   .orElse(null);
    }

    private List<Xs2aExchangeRate> mapToExchangeRateList(List<SpiExchangeRate> spiExchangeRates) {
        if (CollectionUtils.isEmpty(spiExchangeRates)) {
            return new ArrayList<>();
        }

        return spiExchangeRates.stream()
                   .map(this::mapToExchangeRate)
                   .collect(Collectors.toList());
    }

    private Xs2aExchangeRate mapToExchangeRate(SpiExchangeRate spiExchangeRate) {
        return Optional.ofNullable(spiExchangeRate)
                   .map(e -> {
                       Xs2aExchangeRate exchangeRate = new Xs2aExchangeRate();
                       exchangeRate.setCurrencyFrom(e.getCurrencyFrom());
                       exchangeRate.setRateFrom(e.getRateFrom());
                       exchangeRate.setCurrencyTo(e.getCurrencyTo());
                       exchangeRate.setRateTo(e.getRateTo());
                       exchangeRate.setRateDate(e.getRateDate());
                       exchangeRate.setRateContract(e.getRateContract());
                       return exchangeRate;
                   })
                   .orElse(null);
    }

    private Xs2aAccountReference getAccountReference(String iban, String bban, String pan, String maskedPan, String msisdn, Currency currency) {
        Xs2aAccountReference reference = new Xs2aAccountReference();
        reference.setIban(iban);
        reference.setBban(bban);
        reference.setPan(pan);
        reference.setMaskedPan(maskedPan);
        reference.setMsisdn(msisdn);
        reference.setCurrency(currency);
        return reference;
    }

    private CashAccountType mapToAccountType(SpiAccountType spiAccountType) {
        return Optional.ofNullable(spiAccountType)
                   .map(type -> CashAccountType.valueOf(type.name()))
                   .orElse(null);
    }

    private AccountStatus mapToAccountStatus(SpiAccountStatus spiAccountStatus) {
        return Optional.ofNullable(spiAccountStatus)
                   .map(status -> AccountStatus.valueOf(status.name()))
                   .orElse(null);
    }

    private UsageEnum mapToUsageEnum(SpiUsageType spiUsageType) {
        return Optional.ofNullable(spiUsageType)
                   .map(usage -> UsageEnum.valueOf(usage.name()))
                   .orElse(null);
    }

    private Xs2aBalance mapToBalance(SpiAccountBalance spiAccountBalance) {
        return Optional.ofNullable(spiAccountBalance)
                   .map(b -> {
                       Xs2aBalance balance = new Xs2aBalance();
                       balance.setBalanceAmount(mapToAmount(spiAccountBalance.getSpiBalanceAmount()));
                       balance.setBalanceType(BalanceType.valueOf(spiAccountBalance.getSpiBalanceType().name()));
                       balance.setLastChangeDateTime(spiAccountBalance.getLastChangeDateTime());
                       balance.setReferenceDate(spiAccountBalance.getReferenceDate());
                       balance.setLastCommittedTransaction(spiAccountBalance.getLastCommittedTransaction());
                       return balance;
                   })
                   .orElse(null);
    }

    public List<Xs2aAccountDetails> mapToAccountDetailsListNoBalances(List<Xs2aAccountDetails> details) {
        return details.stream()
                   .map(this::mapToAccountDetailNoBalances)
                   .collect(Collectors.toList());
    }

    public Xs2aAccountDetails mapToAccountDetailNoBalances(Xs2aAccountDetails detail) {
        return new Xs2aAccountDetails(detail.getId(), detail.getIban(), detail.getBban(), detail.getPan(),
            detail.getMaskedPan(), detail.getMsisdn(), detail.getCurrency(), detail.getName(),
            detail.getProduct(), detail.getCashAccountType(), detail.getAccountStatus(), detail.getBic(), detail.getLinkedAccounts(), detail.getUsageEnum(), detail.getDetails(), null);
    }
}
