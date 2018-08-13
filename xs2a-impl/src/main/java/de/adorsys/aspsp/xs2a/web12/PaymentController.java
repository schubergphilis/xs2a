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

package de.adorsys.aspsp.xs2a.web12;

import de.adorsys.aspsp.xs2a.service.PaymentService;
import de.adorsys.aspsp.xs2a.service.mapper.ResponseMapper;
import de.adorsys.aspsp.xs2a.service.validator.AccountReferenceValidationService;
import de.adorsys.psd2.api.V1Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
public class PaymentController implements V1Api {

    private final ResponseMapper responseMapper;
    private final PaymentService paymentService;
    private final AccountReferenceValidationService referenceValidationService;

    @Override
    public ResponseEntity<?> initiatePayment(Object body, String paymentService, String paymentProduct, UUID xRequestID, String psUIPAddress, String digest, String signature, byte[] tpPSignatureCertificate, String PSU_ID, String psUIDType, String psUCorporateID, String psUCorporateIDType, String consentID, Boolean tpPRedirectPreferred, String tpPRedirectURI, String tpPNokRedirectURI, Boolean tpPExplicitAuthorisationPreferred, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> cancelPayment(String paymentService, String paymentId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> getPaymentCancellationScaStatus(String paymentService, String paymentId, String cancellationId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> getPaymentInformation(String paymentService, String paymentId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> getPaymentInitiationAuthorisation(String paymentService, String paymentId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> getPaymentInitiationCancellationAuthorisationInformation(String paymentService, String paymentId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> getPaymentInitiationScaStatus(String paymentService, String paymentId, String authorisationId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> getPaymentInitiationStatus(String paymentService, String paymentId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> startPaymentAuthorisation(String paymentService, String paymentId, UUID xRequestID, String PSU_ID, String psUIDType, String psUCorporateID, String psUCorporateIDType, String digest, String signature, byte[] tpPSignatureCertificate, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> startPaymentInitiationCancellationAuthorisation(String paymentService, String paymentId, UUID xRequestID, String digest, String signature, byte[] tpPSignatureCertificate, String PSU_ID, String psUIDType, String psUCorporateID, String psUCorporateIDType, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> updatePaymentCancellationPsuData(String paymentService, String paymentId, String cancellationId, UUID xRequestID, Object body, String digest, String signature, byte[] tpPSignatureCertificate, String PSU_ID, String psUIDType, String psUCorporateID, String psUCorporateIDType, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }

    @Override
    public ResponseEntity<?> updatePaymentPsuData(String paymentService, String paymentId, String authorisationId, UUID xRequestID, Object body, String digest, String signature, byte[] tpPSignatureCertificate, String PSU_ID, String psUIDType, String psUCorporateID, String psUCorporateIDType, String psUIPAddress, Object psUIPPort, String psUAccept, String psUAcceptCharset, String psUAcceptEncoding, String psUAcceptLanguage, String psUUserAgent, String psUHttpMethod, UUID psUDeviceID, String psUGeoLocation) {
        return null;
    }
}
