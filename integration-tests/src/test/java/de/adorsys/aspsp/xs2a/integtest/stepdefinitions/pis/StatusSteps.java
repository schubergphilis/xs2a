package de.adorsys.aspsp.xs2a.integtest.stepdefinitions.pis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.adorsys.aspsp.xs2a.exception.MessageError;
import de.adorsys.aspsp.xs2a.integtest.entities.ITMessageError;
import de.adorsys.aspsp.xs2a.integtest.model.TestData;
import de.adorsys.aspsp.xs2a.integtest.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@FeatureFileSteps
public class StatusSteps {

    @Autowired
    @Qualifier("xs2a")
    private RestTemplate restTemplate;

    @Autowired
    private Context< HashMap, HashMap, ITMessageError> context;

    @Autowired
    private ObjectMapper mapper;

    @Given("^PSU wants to request a payment status without an existing payment-id (.*) using the payment product (.*)$")
    public void setPaymentParameters(String paymentId, String paymentProduct) throws IOException {
        context.setPaymentProduct(paymentProduct);
        context.setPaymentId(paymentId);
    }

    @And("^the set of data (.*)$")
    public void loadTestData(String dataFileName) throws IOException {
        TestData<HashMap, HashMap> data = mapper.readValue(resourceToString("/data-input/pis/status/" + dataFileName, UTF_8), new TypeReference<TestData<HashMap, HashMap>>() {
        });

        context.setTestData(data);
    }

    @When("^PSU requests the status of the payment$")
    public void sendPaymentStatusRequest() throws HttpClientErrorException, IOException {
        HttpEntity<HashMap> entity = getStatusHttpEntity();

        try {
            restTemplate.exchange(
                context.getBaseUrl() + "/payments/" + context.getPaymentProduct() + "/" + context.getPaymentId() + "/status",
                HttpMethod.GET,
                entity,
                HashMap.class);
        } catch (RestClientResponseException rex){
            handleRequestError(rex);
        }
    }

    private void handleRequestError(RestClientResponseException exceptionObj) throws IOException {
        ResponseEntity<ITMessageError> actualResponse = new ResponseEntity<>(HttpStatus.valueOf(exceptionObj.getRawStatusCode()));
        context.setActualResponse(actualResponse);

        String responseBodyAsString = exceptionObj.getResponseBodyAsString();
        ITMessageError messageError = mapper.readValue(responseBodyAsString, ITMessageError.class);
        context.setMessageError(messageError);
    }

    @Then("^an appropriate response code and the status is delivered to the PSU$")
    public void checkStatus() throws IOException {
        HttpStatus httpStatus = context.getTestData().getResponse().getHttpStatus();
        assertThat(context.getActualResponse().getStatusCode(), equalTo(httpStatus));

        ITMessageError givenErrorObject = context.getMessageError();
        HashMap givenResponseBody = context.getTestData().getResponse().getBody();
        String status = (String) givenResponseBody.get("transactionStatus");
        HashMap tppMessageContent = (HashMap) givenResponseBody.get("tppMessage");

        if (givenErrorObject.getTransactionStatus() != null ) {
            assertThat(givenErrorObject.getTransactionStatus().toString(), equalTo(status));
        }
        if (givenErrorObject.getTppMessage() != null ) {
            assertThat(givenErrorObject.getTppMessage().getCategory().name(), equalTo(tppMessageContent.get("category")));
            assertThat(givenErrorObject.getTppMessage().getCode().name(), equalTo(tppMessageContent.get("code")));
        }
    }

    private HttpEntity getStatusHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(context.getTestData().getRequest().getHeader());
        headers.add("Authorization", "Bearer " + context.getAccessToken());
        headers.add("Content-Type", "application/json");

        return new HttpEntity<>(null, headers);
    }
}
