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

import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationRequest;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationResponse;
import de.adorsys.aspsp.xs2a.exception.MessageError;
import de.adorsys.aspsp.xs2a.service.mapper.spi_xs2a_mappers.SpiXs2aAccountMapper;
import de.adorsys.aspsp.xs2a.spi.domain.SpiResponse;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountReference;
import de.adorsys.aspsp.xs2a.spi.domain.common.SpiAmount;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.service.FundsConfirmationSpi;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FundsConfirmationService {
    private final AccountReferenceValidationService referenceValidationService;
    private final FundsConfirmationSpi fundsConfirmationSpi;
    private final SpiXs2aAccountMapper accountMapper;

    /**
     * Checks if the account balance is sufficient for requested operation
     *
     * @param request Contains the requested balanceAmount in order to comparing with available balanceAmount on account
     * @return Response with result 'true' if there are enough funds on the account, 'false' if not
     */
    public ResponseObject<FundsConfirmationResponse> fundsConfirmation(FundsConfirmationRequest request) {
        ResponseObject accountReferenceValidationResponse = referenceValidationService.validateAccountReferences(request.getAccountReferences());

        if (accountReferenceValidationResponse.hasError()) {
            return ResponseObject.<FundsConfirmationResponse>builder()
                       .fail(accountReferenceValidationResponse.getError())
                       .build();
        }

        SpiAccountReference accountReference = accountMapper.mapToSpiAccountReference(request.getPsuAccount());
        SpiAmount amount = accountMapper.mapToSpiAmount(request.getInstructedAmount());

        AspspConsentData aspspConsentData = new AspspConsentData(); // TODO read it https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/379

        SpiResponse<Boolean> fundsSufficientCheck = fundsConfirmationSpi.peformFundsSufficientCheck(
            accountReference,
            amount,
            aspspConsentData);

        aspspConsentData = fundsSufficientCheck.getAspspConsentData(); // TODO save it https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/379

        if (fundsSufficientCheck.hasError()) {
            return ResponseObject.<FundsConfirmationResponse>builder()
                       .fail(new MessageError(MessageErrorCode.PAYMENT_FAILED)) // TODO put real error from SpiResponse.message
                       .build();
        }

        FundsConfirmationResponse fundsConfirmationResponse = new FundsConfirmationResponse(BooleanUtils.isTrue(fundsSufficientCheck.getPayload()));

        return ResponseObject.<FundsConfirmationResponse>builder()
                   .body(fundsConfirmationResponse)
                   .build();
    }
}
