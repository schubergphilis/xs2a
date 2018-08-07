package de.adorsys.aspsp.xs2a.integtest.stepdefinitions.pis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import de.adorsys.aspsp.xs2a.domain.TppMessageInformation;
import de.adorsys.aspsp.xs2a.domain.TransactionStatus;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;
import de.adorsys.aspsp.xs2a.exception.MessageError;
import de.adorsys.aspsp.xs2a.integtest.entities.ITMessageError;
import de.adorsys.aspsp.xs2a.integtest.model.TestData;
import de.adorsys.aspsp.xs2a.integtest.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@FeatureFileSteps
public class StatusSteps {

    @Autowired
    @Qualifier("xs2a")
    private RestTemplate restTemplate;

    @Autowired
    private Context< HashMap, HashMap, ITMessageError> context;

    /* see GlobalSteps.java
        @Given("^PSU is logged in$")
    */

    /* see GlobalSteps.java
        @And("^(.*) approach is used$")
    */

    @And("^created a payment status request with of a not existing payment-id (.*) using the payment product (.*)$")
    public void setPaymentParameters(String paymentId, String paymentProduct) throws IOException {
        context.setPaymentProduct(paymentProduct);
        context.setPaymentId(paymentId);

    }

    @When("^PSU requests the status of the payment$")
    public void sendPaymentStatusRequest() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-request-id", "2f77a125-aa7a-45c0-b414-cea25a116035");
        headers.add("Authorization", "Bearer " + context.getAccessToken());

        HttpEntity<HashMap> entity = new HttpEntity (headers);

        try {
            restTemplate.exchange(
                context.getBaseUrl() + "/payments/" + context.getPaymentProduct() + "/" + context.getPaymentId() + "/status",
                HttpMethod.GET,
                entity,
                HashMap.class);
        } catch (HttpClientErrorException hce){
            ResponseEntity<ITMessageError> actualResponse = new ResponseEntity<>(hce.getStatusCode());
            context.setActualResponse(actualResponse);
            String responseBodyAsString = hce.getResponseBodyAsString();

            ObjectMapper objectMapper = new ObjectMapper();
            ITMessageError messageError = objectMapper.readValue(responseBodyAsString, ITMessageError.class);
            context.setMessageError(messageError);
        }
    }

    @Then("^an appropriate response code and the status (.*) is delivered to the PSU$")
    public void checkStatus(String dataFileName) throws IOException {

        File jsonFile = new File("src/test/resources/data-input/pis/status/" + dataFileName);
        ObjectMapper mapper = new ObjectMapper();
        TestData<MessageError, HashMap> data = mapper.readValue(jsonFile, new TypeReference<TestData<MessageError, HashMap>>() {
        });

        ResponseEntity<ITMessageError> response = context.getActualResponse();
        HttpStatus httpStatus = convertStringToHttpStatusCode(data.getResponse().getCode());

        assertThat(response.getStatusCode(), equalTo(httpStatus));

        ITMessageError givenErrorObject = context.getMessageError();
        HashMap givenResponseBody = data.getResponse().getBody();
        String status = (String) givenResponseBody.get("transactionStatus");
        HashMap tppMessageContent = (HashMap) givenResponseBody.get("tppMessage");

        assertThat(givenErrorObject.getTransactionStatus().toString(), equalTo(status));
        assertThat(givenErrorObject.getTppMessage().getCategory().name(), equalTo(tppMessageContent.get("category")));
        assertThat(givenErrorObject.getTppMessage().getCode().name(), equalTo(tppMessageContent.get("code")));
    }

    private HttpStatus convertStringToHttpStatusCode(String code){
        return HttpStatus.valueOf(Integer.valueOf(code));
    }


}
