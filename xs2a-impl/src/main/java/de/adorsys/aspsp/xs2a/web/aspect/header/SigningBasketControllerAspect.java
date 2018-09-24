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
import de.adorsys.psd2.model.SigningBasketResponse201;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class SigningBasketControllerAspect extends HeaderController {

    public SigningBasketControllerAspect(AspspProfileServiceWrapper aspspProfileServiceWrapper) {
        super(aspspProfileServiceWrapper);
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.SigningBasketController.createSigningBasket(..)) && args(xRequestID, ..)", returning = "result", argNames = "result,xRequestID")
    public ResponseEntity<?> createSigningBasketAspect(ResponseEntity<?> result, UUID xRequestID) {
        addXRequestIdHeader(xRequestID.toString());
        addAspspScaApproachHeader();
        return new ResponseEntity<>(
            result.getBody(),
            addLocationHeader(result),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.SigningBasketController.getSigningBasket(..)) && args(basketId, xRequestID, ..)", returning = "result", argNames = "result,basketId,xRequestID")
    public ResponseEntity<?> getSigningBasketAspect(ResponseEntity<?> result, String basketId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    private HttpHeaders addLocationHeader(ResponseEntity<?> result) {
        if (result.getStatusCode() == HttpStatus.OK) {
            SigningBasketResponse201 response = (SigningBasketResponse201) result.getBody();
            return addHeader("location", response.getLinks().getSelf());
        }
        return null;
    }
}
