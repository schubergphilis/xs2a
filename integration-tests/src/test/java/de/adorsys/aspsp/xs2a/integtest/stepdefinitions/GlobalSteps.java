package de.adorsys.aspsp.xs2a.integtest.stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@de.adorsys.aspsp.xs2a.integtest.stepdefinitions.FeatureFileSteps
public class GlobalSteps {

    @Autowired
    private de.adorsys.aspsp.xs2a.integtest.stepdefinitions.Context context;

    @Autowired
    @Qualifier("xs2a")
    private RestTemplate template;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.url}")
    private String keycloakUrl;

    @Value("${auth.grantType}")
    private String grantType;

    @Value("${auth.username}")
    private String username;

    @Value("${auth.password}")
    private String password;

    @Given("^PSU is logged in$")
    public void loginPsu() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", grantType);
        map.add("password", password);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", username);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<HashMap> response = null;
        try {
            response = template.exchange(keycloakUrl, HttpMethod.POST, entity, HashMap.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

//        context.setAccessToken(response.getBody().get("access_token").toString());
    }

    @And("^(.*) approach is used$")
    public void scaApproach(String scaApproach) {
        context.setScaApproach(scaApproach);
    }
}
