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

package de.adorsys.consent.consentmanagement.service;

import de.adorsys.consent.consentmanagement.domain.AisAccount;
import de.adorsys.consent.consentmanagement.domain.AisAccountAccess;
import de.adorsys.consent.consentmanagement.domain.AisConsent;
import de.adorsys.consent.consentmanagement.domain.TypeAccess;
import de.adorsys.consent.consentmanagement.repository.AisConsentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class AisConsentService {
    private AisConsentRepository aisConsentRepository;

    public AisConsent createConsent(){
        AisConsent aisConsent = new AisConsent();
        aisConsent.setAccounts(buildAccount());
        aisConsentRepository.save(aisConsent);
        return aisConsent;
    }

    private List<AisAccount> buildAccount() {
        AisAccount account = new AisAccount();
        account.setIban("sdfklj345jkljhklj345");
        account.setAccesses(buildAccountAccess());
        return Arrays.asList(account);
    }

    private List<AisAccountAccess> buildAccountAccess() {
        AisAccountAccess first = new AisAccountAccess();
        first.setTypeAccess(TypeAccess.ACCOUNT);

        AisAccountAccess second = new AisAccountAccess();
        second.setTypeAccess(TypeAccess.BALANCE);
        return Arrays.asList(first, second);
    }
}
