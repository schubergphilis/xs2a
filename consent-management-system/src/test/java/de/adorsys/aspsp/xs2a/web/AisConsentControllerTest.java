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

import de.adorsys.aspsp.xs2a.consent.api.AisConsentStatusResponse;
import de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus;
import de.adorsys.aspsp.xs2a.consent.api.UpdateConsentAspspDataRequest;
import de.adorsys.aspsp.xs2a.consent.api.ais.AisConsentAuthorisationRequest;
import de.adorsys.aspsp.xs2a.consent.api.ais.AisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.consent.api.ais.CreateAisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.consent.api.ais.CreateAisConsentResponse;
import de.adorsys.aspsp.xs2a.service.AisConsentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus.EXPIRED;
import static de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus.RECEIVED;
import static de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus.VALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AisConsentControllerTest {

    private static final String CONSENT_ID = "ed4190c7-64ee-42fb-b671-d62645f54672";
    private static final String WRONG_CONSENT_ID = "Wrong consent id";
    private static final CmsConsentStatus CONSENT_STATUS = VALID;
    private static final CmsConsentStatus WRONG_CONSENT_STATUS = EXPIRED;
    private static final AisConsentAuthorisationRequest CONSENT_AUTHORISATION_REQUEST = getConsentAuthorisationRequest();
    private static final AisConsentAuthorisationRequest WRONG_CONSENT_AUTHORISATION_REQUEST = getWrongConsentAuthorisationRequest();
    private static final String PSU_ID = "4e5dbef0-2377-483f-9ab9-ad510c1a557a";
    private static final String WRONG_PSU_ID = "Wrong psu id";
    private static final String AUTHORISATION_ID = "2400de4c-1c74-4ca0-941d-8f56b828f31d";
    private static final String AUTHORISATION_ID_1 = "4400de4c-1c74-4ca0-941d-8f56b828f31d";
    private static final String WRONG_AUTHORISATION_ID = "Wrong authorisation id";
    private static final AisConsentAuthorisationResponse CONSENT_AUTHORISATION_RESPONSE = getConsentAuthorisationResponse();

    @InjectMocks
    private AisConsentController aisConsentController;

    @Mock
    private AisConsentService aisConsentService;

    @Before
    public void setUp() {
        when(aisConsentService.updateAspspConsentData(eq(CONSENT_ID), any())).thenReturn(Optional.of(CONSENT_ID));
        when(aisConsentService.updateAspspConsentData(eq(WRONG_CONSENT_ID), any())).thenReturn(Optional.empty());
        when(aisConsentService.getConsentStatusById(eq(CONSENT_ID))).thenReturn(Optional.of(RECEIVED));
        when(aisConsentService.getConsentStatusById(eq(WRONG_CONSENT_ID))).thenReturn(Optional.empty());
        when(aisConsentService.updateConsentStatusById(eq(CONSENT_ID), eq(CONSENT_STATUS))).thenReturn(true);
        when(aisConsentService.updateConsentStatusById(eq(WRONG_CONSENT_ID), eq(WRONG_CONSENT_STATUS))).thenReturn(false);
        when(aisConsentService.createAuthorisation(eq(CONSENT_ID), eq(CONSENT_AUTHORISATION_REQUEST))).thenReturn(Optional.of(AUTHORISATION_ID));
        when(aisConsentService.createAuthorisation(eq(WRONG_CONSENT_ID), eq(CONSENT_AUTHORISATION_REQUEST))).thenReturn(Optional.empty());
        when(aisConsentService.createAuthorisation(eq(CONSENT_ID), eq(WRONG_CONSENT_AUTHORISATION_REQUEST))).thenReturn(Optional.empty());
        when(aisConsentService.updateConsentAuthorisation(eq(AUTHORISATION_ID_1), eq(CONSENT_ID), eq(CONSENT_AUTHORISATION_REQUEST))).thenReturn(true);
        when(aisConsentService.updateConsentAuthorisation(eq(AUTHORISATION_ID), eq(WRONG_CONSENT_ID), eq(CONSENT_AUTHORISATION_REQUEST))).thenReturn(false);
        when(aisConsentService.updateConsentAuthorisation(eq(WRONG_AUTHORISATION_ID), eq(CONSENT_ID), eq(WRONG_CONSENT_AUTHORISATION_REQUEST))).thenReturn(false);
        when(aisConsentService.getAccountConsentAuthorisationById(eq(AUTHORISATION_ID), eq(CONSENT_ID))).thenReturn(Optional.of(CONSENT_AUTHORISATION_RESPONSE));
        when(aisConsentService.getAccountConsentAuthorisationById(eq(AUTHORISATION_ID), eq(WRONG_CONSENT_ID))).thenReturn(Optional.empty());
        when(aisConsentService.getAccountConsentAuthorisationById(eq(WRONG_AUTHORISATION_ID), eq(CONSENT_ID))).thenReturn(Optional.empty());
    }

    @Test
    public void getConsentStatusById_Success() {

        //When:
        ResponseEntity<AisConsentStatusResponse> responseEntity = aisConsentController.getConsentStatusById(CONSENT_ID);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getConsentStatus()).isEqualTo(RECEIVED);
    }

    @Test
    public void getConsentStatusById_Fail() {

        //When:
        ResponseEntity<AisConsentStatusResponse> responseEntity = aisConsentController.getConsentStatusById(WRONG_CONSENT_ID);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateConsentStatus_Success() {

        //When:
        ResponseEntity responseEntity = aisConsentController.updateConsentStatus(CONSENT_ID, CONSENT_STATUS.name());

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateConsentStatus_Fail() {

        //When:
        ResponseEntity responseEntity = aisConsentController.updateConsentStatus(WRONG_CONSENT_ID, CONSENT_STATUS.name());

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createConsentAuthorisation_Success() {

        //Given:
        AisConsentAuthorisationRequest expectedRequest = getConsentAuthorisationRequest();

        //When:
        ResponseEntity<CreateAisConsentAuthorisationResponse> responseEntity = aisConsentController.createConsentAuthorisation(CONSENT_ID, expectedRequest);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getAuthorisationId()).isEqualTo(AUTHORISATION_ID);
    }

    @Test
    public void createConsentAuthorisation_Fail_WrongConsentId() {

        //Given:
        AisConsentAuthorisationRequest expectedRequest = getConsentAuthorisationRequest();

        //When:
        ResponseEntity<CreateAisConsentAuthorisationResponse> responseEntity = aisConsentController.createConsentAuthorisation(WRONG_CONSENT_ID, expectedRequest);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createConsentAuthorisation_Fail_WrondRequest() {

        //Given:
        AisConsentAuthorisationRequest expectedRequest = getWrongConsentAuthorisationRequest();

        //When:
        ResponseEntity<CreateAisConsentAuthorisationResponse> responseEntity = aisConsentController.createConsentAuthorisation(CONSENT_ID, expectedRequest);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateConsentAuthorisation_Success() {
        doReturn(true)
            .when(aisConsentService).updateConsentAuthorisation(anyString(), anyString(), any(AisConsentAuthorisationRequest.class));

        //Given:
        AisConsentAuthorisationRequest expectedRequest = getConsentAuthorisationRequest();

        //When:
        ResponseEntity responseEntity = aisConsentController.updateConsentAuthorisation(AUTHORISATION_ID_1, CONSENT_ID, expectedRequest);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateConsentAuthorisation_Fail_WrongConsentId() {

        //Given:
        AisConsentAuthorisationRequest expectedRequest = getConsentAuthorisationRequest();

        //When:
        ResponseEntity responseEntity = aisConsentController.updateConsentAuthorisation(AUTHORISATION_ID, WRONG_CONSENT_ID, expectedRequest);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateConsentAuthorisation_Fail_WrongAuthorisationId() {

        //Given:
        AisConsentAuthorisationRequest expectedRequest = getConsentAuthorisationRequest();

        //When:
        ResponseEntity responseEntity = aisConsentController.updateConsentAuthorisation(WRONG_AUTHORISATION_ID, CONSENT_ID, expectedRequest);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateConsentAuthorisation_Fail_WrongRequest() {

        //Given:
        AisConsentAuthorisationRequest expectedRequest = getWrongConsentAuthorisationRequest();

        //When:
        ResponseEntity responseEntity = aisConsentController.updateConsentAuthorisation(AUTHORISATION_ID, CONSENT_ID, expectedRequest);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getConsentAuthorisation_Success() {

        //When:
        ResponseEntity<AisConsentAuthorisationResponse> responseEntity = aisConsentController.getConsentAuthorisation(CONSENT_ID, AUTHORISATION_ID);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(getConsentAuthorisationResponse());
    }

    @Test
    public void getConsentAuthorisation_Fail_WrongConsentId() {

        //When:
        ResponseEntity<AisConsentAuthorisationResponse> responseEntity = aisConsentController.getConsentAuthorisation(WRONG_CONSENT_ID, AUTHORISATION_ID);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getConsentAuthorisation_Fail_WrongAuthorisationId() {

        //When:
        ResponseEntity<AisConsentAuthorisationResponse> responseEntity = aisConsentController.getConsentAuthorisation(CONSENT_ID, WRONG_AUTHORISATION_ID);

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private static AisConsentAuthorisationRequest getConsentAuthorisationRequest() {
        AisConsentAuthorisationRequest request = new AisConsentAuthorisationRequest();
        request.setPsuId(PSU_ID);
        request.setPassword("zzz");

        return request;
    }

    private static AisConsentAuthorisationRequest getWrongConsentAuthorisationRequest() {
        AisConsentAuthorisationRequest request = new AisConsentAuthorisationRequest();
        request.setPsuId(WRONG_PSU_ID);
        request.setPassword("zzz");

        return request;
    }

    private static AisConsentAuthorisationResponse getConsentAuthorisationResponse() {
        AisConsentAuthorisationResponse authorisationResponse = new AisConsentAuthorisationResponse();
        authorisationResponse.setAuthorisationId(AUTHORISATION_ID);
        authorisationResponse.setConsentId(CONSENT_ID);
        authorisationResponse.setPsuId(PSU_ID);

        return authorisationResponse;
    }


}
