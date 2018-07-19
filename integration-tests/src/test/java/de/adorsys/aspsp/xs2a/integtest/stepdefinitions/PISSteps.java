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
import org.springframework.http.*;
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
        TestData<SinglePayments> data = mapper.readValue(jsonFile, new TypeReference<TestData<SinglePayments>>() {
        });

        context.setTestData(data);
    }

    @When("^PSU sends the single payment initiating request$")
    public void sendPaymentInitiatingRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(context.getTestData().getRequest().getHeader());
        headers.add("Authorization", "Bearer " + context.getAccessToken());

        HttpEntity<SinglePayments> entity = new HttpEntity<>(
            (SinglePayments) context.getTestData().getRequest().getBody(),
            headers);

        ResponseEntity<HashMap> response = restTemplate.exchange(
            context.getBaseUrl() + "/payments/" + context.getPaymentProduct(),
            HttpMethod.POST,
            entity,
            HashMap.class);

        context.setResponse(response);
    }

    @Then("^a successful response code and the appropriate single payment response data is delivered to the PSU$")
    public void checkResponseCode() {
        ResponseEntity<HashMap> actualResponse = context.getResponse();
        HashMap<String, String> assertedResponseBody = (HashMap) context.getTestData().getResponse().getBody();

        HttpStatus compareStatus = HttpStatus.valueOf(Integer.valueOf(context.getTestData().getResponse().getCode()));
        assertThat(actualResponse.getStatusCode(), equalTo(compareStatus));

        assertThat(actualResponse.getBody().get("transactionStatus"), equalTo(assertedResponseBody.get("transactionStatus")));
        assertThat(actualResponse.getBody().get("paymentId"), notNullValue());
        assertThat(((HashMap) actualResponse.getBody().get("_links")).get("scaRedirect"), notNullValue());
    }
}
