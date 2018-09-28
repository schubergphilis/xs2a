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

package de.adorsys.aspsp.xs2a.web;

import de.adorsys.aspsp.xs2a.consent.api.CmsScaStatus;
import de.adorsys.aspsp.xs2a.consent.api.PisConsentStatusResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.CreatePisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.UpdatePisConsentPsuDataRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.UpdatePisConsentPsuDataResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.CreatePisConsentResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.PisConsentRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.PisConsentResponse;
import de.adorsys.aspsp.xs2a.service.PisConsentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus.RECEIVED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PisConsentControllerTest {

    private final String AUTHORISATION_ID = "345-9245-2359";
    private final String PAYMENT_ID = "33333-999999999";
    private final String CONSENT_ID = "12345";
    private final String STATUS_RECEIVED = "RECEIVED";

    private final String WRONG_AUTHORISATION_ID = "3254890-5";
    private final String WRONG_PAYMENT_ID = "32343-999997777";
    private final String WRONG_CONSENT_ID = "67890";

    @InjectMocks
    private PisConsentController pisConsentController;

    @Mock
    private PisConsentService pisConsentService;

    @Before
    public void setUp() {
        when(pisConsentService.createPaymentConsent(getPisConsentRequest())).thenReturn(Optional.of(getCreatePisConsentResponse()));
        when(pisConsentService.getConsentStatusById(CONSENT_ID)).thenReturn(Optional.of(RECEIVED));
        when(pisConsentService.getConsentById(CONSENT_ID)).thenReturn(Optional.of(getPisConsentResponse()));
        when(pisConsentService.updateConsentStatusById(CONSENT_ID, RECEIVED)).thenReturn(Optional.of(Boolean.TRUE));
        when(pisConsentService.createAuthorization(PAYMENT_ID)).thenReturn(Optional.of(getCreatePisConsentAuthorisationResponse()));
        when(pisConsentService.updateConsentAuthorization(AUTHORISATION_ID, getUpdatePisConsentPsuDataRequest())).thenReturn(Optional.of(getUpdatePisConsentPsuDataResponse()));
    }

    @Test
    public void createPaymentConsent_Success() {

        ResponseEntity<CreatePisConsentResponse> actual = pisConsentController.createPaymentConsent(getPisConsentRequest());
        ResponseEntity<CreatePisConsentResponse> expected = new ResponseEntity<>(new CreatePisConsentResponse(CONSENT_ID), HttpStatus.CREATED);

        assertEquals(actual, expected);
    }

    @Test
    public void createPaymentConsent_Failure() {
        //Given
        when(pisConsentService.createPaymentConsent(getPisConsentRequest())).thenReturn(Optional.empty());

        //Then
        ResponseEntity<CreatePisConsentResponse> actual = pisConsentController.createPaymentConsent(getPisConsentRequest());
        ResponseEntity<CreatePisConsentResponse> expected = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        assertEquals(actual, expected);
    }

    @Test
    public void getConsentStatusById_Success() {

        ResponseEntity<PisConsentStatusResponse> actual = pisConsentController.getConsentStatusById(CONSENT_ID);
        ResponseEntity<PisConsentStatusResponse> expected = new ResponseEntity<>(new PisConsentStatusResponse(RECEIVED), HttpStatus.OK);

        assertEquals(actual, expected);
    }

    @Test
    public void getConsentStatusById_Failure() {
        //Given
        when(pisConsentService.getConsentStatusById(WRONG_CONSENT_ID)).thenReturn(Optional.empty());

        //Then
        ResponseEntity<PisConsentStatusResponse> actual = pisConsentController.getConsentStatusById(WRONG_CONSENT_ID);
        ResponseEntity<PisConsentStatusResponse> expected = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        assertEquals(actual, expected);
    }

    @Test
    public void getConsentById_Success() {

        ResponseEntity<PisConsentResponse> actual = pisConsentController.getConsentById(CONSENT_ID);
        ResponseEntity<PisConsentResponse> expected = new ResponseEntity<>(new PisConsentResponse(), HttpStatus.OK);

        assertEquals(actual, expected);
    }

    @Test
    public void getConsentById_Failure() {
        //Given
        when(pisConsentService.getConsentById(WRONG_CONSENT_ID)).thenReturn(Optional.empty());

        //Then
        ResponseEntity<PisConsentResponse> actual = pisConsentController.getConsentById(WRONG_CONSENT_ID);
        ResponseEntity<PisConsentResponse> expected = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        assertEquals(actual, expected);
    }

    @Test
    public void updateConsentStatus_Success() {

        ResponseEntity<Void> actual = pisConsentController.updateConsentStatus(CONSENT_ID, STATUS_RECEIVED);
        ResponseEntity<Void> expected = new ResponseEntity<>(HttpStatus.OK);

        assertEquals(actual, expected);
    }

    @Test
    public void updateConsentStatus_Failure() {
        //Given
        when(pisConsentService.updateConsentStatusById(WRONG_CONSENT_ID, RECEIVED)).thenReturn(Optional.empty());

        //Then
        ResponseEntity<Void> actual = pisConsentController.updateConsentStatus(WRONG_CONSENT_ID, STATUS_RECEIVED);
        ResponseEntity<Void> expected = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        assertEquals(actual, expected);
    }

    @Test
    public void createConsentAuthorization_Success() {

        ResponseEntity<CreatePisConsentAuthorisationResponse> actual = pisConsentController.createConsentAuthorization(PAYMENT_ID);
        ResponseEntity<CreatePisConsentAuthorisationResponse> expected = new ResponseEntity<>(new CreatePisConsentAuthorisationResponse(AUTHORISATION_ID), HttpStatus.CREATED);

        assertEquals(actual, expected);
    }

    @Test
    public void createConsentAuthorization_Failure() {
        //Given
        when(pisConsentService.createAuthorization(WRONG_PAYMENT_ID)).thenReturn(Optional.empty());

        //Then
        ResponseEntity<CreatePisConsentAuthorisationResponse> actual = pisConsentController.createConsentAuthorization(WRONG_PAYMENT_ID);
        ResponseEntity<CreatePisConsentAuthorisationResponse> expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        assertEquals(actual, expected);
    }

    @Test
    public void updateConsentAuthorization_Success() {

        ResponseEntity<UpdatePisConsentPsuDataResponse> actual = pisConsentController.updateConsentAuthorization(AUTHORISATION_ID, getUpdatePisConsentPsuDataRequest());
        ResponseEntity<UpdatePisConsentPsuDataResponse> expected = new ResponseEntity<>(new UpdatePisConsentPsuDataResponse(CmsScaStatus.RECEIVED), HttpStatus.OK);

        assertEquals(actual, expected);
    }

    @Test
    public void updateConsentAuthorization_Failure() {
        //Given
        when(pisConsentService.updateConsentAuthorization(WRONG_AUTHORISATION_ID, getUpdatePisConsentPsuDataRequest())).thenReturn(Optional.empty());

        //Then
        ResponseEntity<UpdatePisConsentPsuDataResponse> actual = pisConsentController.updateConsentAuthorization(WRONG_AUTHORISATION_ID, getUpdatePisConsentPsuDataRequest());
        ResponseEntity<UpdatePisConsentPsuDataResponse> expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        assertEquals(actual, expected);
    }

    private PisConsentRequest getPisConsentRequest() {
        return new PisConsentRequest();
    }

    private CreatePisConsentResponse getCreatePisConsentResponse() {
        return new CreatePisConsentResponse(CONSENT_ID);
    }

    private PisConsentResponse getPisConsentResponse() {
        return new PisConsentResponse();
    }

    private CreatePisConsentAuthorisationResponse getCreatePisConsentAuthorisationResponse() {
        return new CreatePisConsentAuthorisationResponse(AUTHORISATION_ID);
    }

    private UpdatePisConsentPsuDataRequest getUpdatePisConsentPsuDataRequest() {
        UpdatePisConsentPsuDataRequest request = new UpdatePisConsentPsuDataRequest();
        request.setPaymentId(PAYMENT_ID);
        return request;
    }

    private UpdatePisConsentPsuDataResponse getUpdatePisConsentPsuDataResponse() {
        return new UpdatePisConsentPsuDataResponse(CmsScaStatus.RECEIVED);
    }
}
