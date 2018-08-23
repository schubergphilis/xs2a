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
import de.adorsys.aspsp.xs2a.domain.account.AccountReference;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationRequest;
import de.adorsys.psd2.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FundsConfirmationMapper {

    private final ObjectMapper objectMapper;

    public FundsConfirmationRequest mapToFundsConfirmationRequest(ConfirmationOfFunds confirmationOfFunds) {
        return Optional.ofNullable(confirmationOfFunds)
                   .map(conf -> {
                       FundsConfirmationRequest fundsConfirmationRequest = new FundsConfirmationRequest();
                       fundsConfirmationRequest.setCardNumber(conf.getCardNumber());
                       fundsConfirmationRequest.setPayee(conf.getPayee());
                       fundsConfirmationRequest.setPsuAccount(mapToAccountReferenceInner(conf.getAccount()));
                       fundsConfirmationRequest.setInstructedAmount(mapToAmount(conf.getInstructedAmount()));
                       return fundsConfirmationRequest;
                   })
                   .orElse(null);
    }

    public de.adorsys.aspsp.xs2a.domain.Amount mapToAmount(Amount amount) {
        de.adorsys.aspsp.xs2a.domain.Amount amountTarget = new de.adorsys.aspsp.xs2a.domain.Amount();
        amountTarget.setContent(amount.getAmount());
        amountTarget.setCurrency(getCurrencyByCode(amount.getCurrency()));
        return amountTarget;
    }


    private AccountReference mapToAccountReferenceInner(Object reference) {
        return objectMapper.convertValue(reference, AccountReference.class);
    }

    private Currency getCurrencyByCode(String code) {
        return Optional.ofNullable(code)
                   .map(Currency::getInstance)
                   .orElseGet(null);
    }
}
