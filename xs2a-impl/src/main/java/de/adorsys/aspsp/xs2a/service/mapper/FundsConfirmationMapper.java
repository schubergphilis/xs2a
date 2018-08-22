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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FundsConfirmationMapper {

    private final ObjectMapper objectMapper;

    public FundsConfirmationRequest mapToFundsConfirmationRequest(ConfirmationOfFunds fundsConfirmationRequest){
        FundsConfirmationRequest fcr = new FundsConfirmationRequest();
        BeanUtils.copyProperties(fundsConfirmationRequest, fcr);
        fcr.setPsuAccount(mapToAccountReferenceInner(fundsConfirmationRequest.getAccount()));
        Amount instructedAmount = fundsConfirmationRequest.getInstructedAmount();
        de.adorsys.aspsp.xs2a.domain.Amount amount = new de.adorsys.aspsp.xs2a.domain.Amount();
        amount.setContent(instructedAmount.getAmount());
        amount.setCurrency(getCurrencyByCode(instructedAmount.getCurrency()));
        fcr.setInstructedAmount(amount);
        return fcr;
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
