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

import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountBalance;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountDetails;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountReference;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiTransaction;
import de.adorsys.aspsp.xs2a.spi.domain.common.SpiAmount;
import de.adorsys.psd2.custom.AccountReference;
import de.adorsys.psd2.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    public AccountDetails mapToAccountDetails(SpiAccountDetails accountDetails) {
        return Optional.ofNullable(accountDetails)
            .map(ad -> new AccountDetails()
                .resourceId(ad.getId())
                .iban(ad.getIban())
                .bban(ad.getBban())
                .msisdn(ad.getMsisdn())
                .currency(ad.getCurrency().getCurrencyCode())
                .name(accountDetails.getName())
                .cashAccountType(ad.getCashSpiAccountType().toString())
                .bic(ad.getBic())
                .balances(mapToBalancesList(ad.getBalances())))
//TODO not need in 1.2 spec
//                           ad.getPan(),
//                           ad.getMaskedPan(),
//                           ad.getAccountType(),

            .orElse(null);
    }

    public Amount mapToAmount(SpiAmount spiAmount) {
        return Optional.ofNullable(spiAmount)
            .map(a -> {
                Amount amount = new Amount();
                amount.setAmount(a.getContent().toString());
                amount.setCurrency(a.getCurrency().toString());
                return amount;
            })
            .orElse(null);
    }

    public Optional<AccountReport> mapToAccountReport(List<SpiTransaction> spiTransactions) {
        if (spiTransactions.isEmpty()) {
            return Optional.empty();
        }

        TransactionList booked = new TransactionList();
        booked.addAll(
            spiTransactions
                .stream()
                .filter(transaction -> transaction.getBookingDate() != null)
                .map(this::mapToTransaction)
                .collect(Collectors.toList()));

        TransactionList pending = new TransactionList();
        pending.addAll(
            spiTransactions
                .stream()
                .filter(transaction -> transaction.getBookingDate() == null)
                .map(this::mapToTransaction)
                .collect(Collectors.toList()));


        return Optional.of(
            new AccountReport()
                .booked(booked)
                .pending(pending)
        );
    }

    public AccountReference mapToAccountReference(SpiAccountReference spiAccountReference) {
        return Optional.ofNullable(spiAccountReference)
            .map(ar -> getAccountReference(ar.getIban(), ar.getBban(), ar.getPan(), ar.getMaskedPan(), ar.getMsisdn(), ar.getCurrency()))
            .orElse(null);

    }

    public List<SpiAccountReference> mapToSpiAccountReferences(List<AccountReference> references) {
        return Optional.ofNullable(references)
            .map(ref -> ref.stream()
                .map(this::mapToSpiAccountReference)
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }

    public SpiAccountReference mapToSpiAccountReference(AccountReference account) {
        return Optional.ofNullable(account)
            .map(ac -> {
                SpiAccountReference accountReference = null;
                if (account instanceof AccountReferenceBban) {
                    accountReference = new SpiAccountReference(null, ((AccountReferenceBban) account).getBban(), null, null, null, account.getCurrency());
                } else if (account instanceof AccountReferenceIban) {
                    accountReference = new SpiAccountReference(((AccountReferenceIban) account).getIban(), null, null, null, null, account.getCurrency());
                } else if (account instanceof AccountReferenceMaskedPan) {
                    accountReference = new SpiAccountReference(null, null, null, ((AccountReferenceMaskedPan) account).getMaskedPan(), null, account.getCurrency());
                } else if (account instanceof AccountReferenceMsisdn) {
                    accountReference = new SpiAccountReference(null, null, null, null, ((AccountReferenceMsisdn) account).getMsisdn(), account.getCurrency());
                } else if (account instanceof AccountReferencePan) {
                    accountReference = new SpiAccountReference(null, null, ((AccountReferencePan) account).getPan(), null, null, account.getCurrency());
                }
                return accountReference;
            })
            .orElse(null);
    }

    public List<AccountReference> mapToAccountReferences(List<SpiAccountReference> references) {
        return Optional.ofNullable(references)
            .map(ref -> ref.stream()
                .map(this::mapToAccountReference)
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }

    private TransactionDetails mapToTransaction(SpiTransaction spiTransaction) {
        return Optional.ofNullable(spiTransaction)
            .map(t -> {
                TransactionDetails transactions = new TransactionDetails();
                transactions.setTransactionAmount(mapToAmount(t.getSpiAmount()));
                transactions.setBankTransactionCode(t.getBankTransactionCodeCode());
                transactions.setBookingDate(t.getBookingDate());
                transactions.setValueDate(t.getValueDate());
                transactions.setCreditorAccount(mapToAccountReference(t.getCreditorAccount()));
                transactions.setDebtorAccount(mapToAccountReference(t.getDebtorAccount()));
                transactions.setCreditorId(t.getCreditorId());
                transactions.setCreditorName(t.getCreditorName());
                transactions.setUltimateCreditor(t.getUltimateCreditor());
                transactions.setDebtorName(t.getDebtorName());
                transactions.setUltimateDebtor(t.getUltimateDebtor());
                transactions.setEndToEndId(t.getEndToEndId());
                transactions.setMandateId(t.getMandateId());
                transactions.setPurposeCode(PurposeCode.fromValue(t.getPurposeCode()));
                transactions.setTransactionId(t.getTransactionId());
                transactions.setRemittanceInformationStructured(t.getRemittanceInformationStructured());
                transactions.setRemittanceInformationUnstructured(t.getRemittanceInformationUnstructured());
                return transactions;
            })
            .orElse(null);
    }

    public List<AccountReference> mapToAccountReferencesFromDetails(List<SpiAccountDetails> details) {
        return Optional.ofNullable(details)
            .map(det -> det.stream()
                .map(this::mapToAccountDetails)
                .map(this::mapToAccountReference)
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }

    private BalanceList mapToBalancesList(List<SpiAccountBalance> spiBalances) {
        if (CollectionUtils.isEmpty(spiBalances)) {
            return new BalanceList();
        }

        BalanceList balances = new BalanceList();
        balances.addAll(spiBalances.stream()
            .map(this::mapToBalance)
            .collect(Collectors.toList()));


        return balances;
    }

    private AccountReference mapToAccountReference(AccountDetails details) {
        return Optional.ofNullable(details)
            .map(det -> getAccountReference(det.getIban(), det.getBban(), null, null, det.getMsisdn(), det.getCurrency()))
            .orElse(null);

    }

    private AccountReference getAccountReference(String iban, String bban, String pan, String maskedPan, String msisdn, String currency) {
        if (StringUtils.isNotEmpty(iban)) {
            return new AccountReferenceIban().iban(iban).currency(currency);
        } else if (StringUtils.isNotEmpty(bban)) {
            return new AccountReferenceBban().bban(bban).currency(currency);
        } else if (StringUtils.isNotEmpty(pan)) {
            return new AccountReferencePan().pan(pan).currency(currency);
        } else if (StringUtils.isNotEmpty(maskedPan)) {
            return new AccountReferenceMaskedPan().maskedPan(maskedPan).currency(currency);
        } else if (StringUtils.isNotEmpty(iban)) {
            return new AccountReferenceMsisdn().msisdn(msisdn).currency(currency);
        }
        return null;
    }

    private Balance mapToBalance(SpiAccountBalance spiAccountBalance) {
        return Optional.ofNullable(spiAccountBalance)
            .map(b -> {
                Balance balance = new Balance();
                balance.setBalanceAmount(mapToAmount(spiAccountBalance.getSpiBalanceAmount()));
                balance.setBalanceType(BalanceType.valueOf(spiAccountBalance.getSpiBalanceType().name()));
                balance.setLastChangeDateTime(spiAccountBalance.getLastChangeDateTime());
                balance.setReferenceDate(spiAccountBalance.getReferenceDate());
                balance.setLastCommittedTransaction(spiAccountBalance.getLastCommittedTransaction());
                return balance;
            })
            .orElse(null);
    }
}
