
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

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BPISStep {

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


    @And("^PSU wants to initiate multiple payments (.*) using the payment product (.*)$")
    public void loadTestData(String dataFileName, String paymentProduct) throws IOException {
        context.setPaymentProduct(paymentProduct);

        File jsonFile = new File("src/test/resources/data-input/pis/bulk/" + dataFileName);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        TestData<List<SinglePayments>> data = mapper.readValue(jsonFile, new TypeReference<TestData<List<SinglePayments>>>() {
        });

        context.setTestData(data);
    }

    @When("^PSU sends the bulk payment initiating request$")
    public void sendPaymentInitiatingRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(context.getTestData().getRequest().getHeader());
        headers.add("Authorization", "Bearer " + context.getAccessToken());
        headers.add("tpp-transaction-id", "16d40f49-a110-4344-a949-f99828ae13c9");

        List<SinglePayments> paymentsList = ((List<SinglePayments>) context.getTestData().getRequest().getBody());

        ResponseEntity<List<PaymentInitialisationResponse>> response = restTemplate.exchange(
            context.getBaseUrl() + "/bulk-payments/" + context.getPaymentProduct(),
            HttpMethod.POST, new HttpEntity<>(paymentsList, headers), new ParameterizedTypeReference<List<PaymentInitialisationResponse>>() {
            });


        context.setResponse(response);
    }

//    @Then("^a successful response code and the appropriate bulk payment response data$")
//    public void checkResponseCode() {
//        ResponseEntity<HashMap> actualResponse = context.getResponse();
//        HashMap<String, String> givenResponseBody = (HashMap) context.getTestData().getResponse().getBody();
//
//        HttpStatus compareStatus = convertStringToHttpStatusCode(context.getTestData().getResponse().getCode());
//        assertThat(actualResponse.getStatusCode(), equalTo(compareStatus));
//
//        assertThat(actualResponse.getBody().get("transactionStatus"), equalTo(givenResponseBody.get("transactionStatus")));
//        assertThat(actualResponse.getBody().get("paymentId"), notNullValue());
//    }
/*
    @And("^a redirect URL is delivered to the PSU$")
    public void checkRedirectUrl() {
        ResponseEntity<HashMap> actualResponse = context.getResponse();

        assertThat(((HashMap) actualResponse.getBody().get("_links")).get("scaRedirect"), notNullValue());
    }
*/
    private HttpStatus convertStringToHttpStatusCode(String code){
        return HttpStatus.valueOf(Integer.valueOf(code));
    }
}

