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

package de.adorsys.aspsp.xs2a.spi.impl.service;

import de.adorsys.aspsp.xs2a.consent.api.pis.PisPayment;
import de.adorsys.aspsp.xs2a.consent.api.pis.PisPaymentType;
import de.adorsys.aspsp.xs2a.service.keycloak.KeycloakInvokerService;
import de.adorsys.aspsp.xs2a.spi.config.AspspRemoteUrls;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.domain.payment.SpiPaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.spi.domain.psu.SpiScaMethod;
import de.adorsys.aspsp.xs2a.spi.impl.PaymentSpiImpl;
import de.adorsys.aspsp.xs2a.spi.impl.mapper.SpiPisConsentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static de.adorsys.aspsp.xs2a.spi.domain.constant.AuthorizationConstant.AUTHORIZATION_HEADER;

@Service
@RequiredArgsConstructor
public class AspspService {
    @Qualifier("aspspRestTemplate")
    private final RestTemplate aspspRestTemplate;
    private final KeycloakInvokerService keycloakInvokerService;
    private final AspspRemoteUrls aspspRemoteUrls;
    private final PaymentSpiImpl paymentSpi;
    private final SpiPisConsentMapper pisConsentMapper;

    public List<SpiScaMethod> readAvailableScaMethod(String psuId, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, keycloakInvokerService.obtainAccessToken(psuId, password));
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<List<SpiScaMethod>> response = aspspRestTemplate.exchange(
            aspspRemoteUrls.getScaMethods(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<SpiScaMethod>>() {
            });

        return Optional.ofNullable(response.getBody())
                   .orElse(Collections.emptyList());
    }

    public String createPayment(PisPaymentType paymentType, List<PisPayment> payments) {
        String executionPaymentId = null;
        if (PisPaymentType.SINGLE == paymentType) {
            SpiPaymentInitialisationResponse paymentInitiation = paymentSpi.createPaymentInitiation(pisConsentMapper.mapToSpiSinglePayment(payments), new AspspConsentData())
                                                                     .getPayload();
            executionPaymentId = paymentInitiation.getPaymentId();
        }
        return executionPaymentId;
    }

    public void generateConfirmationCode() {
        aspspRestTemplate.exchange(aspspRemoteUrls.getGenerateTanConfirmation(), HttpMethod.POST, null, Void.class);
    }
}
