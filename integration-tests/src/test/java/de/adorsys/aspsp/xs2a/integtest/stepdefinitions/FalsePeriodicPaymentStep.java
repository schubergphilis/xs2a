package de.adorsys.aspsp.xs2a.integtest.stepdefinitions;

import cucumber.api.java.en.When;
import de.adorsys.aspsp.xs2a.domain.pis.PaymentInitialisationResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.aspsp.xs2a.integtest.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@FeatureFileSteps
public class FalsePeriodicPaymentStep {

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

    @When("^PSU sends the recurring payment initiating request with errors$")
    public void sendFalsePeriodicPaymentInitiatingRequest() {
        HttpHeaders header = new HttpHeaders();
        header.setAll(context.getTestData().getRequest().getHeader());
        header.add("Authorization", "Bearer" + context.getAccessToken());
        header.add("Content-Type", "application/json");

        HttpEntity<PeriodicPayment> entity = new HttpEntity<>(
            (PeriodicPayment) context.getTestData().getRequest().getBody(), header);

        try {
            restTemplate.exchange(
                context.getBaseUrl() + "/periodic-payments/" + context.getPaymentProduct(),
                HttpMethod.POST,
                entity,
                HashMap.class);


        } catch (HttpClientErrorException hce) {
            ResponseEntity<PaymentInitialisationResponse> actualResponse = new ResponseEntity<>(hce.getStatusCode());
            context.setActualResponse(actualResponse);
        }
    }

    private HttpStatus convertStringToHttpStatusCode(String code) {
        return HttpStatus.valueOf(Integer.valueOf(code));
    }

}
