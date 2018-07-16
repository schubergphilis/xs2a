package de.adorsys.aspsp.xs2a.stepdefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import de.adorsys.aspsp.xs2a.domain.pis.SinglePayments;

@FeatureFileSteps
public class PISSteps {

    @Autowired
    private Context context;

    /* see GlobalSteps.java
        @Given("^PSU is logged in$")
    */

    /* see GlobalSteps.java
        @And("^(.*) approach is used$")
    */

    @And("^PSU wants to initiate a single payment (.*) using the payment product (.*)$")
    public void initiateSinglePayment(String paymentProduct, String dataFileName) throws IOException {
        context.setPaymentProduct(paymentProduct);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(new File("src/main/test/resources/data-input/" + dataFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        de.adorsys.aspsp.xs2a.domain.pis.SinglePayments payments = SingleP
        node.

    }


//
//    And <sca-approach> approach is used
//    And PSU wants to initiate a single payment <single-payment> using the payment product <payment-product>
//    When PSU sends the payment initiating request
//    Then a payment resource is created at the aspsp mock
//    And a successful response code and
//    And the appropriate single payment response data is delivered to the PSU

}
