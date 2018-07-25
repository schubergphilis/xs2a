package de.adorsys.aspsp.xs2a.integtest.stepdefinitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;
import de.adorsys.aspsp.xs2a.integtest.model.TestData;
import de.adorsys.aspsp.xs2a.integtest.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import de.adorsys.aspsp.xs2a.domain.pis.PeriodicPayment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@FeatureFileSteps
public class PISSteps {

    @Autowired
    @Qualifier("xs2a")
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

        File jsonFile = new File("src/test/resources/data-input/pis/single/" + dataFileName);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
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

    @Then("^a successful response code and the appropriate single payment response data$")
    public void checkResponseCode() {
        ResponseEntity<HashMap> actualResponse = context.getResponse();
        HashMap<String, String> givenResponseBody = (HashMap) context.getTestData().getResponse().getBody();

        HttpStatus compareStatus = convertStringToHttpStatusCode(context.getTestData().getResponse().getCode());
        assertThat(actualResponse.getStatusCode(), equalTo(compareStatus));

        assertThat(actualResponse.getBody().get("transactionStatus"), equalTo(givenResponseBody.get("transactionStatus")));
        assertThat(actualResponse.getBody().get("paymentId"), notNullValue());
    }

    @And("^a redirect URL is delivered to the PSU$")
    public void checkRedirectUrl() {
        ResponseEntity<HashMap> actualResponse = context.getResponse();

        assertThat(((HashMap) actualResponse.getBody().get("_links")).get("scaRedirect"), notNullValue());
    }

    private HttpStatus convertStringToHttpStatusCode(String code) {
        return HttpStatus.valueOf(Integer.valueOf(code));
    }

    @And("^PSU wants to initiate a recurring payment (.*) using the payment product (.*)$")
    public void loadTestDataRecPay(String fileName, String paymentProduct) throws IOException {
        context.setPaymentProduct(paymentProduct);

        File jsFile = new File("src/test/resources/data-input/pis/recurring/" + fileName);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        TestData<PeriodicPayment> data = objectMapper.readValue(jsFile, new TypeReference<TestData<PeriodicPayment>>() {
        });

        context.setTestData(data);

    }

    @When("^PSU sends the recurring payment initiating request$")
    public void sendRecPaymentInitiatingRequest() {

        HttpHeaders header = new HttpHeaders();
        header.setAll(context.getTestData().getRequest().getHeader());
        header.add("Authorization", "Bearer" + context.getAccessToken());

        HttpEntity<PeriodicPayment> entity = new HttpEntity<>(
            (PeriodicPayment) context.getTestData().getRequest().getBody(), header);
        ResponseEntity<HashMap> response = restTemplate.exchange(
            context.getBaseUrl() + "/periodic-payments/" + context.getPaymentProduct(),
            HttpMethod.POST,
            entity,
            HashMap.class);

        context.setResponse(response);
    }

    @Then("^a successful response code and the appropriate recurring payment response data")
    public void checkRespCodePerPayment() {
        ResponseEntity<HashMap> resp = context.getResponse();
        HashMap<String, String> respBody = (HashMap) context.getTestData().getResponse().getBody();

        HttpStatus compStatus = convertStringToHttpStatusCode(context.getTestData().getResponse().getCode());

        assertThat(resp.getStatusCode(), equalTo(compStatus));
        assertThat(resp.getBody().get("transactionStatus"), equalTo(respBody.get("transactionStatus")));
        //assertThat(resp.getBody().get("paymentId"), notNullValue());
    }

}
