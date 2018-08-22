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

package de.adorsys.aspsp.xs2a.web12;

import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.account.AccountReference;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationRequest;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationResponse;
import de.adorsys.aspsp.xs2a.service.AccountReferenceValidationService;
import de.adorsys.aspsp.xs2a.service.FundsConfirmationService;
import de.adorsys.aspsp.xs2a.service.mapper.FundsConfirmationMapper;
import de.adorsys.aspsp.xs2a.service.mapper.ResponseMapper;
import de.adorsys.psd2.api.FundsConfirmationApi;
import de.adorsys.psd2.model.ConfirmationOfFunds;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
public class FundsConfirmationController12 implements FundsConfirmationApi {

    private final AccountReferenceValidationService referenceValidationService;
    private final ResponseMapper responseMapper;
    private final FundsConfirmationService fundsConfirmationService;
    private final FundsConfirmationMapper fundsConfirmationMapper;

    @Override
    public ResponseEntity<?> checkAvailabilityOfFunds(ConfirmationOfFunds body, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate) {

        FundsConfirmationRequest fcr = fundsConfirmationMapper.mapToFundsConfirmationRequest(body);
        ResponseObject accountReferenceValidationResponse = referenceValidationService.validateAccountReferences(fcr.getAccountReferences());

        if (!accountReferenceValidationResponse.hasError()) {
            return new ResponseEntity<>(fundsConfirmationService.fundsConfirmation(fcr), OK);
        } else {
            return responseMapper.createErrorResponse(accountReferenceValidationResponse.getError());
        }

    }
}
