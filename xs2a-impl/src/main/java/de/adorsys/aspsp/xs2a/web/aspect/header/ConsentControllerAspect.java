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

package de.adorsys.aspsp.xs2a.web.aspect.header;

import de.adorsys.aspsp.xs2a.service.profile.AspspProfileServiceWrapper;
import de.adorsys.psd2.model.ConsentsResponse201;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class ConsentControllerAspect extends HeaderController {

    public ConsentControllerAspect(AspspProfileServiceWrapper aspspProfileServiceWrapper) {
        super(aspspProfileServiceWrapper);
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.ConsentController.createConsent(..)) && args(xRequestID, ..)", returning = "result", argNames = "result,xRequestID")
    public ResponseEntity<?> paymentInitiationAspect(ResponseEntity<?> result, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addHeadersForCreateConsent(xRequestID, result),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.ConsentController.getConsentStatus(..)) && args(consentId, xRequestID, ..)", returning = "result", argNames = "result,consentId,xRequestID")
    public ResponseEntity<?> getConsentStatusAspect(ResponseEntity<?> result, String consentId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.ConsentController.getConsentInformation(..)) && args(consentId, xRequestID, ..)", returning = "result", argNames = "result,consentId,xRequestID")
    public ResponseEntity<?> getConsentInformationAspect(ResponseEntity<?> result, String consentId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.ConsentController.deleteConsent(..)) && args(consentId, xRequestID, ..)", returning = "result", argNames = "result,consentId,xRequestID")
    public ResponseEntity<?> deleteConsentAspect(ResponseEntity<?> result, String consentId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.ConsentController.startConsentAuthorisation(..)) && args(consentId, xRequestID, ..)", returning = "result", argNames = "result,consentId,xRequestID")
    public ResponseEntity<?> startConsentAuthorisationAspect(ResponseEntity<?> result, String consentId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addStartAuthorizarionHeaders(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.ConsentController.updateConsentsPsuData( ..)) && args(consentId,authorisationId, xRequestID, ..)", returning = "result", argNames = "result,consentId,authorisationId,xRequestID")
    public ResponseEntity<?> startConsentAuthorisationAspect(ResponseEntity<?> result, String consentId, String authorisationId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addStartAuthorizarionHeaders(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    private HttpHeaders addHeadersForCreateConsent(UUID xRequestID, ResponseEntity<?> result) {
        if (result.getStatusCode() == HttpStatus.OK) {
            ConsentsResponse201 response = (ConsentsResponse201) result.getBody();
            return addHeader("location", response.getLinks().get("self").toString());
        }
        addXRequestIdHeader(xRequestID.toString());
        return addAspspScaApproachHeader();

    }
}
