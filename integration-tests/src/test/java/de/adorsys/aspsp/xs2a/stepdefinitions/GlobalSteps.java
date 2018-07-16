package de.adorsys.aspsp.xs2a.stepdefinitions;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@ConfigurationProperties
public class GlobalSteps {

    @Autowired
    private Context context;

    @Value("${xs2a.baseUrl}")
    private String baseUrl;

    @Value("${xs2a.auth.clientId}")
    private String clientId;

    @Value("${xs2a.auth.clientSecret}")
    private String clientSecret;

    @Value("${xs2a.auth.url}")
    private String keycloakUrl;

    @Value("${xs2a.auth.grantType}")
    private String grantType;

    @Value("${xs2a.auth.username}")
    private String username;

    @Value("${xs2a.auth.password}")
    private String password;



    @Given("^PSU is logged in$")
    public void loginPsu() {
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", grantType);
        map.add("password", password);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", username);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<HashMap> response = template.exchange(keycloakUrl, HttpMethod.POST, entity, HashMap.class);

        context.setAccessToken(response.getBody().get("access_token").toString());
    }

    @And("^(.*) approach is used$")
    public void scaApproach(String scaApproach) {
        context.setScaApproach(scaApproach);
    }
}
