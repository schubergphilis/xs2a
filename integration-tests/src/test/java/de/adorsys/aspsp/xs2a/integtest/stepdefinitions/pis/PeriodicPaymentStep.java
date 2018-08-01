package de.adorsys.aspsp.xs2a.integtest.stepdefinitions.pis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;
import de.adorsys.aspsp.xs2a.integtest.model.TestData;
import de.adorsys.aspsp.xs2a.integtest.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@FeatureFileSteps
public class PeriodicPaymentStep {

    @Autowired
    @Qualifier("xs2a")
    private RestTemplate restTemplate;

    @Autowired
    private Context<PeriodicPayment, HashMap, PaymentInitialisationResponse> context;

    /* see GlobalSteps.java
        @Given("^PSU is logged in$")
    */

    /* see GlobalSteps.java
        @And("^(.*) approach is used$")
    */

    @And("^PSU wants to initiate a recurring payment (.*) using the payment product (.*)$")
    public void loadTestDataForPeriodicPayment(String fileName, String paymentProduct) throws IOException {

        context.setPaymentProduct(paymentProduct);
        File periodicPaymentJsonFile = new File("src/test/resources/data-input/pis/recurring/" + fileName);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        TestData<PeriodicPayment, HashMap> data = objectMapper.readValue(periodicPaymentJsonFile, new TypeReference<TestData<PeriodicPayment, HashMap>>() {
        });
        context.setTestData(data);
    }

    @When("^PSU sends the recurring payment initiating request$")
    public void sendPeriodicPaymentInitiatingRequest() {

        HttpHeaders header = new HttpHeaders();
        header.setAll(context.getTestData().getRequest().getHeader());
        header.add("Authorization", "Bearer" + context.getAccessToken());
        header.add("Content-Type", "application/json");
        HttpEntity<PeriodicPayment> entity = new HttpEntity<>(
            context.getTestData().getRequest().getBody(), header);
        ResponseEntity<PaymentInitialisationResponse> responseEntity = restTemplate.exchange(
            context.getBaseUrl() + "/periodic-payments/" + context.getPaymentProduct(),
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<PaymentInitialisationResponse>() {
            });

        context.setActualResponse(responseEntity);
    }

    @Then("^a successful response code and the appropriate recurring payment response data")
    public void checkResponseCodeFromPeriodicPayment() {

        Map responseBody = context.getTestData().getResponse().getBody();
        ResponseEntity<PaymentInitialisationResponse> responseEntity = context.getActualResponse();
        HttpStatus comparedStatus = convertStringToHttpStatusCode(context.getTestData().getResponse().getCode());
        assertThat(responseEntity.getStatusCode(), equalTo(comparedStatus));
        assertThat(responseEntity.getBody().getTransactionStatus().name(), equalTo(responseBody.get("transactionStatus")));
        assertThat(responseEntity.getBody().getPaymentId(), notNullValue());
    }

    @When("^PSU sends the recurring payment initiating request with error$")
    public void sendFalsePeriodicPaymentInitiatingRequest() {
        HttpHeaders header = new HttpHeaders();
        header.setAll(context.getTestData().getRequest().getHeader());
        header.add("Authorization", "Bearer" + context.getAccessToken());
        header.add("Content-Type", "application/json");

        HttpEntity<PeriodicPayment> entity = new HttpEntity<>(
            context.getTestData().getRequest().getBody(), header);

        try {
            restTemplate.exchange(
                context.getBaseUrl() + "/periodic-payments/" + context.getPaymentProduct(),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<PaymentInitialisationResponse>(){
                });


        } catch (HttpClientErrorException httpClientErrorException) {
            ResponseEntity<PaymentInitialisationResponse> actualResponse = new ResponseEntity<>(
                httpClientErrorException.getStatusCode());
            context.setActualResponse(actualResponse);
        }
    }


    private HttpStatus convertStringToHttpStatusCode(String code) {
        return HttpStatus.valueOf(Integer.valueOf(code));
    }


}
