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

import de.adorsys.aspsp.xs2a.config.rest.consent.AspspConsentDataRemoteUrls;
import de.adorsys.aspsp.xs2a.service.consent.ConsentDataService;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//TODO Implement this class properly during implementation of FundsConfirmationConsent https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/379
@Service
public class FundsConfirmationConsentDataService extends ConsentDataService {
    public FundsConfirmationConsentDataService(RestTemplate consentRestTemplate) {
        super(consentRestTemplate);
    }

    @Override
    protected AspspConsentDataRemoteUrls getRemoteUrl() {
        //TODO Create FundsConfirmationConsentRemoteUrls after corresponding endpoins appear https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/379
        return null;
    }

    @Override
    public AspspConsentData getAspspConsentDataByConsentId(String consentId) {
        return null;
    }
}
