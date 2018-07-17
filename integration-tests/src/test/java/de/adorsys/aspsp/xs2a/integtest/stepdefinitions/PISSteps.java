package de.adorsys.aspsp.xs2a.integtest.stepdefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;
import de.adorsys.aspsp.xs2a.integtest.model.TestData;
import de.adorsys.aspsp.xs2a.integtest.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@FeatureFileSteps
public class PISSteps {

    @Autowired
    @Qualifier("aspsp-mock")
    private RestTemplate restTemplate;

    @Autowired
    private Context context;

    /* see GlobalSteps.java
        @Given("^PSU is logged in$")
    */

    /* see GlobalSteps.java
        @And("^(.*) approach is used$")
    */

    @And("^PSU wants to initiate a single payment (.*) using the payment product (.*)$")
    public void loadTestData(String dataFileName, String paymentProduct) throws IOException {
        context.setPaymentProduct(paymentProduct);

        File jsonFile = new File("src/test/resources/data-input/" + dataFileName);

        ObjectMapper mapper = new ObjectMapper();
        TestData<SinglePayments> data = mapper.readValue(jsonFile, new TypeReference<TestData<SinglePayments>>() {});

        context.setTestData(data);
    }

    @When("^PSU sends the payment initiating request$")
    public void sendPaymentInitiatingRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(context.getTestData().getRequest().getHeader());
        headers.add("Authorization", "Bearer " + context.getAccessToken());

        HttpEntity<SinglePayments> entity = new HttpEntity<>(
            (SinglePayments) context.getTestData().getRequest().getBody(),
            headers);

        ResponseEntity<HashMap> response = restTemplate.exchange(
            context.getMockUrl() + "/payments/" + context.getPaymentProduct(),
            HttpMethod.POST,
            entity,
            HashMap.class);

        context.setResponse(response);
    }

    @Then("^a payment resource is created at the aspsp mock$")
    public void checkResponse() {
        ResponseEntity<HashMap> response = context.getResponse();
        assertThat(response.getStatusCode(), equalTo(context.getTestData().getResponse().getCode()));
        assertThat(((HashMap)response.getBody().get("_links")).get("redirect"), notNullValue());
    }
}
