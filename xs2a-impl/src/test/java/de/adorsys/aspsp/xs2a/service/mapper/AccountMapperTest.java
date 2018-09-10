/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.aspsp.xs2a.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.adorsys.aspsp.xs2a.component.JsonConverter;
import de.adorsys.aspsp.xs2a.domain.CashAccountType;
import de.adorsys.aspsp.xs2a.domain.Transactions;
import de.adorsys.aspsp.xs2a.domain.account.*;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountDetails;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountReference;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiTransaction;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AccountMapperTest {
    private static final String SPI_ACCOUNT_DETAILS_JSON_PATH = "/json/MapSpiAccountDetailsToXs2aAccountDetailsTest.json";
    private static final String SPI_TRANSACTION_JSON_PATH = "/json/AccountReportDataTest.json";
    private static final Charset UTF_8 = Charset.forName("utf-8");

    @InjectMocks
    private AccountMapper accountMapper;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private JsonConverter jsonConverter = new JsonConverter(objectMapper);

    @Test
    public void mapSpiAccountDetailsToXs2aAccountDetails() throws IOException {
        //Given:
        String spiAccountDetailsJson = IOUtils.resourceToString(SPI_ACCOUNT_DETAILS_JSON_PATH, UTF_8);
        SpiAccountDetails donorAccountDetails = jsonConverter.toObject(spiAccountDetailsJson, SpiAccountDetails.class).get();

        //When:
        assertNotNull(donorAccountDetails);
        Xs2aAccountDetails actualAccountDetails = accountMapper.mapToAccountDetails(donorAccountDetails);

        //Then:
        assertThat(actualAccountDetails.getId()).isEqualTo("3dc3d5b3-7023-4848-9853-f5400a64e80f");
        assertThat(actualAccountDetails.getIban()).isEqualTo("DE2310010010123456789");
        assertThat(actualAccountDetails.getBban()).isEqualTo("DE2310010010123452343");
        assertThat(actualAccountDetails.getPan()).isEqualTo("1111222233334444");
        assertThat(actualAccountDetails.getMaskedPan()).isEqualTo("111122xxxxxx4444");
        assertThat(actualAccountDetails.getMsisdn()).isEqualTo("4905123123");
        assertThat(actualAccountDetails.getCurrency()).isEqualTo(Currency.getInstance("EUR"));
        assertThat(actualAccountDetails.getName()).isEqualTo("Main Account");
        assertThat(actualAccountDetails.getProduct()).isEqualTo("Girokonto");
        assertThat(actualAccountDetails.getCashAccountType()).isEqualTo(CashAccountType.CACC);
        assertThat(actualAccountDetails.getAccountStatus()).isEqualTo(AccountStatus.ENABLED);
        assertThat(actualAccountDetails.getBic()).isEqualTo("EDEKDEHHXXX");
        assertThat(actualAccountDetails.getUsageEnum()).isEqualTo(UsageEnum.PRIV);
        assertThat(actualAccountDetails.getDetails()).isEqualTo("Some details");
        assertThat(actualAccountDetails.getBalances()).isEqualTo(Collections.emptyList());
    }

    @Test
    public void mapAccountReport() throws IOException {
        //Given:
        String spiTransactionJson = IOUtils.resourceToString(SPI_TRANSACTION_JSON_PATH, UTF_8);
        SpiTransaction donorSpiTransaction = jsonConverter.toObject(spiTransactionJson, SpiTransaction.class).get();
        List<SpiTransaction> donorSpiTransactions = new ArrayList<>();
        donorSpiTransactions.add(donorSpiTransaction);
        SpiTransaction[] expectedBooked = donorSpiTransactions.stream()
                                              .filter(transaction -> transaction.getBookingDate() != null)
                                              .toArray(SpiTransaction[]::new);

        //When:
        assertNotNull(donorSpiTransaction);
        Optional<Xs2aAccountReport> aAR = accountMapper.mapToAccountReport(donorSpiTransactions);
        Xs2aAccountReport actualAccountReport;
        actualAccountReport = aAR.orElseGet(() -> new Xs2aAccountReport(new Transactions[]{}, new Transactions[]{}));


        //Then:
        assertThat(actualAccountReport.getBooked()[0].getTransactionId())
            .isEqualTo(expectedBooked[0].getTransactionId());
        assertThat(actualAccountReport.getBooked()[0].getEntryReference())
            .isEqualTo(expectedBooked[0].getEntryReference());
        assertThat(actualAccountReport.getBooked()[0].getEndToEndId()).isEqualTo(expectedBooked[0].getEndToEndId());
        assertThat(actualAccountReport.getBooked()[0].getMandateId()).isEqualTo(expectedBooked[0].getMandateId());
        assertThat(actualAccountReport.getBooked()[0].getCheckId()).isEqualTo(expectedBooked[0].getCheckId());
        assertThat(actualAccountReport.getBooked()[0].getCreditorId()).isEqualTo(expectedBooked[0].getCreditorId());
        assertThat(actualAccountReport.getBooked()[0].getBookingDate()).isEqualTo(expectedBooked[0].getBookingDate());
        assertThat(actualAccountReport.getBooked()[0].getValueDate()).isEqualTo(expectedBooked[0].getValueDate());

        assertThat(actualAccountReport.getBooked()[0].getAmount().getAmount())
            .isEqualTo(expectedBooked[0].getSpiAmount().getAmount().toString());
        assertThat(actualAccountReport.getBooked()[0].getAmount().getCurrency())
            .isEqualTo(expectedBooked[0].getSpiAmount().getCurrency());

        assertThat(actualAccountReport.getBooked()[0].getExchangeRate().get(0).getCurrencyFrom())
            .isEqualTo(expectedBooked[0].getExchangeRate().get(0).getCurrencyFrom());
        assertThat(actualAccountReport.getBooked()[0].getExchangeRate().get(0).getRateFrom())
            .isEqualTo(expectedBooked[0].getExchangeRate().get(0).getRateFrom());
        assertThat(actualAccountReport.getBooked()[0].getExchangeRate().get(0).getCurrencyTo())
            .isEqualTo(expectedBooked[0].getExchangeRate().get(0).getCurrencyTo());
        assertThat(actualAccountReport.getBooked()[0].getExchangeRate().get(0).getRateTo())
            .isEqualTo(expectedBooked[0].getExchangeRate().get(0).getRateTo());
        assertThat(actualAccountReport.getBooked()[0].getExchangeRate().get(0).getRateDate())
            .isEqualTo(expectedBooked[0].getExchangeRate().get(0).getRateDate());
        assertThat(actualAccountReport.getBooked()[0].getExchangeRate().get(0).getRateContract())
            .isEqualTo(expectedBooked[0].getExchangeRate().get(0).getRateContract());

        assertThat(actualAccountReport.getBooked()[0].getCreditorName()).isEqualTo(expectedBooked[0].getCreditorName());
        assertAccountReferences(actualAccountReport.getBooked()[0].getCreditorAccount(),
            expectedBooked[0].getCreditorAccount());
        assertThat(actualAccountReport.getBooked()[0].getUltimateCreditor())
            .isEqualTo(expectedBooked[0].getUltimateCreditor());
        assertThat(actualAccountReport.getBooked()[0].getDebtorName()).isEqualTo(expectedBooked[0].getDebtorName());
        assertAccountReferences(actualAccountReport.getBooked()[0].getDebtorAccount(),
            expectedBooked[0].getDebtorAccount());
        assertThat(actualAccountReport.getBooked()[0].getUltimateDebtor())
            .isEqualTo(expectedBooked[0].getUltimateDebtor());
        assertThat(actualAccountReport.getBooked()[0].getRemittanceInformationStructured())
            .isEqualTo(expectedBooked[0].getRemittanceInformationStructured());
        assertThat(actualAccountReport.getBooked()[0].getRemittanceInformationUnstructured())
            .isEqualTo(expectedBooked[0].getRemittanceInformationUnstructured());
        assertThat(actualAccountReport.getBooked()[0].getPurposeCode().getCode())
            .isEqualTo(expectedBooked[0].getPurposeCode());
        assertThat(actualAccountReport.getBooked()[0].getBankTransactionCodeCode().getCode())
            .isEqualTo(expectedBooked[0].getBankTransactionCodeCode());
        assertThat(actualAccountReport.getBooked()[0].getProprietaryBankTransactionCode())
            .isEqualTo(expectedBooked[0].getProprietaryBankTransactionCode());
    }

    private void assertAccountReferences(Xs2aAccountReference xs2aAccountReference,
                                     SpiAccountReference spiAccountReference) {
        assertThat(xs2aAccountReference.getIban()).isEqualTo(spiAccountReference.getIban());
        assertThat(xs2aAccountReference.getBban()).isEqualTo(spiAccountReference.getBban());
        assertThat(xs2aAccountReference.getPan()).isEqualTo(spiAccountReference.getPan());
        assertThat(xs2aAccountReference.getMaskedPan()).isEqualTo(spiAccountReference.getMaskedPan());
        assertThat(xs2aAccountReference.getMsisdn()).isEqualTo(spiAccountReference.getMsisdn());
        assertThat(xs2aAccountReference.getCurrency()).isEqualTo(spiAccountReference.getCurrency());
    }
}
