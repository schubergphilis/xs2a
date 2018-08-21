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

package de.adorsys.aspsp.aspspmockserver.service;

import de.adorsys.aspsp.aspspmockserver.config.rest.consent.AisConsentRemoteUrls;
import de.adorsys.aspsp.aspspmockserver.repository.PsuRepository;
import de.adorsys.aspsp.aspspmockserver.repository.TanRepository;
import de.adorsys.aspsp.aspspmockserver.service.mapper.PaymentMapper;
import de.adorsys.aspsp.xs2a.consent.api.CmsAccountReference;
import de.adorsys.aspsp.xs2a.consent.api.ais.AisAccountConsent;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountDetails;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiConsentStatus;
import de.adorsys.aspsp.xs2a.spi.domain.psu.Tan;
import de.adorsys.aspsp.xs2a.spi.domain.psu.TanStatus;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.adorsys.aspsp.xs2a.spi.domain.consent.SpiConsentStatus.REJECTED;
import static de.adorsys.aspsp.xs2a.spi.domain.psu.TanStatus.UNUSED;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class ConsentConfirmationService {
    private final AccountService accountService;

    @Qualifier("consentRestTemplate")
    private final RestTemplate consentRestTemplate;
    private final AisConsentRemoteUrls aisConsentRemoteUrls;

    public List<SpiAccountDetails> getAccountDetailsListByConsentId(String consentId) {

        //TODO balances bla bla
        return getConsentByConsentId(consentId)
                   .map(this::getAccountsList)
                   .orElse(Collections.emptyList());
    }

    //TODO Create GlobalExceptionHandler for error 400 from consentManagement https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/158
    public void updateConsentStatus(@NotNull String consentId, SpiConsentStatus consentStatus) {
        consentRestTemplate.put(aisConsentRemoteUrls.updateAisConsentStatus(), null, consentId, consentStatus.name());
    }

    //TODO CODE STYLE
    public String getIbanByConsentId(@NotNull String consentId) {
        Optional<String> iban = getConsentByConsentId(consentId)
                          .map(acc -> acc.getAccess().getAccounts().stream()
                                          .findFirst()
                                            .map(CmsAccountReference::getIban)
                                            .orElse(null));

        return iban.get();
    }

    private List<SpiAccountDetails> getAccountsList(AisAccountConsent consent) {
        return consent.getAccess().getAccounts().stream()
                   .map(this::getSpiAccountDetailsByReference)
                   .collect(Collectors.toList());
    }

    private SpiAccountDetails getSpiAccountDetailsByReference(CmsAccountReference cms) {
        return accountService.getAccountsByIban(cms.getIban()).stream()
                   .filter(spiAccountDetails -> spiAccountDetails.getCurrency().equals(cms.getCurrency()))
                   .findFirst()
                   .orElse(null);
    }

    private Optional<AisAccountConsent> getConsentByConsentId(String consentId) {
        ResponseEntity<AisAccountConsent> response = consentRestTemplate.getForEntity(aisConsentRemoteUrls.getAisConsentById(), AisAccountConsent.class, consentId);
        return Optional.ofNullable(response.getBody());
    }
}
