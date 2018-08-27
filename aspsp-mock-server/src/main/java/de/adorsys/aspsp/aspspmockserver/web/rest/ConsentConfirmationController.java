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
import de.adorsys.aspsp.aspspmockserver.service.TanConfirmationService;
import de.adorsys.aspsp.aspspmockserver.service.ConsentService;
import de.adorsys.aspsp.aspspmockserver.web.util.ApiError;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiConsentStatus;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static de.adorsys.aspsp.xs2a.spi.domain.consent.SpiConsentStatus.REJECTED;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/consent/confirmation")
@Api(tags = "Consent confirmation for online banking", description = "Provides access to consent confirmation for online banking")
public class ConsentConfirmationController {

    private final TanConfirmationService tanConfirmationService;
    private final ConsentService consentService;

    @Value("${onlinebanking-mock-webapp.baseurl}")
    private String onlineBankingMockWebappUrl;

    @GetMapping(path = "/{consent-id}")
    @ApiOperation(value = "Redirects to online banking consent confirmation page", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    public void showConfirmationPage(@PathVariable("consent-id") String consentId,
                                     HttpServletResponse response) throws IOException {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                                          .path("/{consentId}").buildAndExpand(consentId);

        response.sendRedirect(onlineBankingMockWebappUrl + uriComponents.toUriString());
    }

    @PutMapping(path = "/status/{consent-id}/{status}")
    @ApiOperation(value = "Updates ais consent status", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    public ResponseEntity<Void> updateAisConsentStatus(@PathVariable("consent-id") String consentId,
                                                       @PathVariable("status") SpiConsentStatus status) throws IOException {
        consentService.updateAisConsentStatus(consentId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/tan/{iban}")
    @ApiOperation(value = "Generates TAN for consent confirmation", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity generateAndSendTan(@PathVariable("iban") String iban) {
        return tanConfirmationService.generateAndSendTanForPsuByIban(iban)
                   ? ResponseEntity.ok().build()
                   : ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "/tan/validate")
    @ApiOperation(value = "Validates TAN for consent confirmation", authorizations = {@Authorization(value = "oauth2", scopes = {@AuthorizationScope(scope = "read", description = "Access read API")})})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity confirmTan(@RequestBody Confirmation confirmation) {
        if (tanConfirmationService.isTanNumberValidByIban(confirmation.getIban(), confirmation.getTanNumber())) {
            return ResponseEntity.ok().build();
        } else if (tanConfirmationService.getTanNumberOfAttemptsByIban(confirmation.getIban()) < 3) {
            ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "WRONG_TAN", "Bad request");
            return new ResponseEntity<>(error, error.getStatus());
        }
        consentService.updateAisConsentStatus(confirmation.getConsentId(), REJECTED);
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, "LIMIT_EXCEEDED", "Bad request");
        return new ResponseEntity<>(error, error.getStatus());
    }
}
