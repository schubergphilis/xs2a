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

import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountReference;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiAccountAccess;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiAccountAccessType;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiCreateConsentRequest;
import de.adorsys.consent.consentmanagement.domain.AisAccount;
import de.adorsys.consent.consentmanagement.domain.AisConsent;
import de.adorsys.consent.consentmanagement.domain.AisConsentStatus;
import de.adorsys.consent.consentmanagement.domain.TypeAccess;
import de.adorsys.consent.consentmanagement.repository.AisConsentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class AisConsentService {
    private final AisConsentRepository aisConsentRepository;

    public Optional<String> createConsent(SpiCreateConsentRequest request, String psuId, String tppId, boolean withBalance){
        AisConsent consent = new AisConsent();
        consent.setExternalId(UUID.randomUUID().toString());
        consent.setConsentStatus(AisConsentStatus.RECEIVED);
        consent.setFrequencyPerDay(request.getFrequencyPerDay());
        consent.setUsageCounter(request.getFrequencyPerDay());
        consent.setRequestDate(LocalDateTime.now());
        consent.setExpireDate(LocalDateTime.now().plusDays(1));
        consent.setPsuId(psuId);
        consent.setTppId(tppId);

        AccountInfoDetail info = buildAccountInfoDetail(request.getAccess());

        consent.addAccounts(buildAccounts(info));
        aisConsentRepository.save(consent);
        return Optional.of(consent.getExternalId());
    }

    private AccountInfoDetail buildAccountInfoDetail(SpiAccountAccess access) {
        List<SpiAccountReference> accounts = access.getAccounts();
        List<SpiAccountReference> balances = access.getBalances();
        List<SpiAccountReference> transactions = access.getTransactions();

        // TODO check
        SpiAccountAccessType allPsd2 = access.getAllPsd2();
        SpiAccountAccessType availableAccounts = access.getAvailableAccounts();

        AccountInfoDetail info = new AccountInfoDetail();
        fillAccountInfoDetail(accounts, info, TypeAccess.ACCOUNT);
        fillAccountInfoDetail(balances, info, TypeAccess.BALANCE);
        fillAccountInfoDetail(transactions, info, TypeAccess.TRANSACTION);
        return info;
    }

    private void fillAccountInfoDetail(List<SpiAccountReference> references, AccountInfoDetail info, TypeAccess typeAccess) {
        references.forEach(a -> info.addIbanAccess(a.getIban(), a.getCurrency(), typeAccess));
    }

    private List<AisAccount> buildAccounts(AccountInfoDetail info) {
        return info.getIbansAccess()
            .entrySet()
            .stream()
            .map(e -> buildAccount(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    private AisAccount buildAccount(String iban, AccountInfoDetail.InfoDetail info) {
        AisAccount account = new AisAccount(iban);
        account.addAccesses(info.getAccesses());
        account.addCurrencies(info.getCurrencies());
        return account;
    }

    @Value
    @Getter
    class AccountInfoDetail {
        private Map<String, InfoDetail> ibansAccess = new HashMap<>();

        public void addIbanAccess(String iban, Currency currency, TypeAccess typeAccess){
            InfoDetail detail = ibansAccess.get(iban);
            if(detail == null){
                detail = new InfoDetail();
                ibansAccess.put(iban, detail);
            }
            detail.addAccess(typeAccess);
            detail.addCurrency(currency);
        }

        @Value
        @Getter
        class InfoDetail {
            private Set<TypeAccess> accesses = new HashSet<>();
            private Set<Currency> currencies = new HashSet<>();

            public void addAccess(TypeAccess typeAccess){
                accesses.add(typeAccess);
                if(EnumSet.of(TypeAccess.BALANCE, TypeAccess.TRANSACTION).contains(typeAccess)){
                    accesses.add(TypeAccess.ACCOUNT);
                }
            }

            public void addCurrency(Currency currency){
                ofNullable(currency).ifPresent(c -> currencies.add(c));
            }
        }
    }
}
