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

package de.adorsys.aspsp.xs2a.domain.consents;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.adorsys.aspsp.xs2a.component.JsonConverter;
import de.adorsys.psd2.model.AccountAccess;
import de.adorsys.psd2.model.AccountReferenceIban;
import de.adorsys.psd2.model.AccountReferenceMaskedPan;
import de.adorsys.psd2.model.Consents;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SpiAccountConsentModelsTest {
    private static final String CREATE_CONSENT_REQ_JSON_PATH = "/json/CreateAccountConsentReqTest.json";
    private static final String NO_DEDICATE_REQ_PATH = "/json/CreateConsentsNoDedicateAccountReqTest.json";
    private final String CREATE_CONSENT_REQ_WRONG_JSON_PATH = "/json/CreateAccountConsentReqWrongTest.json";
    private static final Charset UTF_8 = Charset.forName("utf-8");
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private JsonConverter jsonConverter = new JsonConverter(objectMapper);

    @Test
    public void Consents_jsonTest() throws IOException {
        //Given:
        String requestStringJson = IOUtils.resourceToString(CREATE_CONSENT_REQ_JSON_PATH, UTF_8);
        Consents expectedRequest = getCreateConsentsRequestTest();

        //When:
        Consents actualRequest = jsonConverter.toObject(requestStringJson, Consents.class).get();

        //Then:
        assertThat(actualRequest).isEqualTo(expectedRequest);
    }

    @Test
    public void shouldFail_ConsentsValidation_json() throws IOException {
        //Given:
        String requestStringJson = IOUtils.resourceToString(CREATE_CONSENT_REQ_WRONG_JSON_PATH, UTF_8);

        Consents actualRequest = jsonConverter.toObject(requestStringJson, Consents.class).get();

        //When:
        Set<ConstraintViolation<Consents>> actualViolations = validator.validate(actualRequest);

        //Then:
        assertThat(actualViolations.size()).isEqualTo(1);
    }

    @Test
    public void shouldFail_ConsentsValidation_object() {
        //Given:
        Consents wrongCreateConsentsRequest = getCreateConsentsRequestTest();
        wrongCreateConsentsRequest.setAccess(null);

        //When:
        Set<ConstraintViolation<Consents>> actualOneViolation = validator.validate(wrongCreateConsentsRequest);

        //Then:
        assertThat(actualOneViolation.size()).isEqualTo(1);

        //Given:
        wrongCreateConsentsRequest.setValidUntil(null);

        //When:
        Set<ConstraintViolation<Consents>> actualTwoViolations = validator.validate(wrongCreateConsentsRequest);

        //Then:
        assertThat(actualTwoViolations.size()).isEqualTo(2);
    }

    @Test
    public void ConsentsValidation() throws IOException {
        //Given:
        String requestStringJson = IOUtils.resourceToString(CREATE_CONSENT_REQ_JSON_PATH, UTF_8);
        Consents actualRequest = jsonConverter.toObject(requestStringJson, Consents.class).get();

        //When:
        Set<ConstraintViolation<Consents>> actualViolations = validator.validate(actualRequest);

        //Then:
        assertThat(actualViolations.size()).isEqualTo(0);
    }

    @Test
    public void createConsentNoDedicateAccountReq_jsonTest() throws IOException {
        //Given:
        String requestStringJson = IOUtils.resourceToString(NO_DEDICATE_REQ_PATH, UTF_8);
        Consents expectedRequest = getAicNoDedicatedAccountRequest();

        //When:
        Consents actualRequest = jsonConverter.toObject(requestStringJson, Consents.class).get();

        //Then:
        assertThat(actualRequest).isEqualTo(expectedRequest);
    }

    private Consents getAicNoDedicatedAccountRequest() {

        AccountAccess accountAccess = new AccountAccess().accounts(Collections.emptyList())
            .transactions(Collections.emptyList()).balances(Collections.emptyList());

        Consents aicRequestObj = new Consents();
        aicRequestObj.setAccess(accountAccess);
        aicRequestObj.setRecurringIndicator(true);
        aicRequestObj.setValidUntil(LocalDate.parse("2017-11-01"));
        aicRequestObj.setFrequencyPerDay(4);

        return aicRequestObj;
    }

    private Consents getCreateConsentsRequestTest() {

        AccountReferenceIban iban1 = new AccountReferenceIban();
        iban1.setIban("DE2310010010123456789");

        AccountReferenceIban iban2 = new AccountReferenceIban();
        iban2.setIban("DE2310010010123456790");
        iban2.setCurrency("USD");

        AccountReferenceIban iban3 = new AccountReferenceIban();
        iban3.setIban("DE2310010010123456788");

        AccountReferenceIban iban4 = new AccountReferenceIban();
        iban4.setIban("DE2310010010123456789");

        AccountReferenceMaskedPan maskedPan = new AccountReferenceMaskedPan();
        maskedPan.setMaskedPan("123456xxxxxx1234");

        List<Object> balances = Arrays.asList(iban1, iban2, iban3);
        List<Object> transactions = Arrays.asList(iban4, maskedPan);

        AccountAccess accountAccess = new AccountAccess().accounts(Collections.emptyList())
            .balances(balances).transactions(transactions);

        Consents aicRequestObj = new Consents();
        aicRequestObj.setAccess(accountAccess);
        aicRequestObj.setRecurringIndicator(true);
        aicRequestObj.setValidUntil(LocalDate.parse("2017-11-01"));
        aicRequestObj.setFrequencyPerDay(4);

        return aicRequestObj;
    }
}
