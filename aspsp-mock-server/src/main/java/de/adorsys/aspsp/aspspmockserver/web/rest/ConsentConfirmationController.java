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

package de.adorsys.aspsp.aspspmockserver.web.rest;

import de.adorsys.aspsp.aspspmockserver.domain.Confirmation;
import de.adorsys.aspsp.aspspmockserver.service.ConfirmationService;
import de.adorsys.aspsp.aspspmockserver.service.ConsentConfirmationService;
import de.adorsys.aspsp.aspspmockserver.web.util.ApiError;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountDetails;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiConsentStatus;
import de.adorsys.aspsp.xs2a.spi.domain.psu.ConfirmationType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/consent/confirmation")
@Api(tags = "Consent confirmation for online banking", description = "Provides access to consent confirmation for online banking")
public class ConsentConfirmationController {

    private final ConfirmationService confirmationService;
    private final ConsentConfirmationService consentConfirmationService;

    @Value("${onlinebanking-mock-webapp.baseurl}")
    private String onlineBankingMockWebappUrl;

    @GetMapping(path = "/{consent-id}")
    @ApiOperation(value = "Redirects to online banking consent confirmation page")
    public void showConfirmationPage(@PathVariable("consent-id") String consentId,
                                     HttpServletResponse response) throws IOException {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                                          .path("/{consentId}").buildAndExpand(consentId);

        response.sendRedirect(onlineBankingMockWebappUrl + uriComponents.toUriString());
    }

    @GetMapping(path = "/accounts/{consent-id}")
    @ApiOperation(value = "Gets available accounts by consent id")
    public ResponseEntity<List<SpiAccountDetails>> getAccountDetailsListByConsentId(@PathVariable("consent-id") String consentId) throws IOException {
        List<SpiAccountDetails> response = consentConfirmationService.getAccountDetailsListByConsentId(consentId);

        return CollectionUtils.isEmpty(response)
                   ? ResponseEntity.ok(response)
                   : ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/status/{consent-id}/{status}")
    @ApiOperation(value = "Updates ais consent status")
    public ResponseEntity<Void> updateAisConsentStatus(@PathVariable("consent-id") String consentId,
                                                       @PathVariable("status") SpiConsentStatus status) throws IOException {
        consentConfirmationService.updateConsentStatus(consentId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/tan")
    @ApiOperation(value = "Generates TAN for consent confirmation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity generateAndSendTan(@RequestBody Confirmation confirmation) {
        return confirmationService.generateAndSendTanForPsuByIban(confirmation.getIban())
                   ? ResponseEntity.ok().build()
                   : ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "/{iban}")
    @ApiOperation(value = "Validates TAN for consent confirmation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity confirmTan(@RequestBody Confirmation confirmation) {
        return confirmationService.isTanNumberValidByIban(confirmation.getIban(), confirmation.getTanNumber(), confirmation.getConsentId(), ConfirmationType.CONSENT)
                   ? ResponseEntity.ok().build()
                   : ResponseEntity.badRequest().build();
    }
}
