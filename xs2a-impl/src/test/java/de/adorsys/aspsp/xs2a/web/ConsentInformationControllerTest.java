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

import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.service.ConsentService;
import de.adorsys.aspsp.xs2a.service.mapper.ResponseMapper;
import de.adorsys.aspsp.xs2a.web12.ConsentController;
import de.adorsys.psd2.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.util.StringUtils.isEmpty;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInformationControllerTest {
    private final String CORRECT_PSU_ID = "ID 777";
    private final String WRONG_PSU_ID = "ID 666";
    private final String CONSENT_ID = "XXXX-YYYY-XXXX-YYYY";
    private final String WRONG_CONSENT_ID = "YYYY-YYYY-YYYY-YYYY";

    @InjectMocks
    private ConsentController consentInformationController;

    @Mock
    private ConsentService consentService;

    @Mock
    private ResponseMapper responseMapper;

    @Before
    public void setUp() {
        when(consentService.createAccountConsentsWithResponse(any(), eq(CORRECT_PSU_ID))).thenReturn(createConsentResponse(CONSENT_ID));
        when(consentService.createAccountConsentsWithResponse(any(), eq(WRONG_PSU_ID))).thenReturn(createConsentResponse(null));
        when(consentService.getAccountConsentsStatusById(CONSENT_ID)).thenReturn(ResponseObject.<ConsentStatusResponse200>builder().body(new ConsentStatusResponse200().consentStatus(ConsentStatus.RECEIVED)).build());
        when(consentService.getAccountConsentsStatusById(WRONG_CONSENT_ID)).thenReturn(ResponseObject.<ConsentStatusResponse200>builder()
            .fail(Arrays.asList(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICRESOURCEUNKNOWN404403400.CodeEnum.UNKNOWN)))
            .build());
        when(consentService.getAccountConsentById(CONSENT_ID)).thenReturn(getConsent(CONSENT_ID));
        when(consentService.getAccountConsentById(WRONG_CONSENT_ID)).thenReturn(getConsent(WRONG_CONSENT_ID));
        when(consentService.deleteAccountConsentsById(CONSENT_ID)).thenReturn(ResponseObject.<Void>builder().build());
        when(consentService.deleteAccountConsentsById(WRONG_CONSENT_ID)).thenReturn(ResponseObject.<Void>builder()
            .fail(Arrays.asList(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICRESOURCEUNKNOWN404403400.CodeEnum.UNKNOWN)))
            .build());
    }

    @Test
    public void createAccountConsent_Success() {
        when(responseMapper.created(any())).thenReturn(new ResponseEntity<>(createConsentResponse(CONSENT_ID).getBody(), HttpStatus.CREATED));
        //Given:
        Consents consentRequest = new Consents();

        //When:
        ResponseEntity responseEntity = consentInformationController.createConsent(null, consentRequest, null,
            null, null, CORRECT_PSU_ID, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null, null);
        ConsentsResponse201 resp = (ConsentsResponse201) responseEntity.getBody();

        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getConsentStatus()).isEqualTo(ConsentStatus.RECEIVED);
        assertThat(resp.getConsentId()).isEqualTo(CONSENT_ID);
    }

    @Test
    public void createAccountConsent_Failure() {
        when(responseMapper.created(any())).thenReturn(new ResponseEntity<>(createConsentResponse(null).getError(), HttpStatus.NOT_FOUND));
        //Given:
        Consents consentRequest = new Consents();
        //When:
        ResponseEntity responseEntity = consentInformationController.createConsent(null, consentRequest, null,
            null, null, WRONG_PSU_ID, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null, null);
        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAccountConsentsStatusById_Success() {
        when(responseMapper.ok(any())).thenReturn(new ResponseEntity<>(ConsentStatus.RECEIVED, HttpStatus.OK));
        //When:
        ResponseEntity responseEntity = consentInformationController.getConsentStatus(CONSENT_ID, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null);
        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(ConsentStatus.RECEIVED);
    }

    @Test
    public void getAccountConsentsStatusById_Failure() {
        when(responseMapper.ok(any())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        //When:
        ResponseEntity responseEntity = consentInformationController.getConsentStatus(WRONG_CONSENT_ID, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null);
        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getAccountConsentsInformationById_Success() {
        when(responseMapper.ok(any())).thenReturn(new ResponseEntity<>(getConsent(CONSENT_ID).getBody(), HttpStatus.OK));
        //When:
        ResponseEntity responseEntity = consentInformationController.getConsentInformation(CONSENT_ID, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            null, null);
        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isExactlyInstanceOf(ConsentInformationResponse200Json.class);
    }

    @Test
    public void getAccountConsentsInformationById_Failure() {
        when(responseMapper.ok(any())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        //When:
        ResponseEntity responseEntity = consentInformationController.getConsentInformation(WRONG_CONSENT_ID, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            null, null);
        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteAccountConsent_Success() {
        when(responseMapper.delete(any())).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        //When:
        ResponseEntity responseEntity = consentInformationController.deleteConsent(CONSENT_ID, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            null, null);
        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteAccountConsent_Failure() {
        when(responseMapper.delete(any())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        //When:
        ResponseEntity responseEntity = consentInformationController.deleteConsent(WRONG_CONSENT_ID, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            null, null);
        //Then:
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private ResponseObject createConsentResponse(String consentId) {
        return isEmpty(consentId)
            ? ResponseObject.builder()
            .fail(Arrays.asList(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICRESOURCEUNKNOWN404403400.CodeEnum.UNKNOWN)))
            .build()
            : ResponseObject.builder().body(new ConsentsResponse201().consentStatus(ConsentStatus.RECEIVED).consentId(consentId)._links(new HashMap())).build();
    }

    private ResponseObject getConsent(String consentId) {
        ConsentInformationResponse200Json accountConsent = consentId.equals(WRONG_CONSENT_ID)
            ? null
            : new ConsentInformationResponse200Json()
            .access(new AccountAccess())
            .lastActionDate(LocalDate.now())
            .validUntil(LocalDate.now())
            .frequencyPerDay(4)
            .consentStatus(ConsentStatus.VALID)
            .recurringIndicator(false);
        return isEmpty(accountConsent)
            ? ResponseObject.builder()
            .fail(Arrays.asList(new TppMessageGeneric().category(TppMessageCategory.ERROR).code(TppMessageGENERICRESOURCEUNKNOWN404403400.CodeEnum.UNKNOWN)))
            .build()
            : ResponseObject.builder().body(accountConsent).build();
    }

}
