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
import de.adorsys.aspsp.xs2a.domain.Links;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.consent.*;
import de.adorsys.aspsp.xs2a.service.authorization.AuthorisationMethodService;
import de.adorsys.aspsp.xs2a.service.message.MessageService;
import de.adorsys.aspsp.xs2a.service.profile.AspspProfileServiceWrapper;
import de.adorsys.aspsp.xs2a.web.ConsentController;
import de.adorsys.psd2.aspsp.profile.domain.ScaApproach;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public class ConsentAspect extends AbstractLinkAspect<ConsentController> {
    private AuthorisationMethodService authorisationMethodService;

    public ConsentAspect(int maxNumberOfCharInTransactionJson, AspspProfileServiceWrapper aspspProfileService, JsonConverter jsonConverter, MessageService messageService, AuthorisationMethodService authorisationMethodService) {
        super(maxNumberOfCharInTransactionJson, aspspProfileService, jsonConverter, messageService);
        this.authorisationMethodService = authorisationMethodService;
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.service.ConsentService.createAccountConsentsWithResponse(..)) && args(request, psuId, explicitPreferred)", returning = "result", argNames = "result,request,psuId,explicitPreferred")
    public ResponseObject<CreateConsentResponse> invokeCreateAccountConsentAspect(ResponseObject<CreateConsentResponse> result, CreateConsentReq request, String psuId, boolean explicitPreferred) {
        if (!result.hasError()) {

            CreateConsentResponse body = result.getBody();
            body.setLinks(buildLinksForConsentResponse(body, explicitPreferred));
            return result;
        }
        return enrichErrorTextMessage(result);
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.service.ConsentService.updateConsentPsuData(..)) && args(updatePsuData)", returning = "result", argNames = "result,updatePsuData")
    public ResponseObject<UpdateConsentPsuDataResponse> invokeUpdateConsentPsuDataAspect(ResponseObject<UpdateConsentPsuDataResponse> result, UpdateConsentPsuDataReq updatePsuData) {
        if (!result.hasError()) {
            UpdateConsentPsuDataResponse body = result.getBody();
            body.setLinks(buildLinksForUpdateConsentResponse(body, updatePsuData));
            return result;
        }
        return enrichErrorTextMessage(result);
    }

    private Links buildLinksForConsentResponse(CreateConsentResponse response, boolean explicitPreferred) {
        Links links = new Links();

        if (ScaApproach.EMBEDDED == aspspProfileService.getScaApproach()) {
            buildLinkForEmbeddedScaApproach(response, links, explicitPreferred);
        } else {
            links.setScaRedirect(aspspProfileService.getAisRedirectUrlToAspsp() + response.getConsentId());
        }

        return links;
    }

    private void buildLinkForEmbeddedScaApproach(CreateConsentResponse response, Links links, boolean explicitPreferred) {
        if (authorisationMethodService.isExplicitMethod(explicitPreferred)) {
            links.setStartAuthorisation(buildPath("/v1/consents/{consentId}/authorisations", response.getConsentId()));
        } else {
            links.setStartAuthorisationWithPsuAuthentication(buildPath("/v1/consents/{consentId}/authorisations/{authorisation-id}", response.getConsentId(), response.getAuthorizationId()));
        }
    }

    private Links buildLinksForUpdateConsentResponse(UpdateConsentPsuDataResponse response, UpdateConsentPsuDataReq request) {
        return Optional.ofNullable(response.getScaStatus())
                   .map(status -> {
                       Links links = null;

                       if (status == Xs2aScaStatus.PSUAUTHENTICATED) {
                           links = buildLinksForPsuAuthenticatedConsentResponse(request);
                       } else if (status == Xs2aScaStatus.SCAMETHODSELECTED) {
                           links = buildLinksForScaMethodSelectedConsentResponse(request);
                       } else if (status == Xs2aScaStatus.FINALISED) {
                           links = buildLinksForFinalisedConsentResponse(request);
                       }

                       return links;
                   })
                   .orElse(null);
    }

    private Links buildLinksForPsuAuthenticatedConsentResponse(UpdateConsentPsuDataReq request) {
        Links links = new Links();
        links.setSelectAuthenticationMethod(buildPath("/v1/consents/{consentId}/authorisations/{authorisation-id}", request.getConsentId(), request.getAuthorizationId()));

        return links;
    }

    private Links buildLinksForScaMethodSelectedConsentResponse(UpdateConsentPsuDataReq request) {
        Links links = new Links();
        links.setAuthoriseTransaction(buildPath("/v1/consents/{consentId}/authorisations/{authorisation-id}", request.getConsentId(), request.getAuthorizationId()));

        return links;
    }

    private Links buildLinksForFinalisedConsentResponse(UpdateConsentPsuDataReq request) {
        Links links = new Links();
        links.setScaStatus(buildPath("/v1/consents/{consentId}/authorisations/{authorisation-id}", request.getConsentId(), request.getAuthorizationId()));

        return links;
    }
}
