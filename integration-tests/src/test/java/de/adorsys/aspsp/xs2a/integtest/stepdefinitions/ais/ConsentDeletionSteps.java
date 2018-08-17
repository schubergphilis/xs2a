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

package de.adorsys.aspsp.xs2a.integtest.stepdefinitions.ais;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.adorsys.aspsp.xs2a.domain.consent.CreateConsentReq;
import de.adorsys.aspsp.xs2a.domain.consent.CreateConsentResponse;
import de.adorsys.aspsp.xs2a.integtest.model.TestData;
import de.adorsys.aspsp.xs2a.integtest.util.ConsentTestData;
import de.adorsys.aspsp.xs2a.integtest.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

// muss hier nicht eine annotation?
//@FeatureFileSteps
public class ConsentDeletionSteps {

    @Autowired
    @Qualifier("xs2a")
    private RestTemplate restTemplate;

   // @Autowired
    // TODO: Create Classes for request and Response
    // private Context<CreateConsentDeletionReq, HashMap, CreateConsentDeletionResponse> context;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConsentTestData testConsent;


    @Given("^PSU wants to delete the consent (.*)$")
    public void psuWantsToDeleteTheConsentConsentResource(String dataFileName) {

        //TODO:
       // TestData<CreateConsentDeletionReq, HashMap> data = mapper.readValue(resourceToString("/data-input/ais/consent/deletion" + dataFileName, UTF_8), new TypeReference<TestData<CreateConsentReq, HashMap>>() {
        // });

      //  context.setTestData(data);
    }

    @When("^PSU deletes consent$")
    public void psuDeletesConsent(){
        //TODO:
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAll(context.getTestData().getRequest().getHeader());
//        headers.add("Authorization", "Bearer " + context.getAccessToken());
//        headers.add("Content-Type", "application/json");
//        HttpEntity<CreateConsentReq> entity = new HttpEntity<>(context.getTestData().getRequest().getBody(), headers);
//
//        ResponseEntity<CreateConsentResponse> response = restTemplate.exchange(
//            context.getBaseUrl() + "/consents",
//            HttpMethod.POST,
//            entity,
//            CreateConsentDeletionResponse.class);
//
//        context.setActualResponse(response);

    }

    @Then("^a successful response code and the appropriate messages get returned$")
    public void aSuccessfulResponseCodeAndTheAppropriateMessagesGetReturned(){
        //TODO:
//        ResponseEntity<CreateConsentResponse> actualResponse = context.getActualResponse();
//        Map givenResponseBody = context.getTestData().getResponse().getBody();



    }
}
