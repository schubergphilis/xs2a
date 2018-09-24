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

package de.adorsys.aspsp.xs2a.web.aspect;

import de.adorsys.aspsp.xs2a.component.JsonConverter;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.UpdatePisConsentPsuDataRequest;
import de.adorsys.aspsp.xs2a.domain.Links;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.consent.Xs2aChosenScaMethod;
import de.adorsys.aspsp.xs2a.domain.consent.Xs2aUpdatePisConsentPsuDataResponse;
import de.adorsys.aspsp.xs2a.service.message.MessageService;
import de.adorsys.aspsp.xs2a.service.profile.AspspProfileServiceWrapper;
import de.adorsys.aspsp.xs2a.web12.PaymentController12;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class UpdatePisAuthorizationAspect extends AbstractLinkAspect<PaymentController12> {
    public UpdatePisAuthorizationAspect(int maxNumberOfCharInTransactionJson, AspspProfileServiceWrapper aspspProfileService, JsonConverter jsonConverter, MessageService messageService) {
        super(maxNumberOfCharInTransactionJson, aspspProfileService, jsonConverter, messageService);
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.service.ConsentService.updatePisConsentPsuData(..)) && args(request)", returning = "result", argNames = "request, result")
    public ResponseObject<Xs2aUpdatePisConsentPsuDataResponse> updatePisConsentAuthorizationAspect(UpdatePisConsentPsuDataRequest request, ResponseObject<Xs2aUpdatePisConsentPsuDataResponse> result) {
        if (!result.hasError()) {
            if (StringUtils.isNotBlank(request.getAuthenticationMethodId())) {
                Xs2aUpdatePisConsentPsuDataResponse body = result.getBody();
                body.setLinks(buildLink(body.getLinks(), request.getPaymentService(), request.getPaymentId(), request.getAuthorizationId()));
                body.setChosenScaMethod(getChosenScaMethod(request.getAuthenticationMethodId()));
            }
            return result;
        }
        return enrichErrorTextMessage(result);
    }

    private Links buildLink(Links links, String paymentService, String paymentId, String authorizationId) {
        links.setAuthoriseTransaction(buildPath("/v1/{paymentService}/{paymentId}/authorisations/{authorizationId}", paymentService, paymentId, authorizationId));
        return links;
    }

    private Xs2aChosenScaMethod getChosenScaMethod(String authenticationMethodId) {
        Xs2aChosenScaMethod method = new Xs2aChosenScaMethod();
        method.setAuthenticationMethodId(authenticationMethodId);
        method.setAuthenticationType(authenticationMethodId);
        return method;
    }
}
