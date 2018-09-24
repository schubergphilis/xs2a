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
import de.adorsys.psd2.model.ConfirmationOfFunds;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class FundsConfirmationtControllerAspect extends HeaderController {

    public FundsConfirmationtControllerAspect(AspspProfileServiceWrapper aspspProfileServiceWrapper) {
        super(aspspProfileServiceWrapper);
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.FundsConfirmationController12.checkAvailabilityOfFunds(..)) && args(body,xRequestID, ..)", returning = "result", argNames = "result,body,xRequestID")
    public ResponseEntity<?> createSigningBasketAspect(ResponseEntity<?> result, ConfirmationOfFunds body, UUID xRequestID) {
        addXRequestIdHeader(xRequestID.toString());
        return new ResponseEntity<>(
            result.getBody(),
            result.getStatusCode()
        );
    }
}
