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

package de.adorsys.aspsp.aspspmockserver.service;

import de.adorsys.aspsp.aspspmockserver.repository.PaymentRepository;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountBalance;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountDetails;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountReference;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiBalances;
import de.adorsys.aspsp.xs2a.spi.domain.common.SpiAmount;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPeriodicPayment;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiSinglePayments;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountService accountService;

    /**
     * Checks if there is enough funds for payment and if so saves the payment
     *
     * @param payment Single payment
     * @return Optional of saved single payment
     */
    public Optional<SpiSinglePayments> addPayment(@NotNull SpiSinglePayments payment) {
        return isFundsSufficient(payment.getDebtorAccount(), payment.getInstructedAmount().getContent())
                   ? Optional.ofNullable(paymentRepository.save(payment))
                   : Optional.empty();
    }

    /**
     * Saves a periodic payment
     *
     * @param payment Periodic payment
     * @return Optional of saved periodic payment
     */
    public Optional<SpiPeriodicPayment> addPeriodicPayment(@NotNull SpiPeriodicPayment payment) {
        return Optional.ofNullable(paymentRepository.save(payment));
    }

    /**
     * Checks if payment is registered at ASPSP
     *
     * @param paymentId Payments primary ASPSP identifier
     * @return boolean representation of payments presence
     */
    public boolean isPaymentExist(String paymentId) {
        return paymentRepository.exists(paymentId);
    }

    /**
     * Saves a bulk payment
     *
     * @param payments Bulk payment
     * @return list of single payments forming bulk payment
     */
    public List<SpiSinglePayments> addBulkPayments(List<SpiSinglePayments> payments) {
        return paymentRepository.save(payments);
    }

    BigDecimal calculateAmountToBeCharged(String accountId) {
        return paymentRepository.findAll().stream()
                   .filter(paym -> getDebtorAccountIdFromPayment(paym).equals(accountId))
                   .map(this::getAmountFromPayment)
                   .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isFundsSufficient(SpiAccountReference reference, BigDecimal amount) {
        Optional<SpiAccountBalance> balance = Optional.ofNullable(reference)
                                                  .flatMap(this::getBalance);
        return balance
                   .map(b -> b.getSpiAmount().getContent().compareTo(amount) > 0)
                   .orElse(false);
    }

    private Optional<SpiAccountBalance> getBalance(SpiAccountReference reference) {
        List<SpiAccountDetails> accountsByIban = accountService.getAccountsByIban(reference.getIban());
        return getDetails(accountsByIban, reference)
                   .flatMap(SpiAccountDetails::getFirstBalance)
                   .map(SpiBalances::getInterimAvailable);
    }

    private Optional<SpiAccountDetails> getDetails(List<SpiAccountDetails> accounts, SpiAccountReference reference) {
        return Optional.ofNullable(accounts)
                   .flatMap(accs -> accs.stream()
                                        .filter(ac -> ac.getCurrency() == reference.getCurrency())
                                        .findFirst());
    }

    private String getDebtorAccountIdFromPayment(SpiSinglePayments payment) {
        return Optional.ofNullable(payment.getDebtorAccount())
                   .map(SpiAccountReference::getIban)
                   .orElse("");
    }

    private BigDecimal getAmountFromPayment(SpiSinglePayments payment) {
        return Optional.ofNullable(payment)
                   .map(paym -> getContentFromAmount(payment.getInstructedAmount()))
                   .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getContentFromAmount(SpiAmount amount) {
        return Optional.ofNullable(amount)
                   .map(SpiAmount::getContent)
                   .orElse(BigDecimal.ZERO);
    }
}
