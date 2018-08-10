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

package de.adorsys.aspsp.xs2a;

import de.adorsys.aspsp.xs2a.consent.api.ConsentStatus;
import de.adorsys.aspsp.xs2a.consent.api.TypeAccess;
import de.adorsys.aspsp.xs2a.domain.AccountAccess;
import de.adorsys.aspsp.xs2a.domain.AisAccount;
import de.adorsys.aspsp.xs2a.domain.AisConsent;
import de.adorsys.aspsp.xs2a.repository.AisConsentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class consentTestData {

    @Component
    public class AISConsentServiceTestData {
        private final AisConsentRepository aisConsentRepository;

        public AISConsentServiceTestData(AisConsentRepository aisConsentRepository) {
            this.aisConsentRepository = aisConsentRepository;

            fillAccountAccess();
            fillAisConsentRequest();
        }

        /**
         * Create AIS consent test data
         */

        private void fillAisConsentRequest() {
            aisConsentRepository.save(getConsentForTestData("61f83da6-ed98-4420-afd1-1dfd6ee51b03", ConsentStatus.RECEIVED, 200, 50, 20, LocalDateTime.parse("2018-07-15T18:30:35.035"),
                LocalDate.parse("2018-05-01"), "ba8f7012-bdaf-4ada-bbf7-4c004d046ffe", "d9e71419-24e4-4c5a-8d93-fcc23153aaff", true, false, false));
/*

            aisConsentRepository.save(getConsentForTestData("16516ee51b02-ed98-4420-afe-d98-4420-", ConsentStatus.RECEIVED, 203, 1000, 30, LocalDateTime.parse("2018-07-15T18:30:35.099"),
                LocalDate.parse("2018-10-16"), "7d12ff85-8ace-4124-877a-6bc3f125e98b", "ba8f7012-bdaf-4ada-bbf7-4c004d046ffe", true, false, false));

            aisConsentRepository.save(getConsentForTestData("61f83d02-ed98-4420-afe16516eea6-51b0o", ConsentStatus.RECEIVED, 250, 500, 2032, LocalDateTime.parse("2018-07-15T18:30:35.034"),
                LocalDate.parse("2018-07-15"), "d9e71419-24e4-4c5a-8d93-fcc23153aaff", "42fb4cc3-91cb-45ba-9159-b87acf6d8add", true, false, false));
*/

        }

        public AisConsent getConsentForTestData(String consentId, ConsentStatus consentStatus, int expectedFrequencyPerDay, int tppFrequencyPerDay, int usageCounter, LocalDateTime requestedExecutionDate,
                                                LocalDate expireDate, String tppId, String psuId, boolean recurringIndicator, boolean tppRedirectPreferred, boolean combinedServiceIndicator) {

            AisConsent consent = new AisConsent();
            consent.setExternalId(consentId);
            consent.setConsentStatus(consentStatus);
            consent.setExpectedFrequencyPerDay(expectedFrequencyPerDay);
            consent.setTppFrequencyPerDay(tppFrequencyPerDay);
            consent.setUsageCounter(usageCounter);
            consent.setRequestDateTime(requestedExecutionDate);
            consent.setExpireDate(expireDate);
            consent.setPsuId(psuId);
            consent.setTppId(tppId);
            consent.addAccounts(addAisAccount());
            consent.setRecurringIndicator(recurringIndicator);
            consent.setTppRedirectPreferred(tppRedirectPreferred);
            consent.setCombinedServiceIndicator(combinedServiceIndicator);

            aisConsentRepository.save(consent);
            return consent;
        }

        private List<AisAccount> addAisAccount() {
            List<AisAccount> accounts = new LinkedList<>();
            accounts.add(getAisAccountsInfo("DE52500105173911841934", fillAccountAccess()));
/*
            accounts.add(getAisAccountsInfo("DE89370400440532013002", fillAccountAccess()));
            accounts.add(getAisAccountsInfo("DE89370400440532013005", fillAccountAccess()));
*/

            return accounts;
        }

        private Set<AccountAccess> fillAccountAccess() {
            Set<AccountAccess> accountAccesses = null;
            accountAccesses.add(new AccountAccess(Currency.getInstance("EUR"), TypeAccess.BALANCE));
            /*accountAccesses.add(new AccountAccess(EUR, ACCOUNT));
            accountAccesses.add(new AccountAccess(EUR, ACCOUNT));*/
            return accountAccesses;
        }

        private AisAccount getAisAccountsInfo(String iban, Set<AccountAccess> accountAccesses) {
            return new AisAccount(iban, accountAccesses);
        }

    }

}
