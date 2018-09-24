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
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class AccountControllerAspect extends HeaderController {

    public AccountControllerAspect(AspspProfileServiceWrapper aspspProfileServiceWrapper) {
        super(aspspProfileServiceWrapper);
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController12.getAccountList(..)) && args(xRequestID, ..)", returning = "result", argNames = "result,xRequestID")
    public ResponseEntity<?> getAccountListAspect(ResponseEntity<?> result, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController12.readAccountDetails(..)) && args(accountId, xRequestID, ..)", returning = "result", argNames = "result,accountId,xRequestID")
    public ResponseEntity<?> readAccountDetailsAspect(ResponseEntity<?> result, String accountId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController12.getBalances(..)) && args(accountId, xRequestID,consentID, ..)", returning = "result", argNames = "result,accountId,xRequestID,consentID")
    public ResponseEntity<?> getBalancesAspect(ResponseEntity<?> result, String accountId, UUID xRequestID, String consentID) {
        addHeadersForGetBalancesAspect(xRequestID, consentID);
        return new ResponseEntity<>(
            result.getBody(),
            getHeaders(),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController12.getTransactionList(..)) && args(accountId, bookingStatus, xRequestID, ..)", returning = "result", argNames = "result,accountId,bookingStatus,xRequestID")
    public ResponseEntity<?> getTransactionListAspect(ResponseEntity<?> result, String accountId, String bookingStatus, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController12.getTransactionDetails(..)) && args(accountId, resourceId, xRequestID, ..)", returning = "result", argNames = "result,accountId,resourceId,xRequestID")
    public ResponseEntity<?> getTransactionDetailsAspect(ResponseEntity<?> result, String accountId, String resourceId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    private void addHeadersForGetBalancesAspect(UUID xRequestID, String consentId) {
        addXRequestIdHeader(xRequestID.toString());
        addHeader("consent-id", consentId);
    }
}
