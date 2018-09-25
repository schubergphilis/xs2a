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

import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.service.profile.AspspProfileServiceWrapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class PaymentResponseHeadersAspect extends HeaderController {

    public PaymentResponseHeadersAspect(AspspProfileServiceWrapper aspspProfileServiceWrapper) {
        super(aspspProfileServiceWrapper);
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.initiatePayment(..)) && args(body, paymentService, paymentProduct, xRequestID, ..)", returning = "result", argNames = "result,body,paymentService,paymentProduct,xRequestID")
    public ResponseEntity<?> paymentInitiationAspect(ResponseEntity<?> result, Object body, String paymentService, String paymentProduct, UUID xRequestID) {
        addXRequestIdHeader(xRequestID.toString());
        addLocationHeader(result);
        return new ResponseEntity<>(
            result.getBody(),
            addAspspScaApproachHeader(),
        result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.getPaymentInitiationStatus(..)) && args(paymentService, paymentId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,xRequestID")
    public ResponseEntity<?> getPaymentInitiationStatusAspect(ResponseEntity<?> result, String paymentService, String paymentId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.getPaymentInformation(..)) && args(paymentService, paymentId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,xRequestID")
    public ResponseEntity<?> getPaymentInformationAspect(ResponseEntity<?> result, String paymentService, String paymentId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.cancelPayment(..)) && args(paymentService, paymentId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,xRequestID")
    public ResponseEntity<?> cancelPaymentAspect(ResponseEntity<?> result, String paymentService, String paymentId, UUID xRequestID) {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.getPaymentInitiationCancellationAuthorisationInformation(..)) && args(paymentService, paymentId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,xRequestID")
    public ResponseEntity<?> getPaymentInitiationCancellationAuthorisationInformationAspect(ResponseEntity<?> result, String paymentService, String paymentId, UUID xRequestID)  {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.startPaymentAuthorisation(..)) && args(paymentService, paymentId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,xRequestID")
    public ResponseEntity<?> startPaymentAuthorisationAspect(ResponseEntity<?> result, String paymentService, String paymentId, UUID xRequestID)  {
        return new ResponseEntity<>(
            result.getBody(),
            addStartAuthorizarionHeaders(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.startPaymentInitiationCancellationAuthorisation(..)) && args(paymentService, paymentId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,xRequestID")
    public ResponseEntity<?> startPaymentInitiationCancellationAuthorisationAspect(ResponseEntity<?> result, String paymentService, String paymentId, UUID xRequestID)  {
        return new ResponseEntity<>(
            result.getBody(),
            addStartAuthorizarionHeaders(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.updatePaymentPsuData(..)) && args(paymentService, paymentId, authorisationId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,authorisationId,xRequestID")
    public ResponseEntity<?> updatePaymentPsuDataAspect(ResponseEntity<?> result, String paymentService, String paymentId, String authorisationId, UUID xRequestID)  {
        return new ResponseEntity<>(
            result.getBody(),
            addStartAuthorizarionHeaders(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.updatePaymentCancellationPsuData(..)) && args(paymentService, paymentId, cancellationId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,cancellationId,xRequestID")
    public ResponseEntity<?> updatePaymentCancellationPsuDataAspect(ResponseEntity<?> result, String paymentService, String paymentId, String cancellationId, UUID xRequestID)  {
        return new ResponseEntity<>(
            result.getBody(),
            addStartAuthorizarionHeaders(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web.PaymentController.getPaymentInitiationAuthorisation(..)) && args(paymentService, paymentId, xRequestID, ..)", returning = "result", argNames = "result,paymentService,paymentId,xRequestID")
    public ResponseEntity<?> getPaymentInitiationAuthorisationAspect(ResponseEntity<?> result, String paymentService, String paymentId, UUID xRequestID)  {
        return new ResponseEntity<>(
            result.getBody(),
            addXRequestIdHeader(xRequestID.toString()),
            result.getStatusCode()
        );
    }

    private HttpHeaders addLocationHeader(ResponseEntity<?> result) {
        if (result.getStatusCode() == HttpStatus.OK) {
            PaymentInitialisationResponse response = (PaymentInitialisationResponse) result.getBody();
            return addHeader("location", response.getLinks().getSelf());
        }
        return null;
    }
}
