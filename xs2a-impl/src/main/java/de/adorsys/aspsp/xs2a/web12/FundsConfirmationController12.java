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
import de.adorsys.aspsp.xs2a.domain.TppMessageInformation;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationRequest;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationResponse;
import de.adorsys.aspsp.xs2a.exception.MessageError;
import de.adorsys.aspsp.xs2a.service.AccountReferenceValidationService;
import de.adorsys.aspsp.xs2a.service.FundsConfirmationService;
import de.adorsys.aspsp.xs2a.service.mapper.FundsConfirmationMapper;
import de.adorsys.aspsp.xs2a.service.mapper.ResponseMapper;
import de.adorsys.psd2.api.FundsConfirmationApi;
import de.adorsys.psd2.model.ConfirmationOfFunds;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

import static de.adorsys.aspsp.xs2a.domain.MessageErrorCode.FORMAT_ERROR;
import static de.adorsys.aspsp.xs2a.exception.MessageCategory.ERROR;

@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "PIISP, Funds confirmation 1.2", description = "Provides access to the funds confirmation")
public class FundsConfirmationController12 implements FundsConfirmationApi {

    private final AccountReferenceValidationService referenceValidationService;
    private final ResponseMapper responseMapper;
    private final FundsConfirmationService fundsConfirmationService;
    private final FundsConfirmationMapper fundsConfirmationMapper;

    @Override
    public ResponseEntity<?> checkAvailabilityOfFunds(ConfirmationOfFunds body, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate) {

        FundsConfirmationRequest fundsConfirmationRequest = fundsConfirmationMapper.mapToFundsConfirmationRequest(body);

        return Optional.ofNullable(fundsConfirmationRequest)
                   .map(fcr -> {
                       ResponseObject accountReferenceValidationResponse = referenceValidationService.validateAccountReferences(fcr.getAccountReferences());
                       ResponseObject<FundsConfirmationResponse> fundsConfirmationResponse = accountReferenceValidationResponse.hasError()
                                                                                                 ? ResponseObject.<FundsConfirmationResponse>builder().fail(accountReferenceValidationResponse.getError()).build()
                                                                                                 : fundsConfirmationService.fundsConfirmation(fcr);
                       return responseMapper.ok(fundsConfirmationResponse);
                   })
                   .orElse(
                       responseMapper.ok(ResponseObject.<FundsConfirmationResponse>builder().fail(new MessageError(new TppMessageInformation(ERROR, FORMAT_ERROR))).build())
                   );
    }
}
