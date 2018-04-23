package de.adorsys.aspsp.xs2a.web.validation;


import de.adorsys.aspsp.xs2a.component.JsonConverter;
import de.adorsys.aspsp.xs2a.domain.AccountReference;
import de.adorsys.aspsp.xs2a.domain.Amount;
import de.adorsys.aspsp.xs2a.domain.ResponseObject;
import de.adorsys.aspsp.xs2a.domain.fund.FundsConfirmationRequest;
import de.adorsys.aspsp.xs2a.service.FundsConfirmationService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Currency;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ValidationTest {
    private static FundsConfirmationRequest request;
    private final String FUNDS_CONFIRMATION = "/api/v1/funds-confirmations";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonConverter jsonConverter;
    @MockBean
    private FundsConfirmationService fundsConfirmationService;

    @BeforeClass
    public static void initRequest(){
        request = buildRequest();
    }

    @Test
    public void validateHeaderParameterShouldReturn200() throws Exception {
        //When
        when(fundsConfirmationService.fundsConfirmation(request))
                    .thenReturn(ResponseObject.builder().body(anyBoolean()).build());

        //Then
        mvc.perform(MockMvcRequestBuilders.post(FUNDS_CONFIRMATION)
                    .content(jsonConverter.toJson(request).get())
                    .contentType(APPLICATION_JSON_UTF8)
                    .headers(correctHeaders()))
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    public void shouldReturn400() throws Exception {
        //When
        when(fundsConfirmationService.fundsConfirmation(request))
        .thenReturn(ResponseObject.builder().body(false).build());

        //Then
        mvc.perform(MockMvcRequestBuilders.post(FUNDS_CONFIRMATION)
                    .content(jsonConverter.toJson(request).get())
                    .contentType(APPLICATION_JSON_UTF8)
                    .headers(withoutTppTransactionId()))
        .andExpect(status().isBadRequest())
        .andReturn();
    }

    private static FundsConfirmationRequest buildRequest(){
        FundsConfirmationRequest request = new FundsConfirmationRequest();
        request.setInstructedAmount(buildAmount());
        request.setPsuAccount(buildAccountReference());
        request.setCardNumber("5434534535");
        request.setPayee("345f");
        return request;
    }

    private static AccountReference buildAccountReference() {
        AccountReference account = new AccountReference();
        account.setIban("DE2310010010123456790");
        account.setCurrency(Currency.getInstance("EUR"));
        return account;
    }

    private static Amount buildAmount() {
        final Amount amount = new Amount();
        amount.setContent("23.0");
        amount.setCurrency(Currency.getInstance("EUR"));
        return amount;
    }

    private HttpHeaders withoutTppTransactionId(){
        HttpHeaders headers = correctHeaders();
        headers.remove("tpp-transaction-id");
        return headers;
    }

    private HttpHeaders correctHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("tpp-transaction-id", "16d40f49-a110-4344-a949-f99828ae13c9");
        headers.add("tpp-request-id", "21d40f65-a150-8343-b539-b9a822ae98c0");
        return headers;
    }
}
