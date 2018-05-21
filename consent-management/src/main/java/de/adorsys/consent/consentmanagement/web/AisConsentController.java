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

package de.adorsys.consent.consentmanagement.web;

import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiCreateConsentRequest;
import de.adorsys.consent.consentmanagement.service.AisConsentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "consent")
public class AisConsentController {
    private final AisConsentService aisConsentService;

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@RequestBody SpiCreateConsentRequest request,
                                             @RequestParam(required = false) String psuId,
                                             @RequestParam(required = false) String tppId,
                                             @RequestParam(required = false) boolean withBalance,
                                             @RequestParam(required = false) boolean tppRedirectPreferred) {
        return aisConsentService.createConsent(request, psuId, tppId, withBalance)
            .map(consentId -> new ResponseEntity<>(consentId, HttpStatus.CREATED))
            .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
