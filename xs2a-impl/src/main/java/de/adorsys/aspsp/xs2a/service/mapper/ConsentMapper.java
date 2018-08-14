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

import de.adorsys.aspsp.xs2a.consent.api.AccountInfo;
import de.adorsys.aspsp.xs2a.consent.api.ActionStatus;
import de.adorsys.aspsp.xs2a.consent.api.TypeAccess;
import de.adorsys.aspsp.xs2a.consent.api.ais.AisAccountAccessInfo;
import de.adorsys.aspsp.xs2a.consent.api.ais.AisConsentRequest;
import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.spi.domain.account.SpiAccountConsent;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiAccountAccess;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiAccountAccessType;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiConsentStatus;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiCreateConsentRequest;
import de.adorsys.psd2.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConsentMapper {
    private final AccountMapper accountMapper;

    public AisConsentRequest mapToAisConsentRequest(Consents req, String psuId, String tppId) {
        return Optional.ofNullable(req)
            .map(r -> {
                AisConsentRequest request = new AisConsentRequest();
                request.setPsuId(psuId);
                request.setTppId(tppId);
                request.setFrequencyPerDay(r.getFrequencyPerDay());
                request.setAccess(mapToAisAccountAccessInfo(req.getAccess()));
                request.setValidUntil(r.getValidUntil());
                request.setRecurringIndicator(r.getRecurringIndicator());
                request.setCombinedServiceIndicator(r.isCombinedServiceIndicator());

                return request;
            })
            .orElse(null);
    }

    public SpiCreateConsentRequest mapToSpiCreateConsentRequest(Consents consentReq) {
        return Optional.ofNullable(consentReq)
            .map(cr -> new SpiCreateConsentRequest(mapToSpiAccountAccess(cr.getAccess()),
                cr.getRecurringIndicator(), cr.getValidUntil(),
                cr.getFrequencyPerDay(), cr.isCombinedServiceIndicator()))
            .orElse(null);
    }

    public ConsentInformationResponse200Json mapToAccountConsent(SpiAccountConsent spiAccountConsent) {
        return Optional.ofNullable(spiAccountConsent)
            .map(ac -> new ConsentInformationResponse200Json()
                .recurringIndicator(ac.isRecurringIndicator())
                .frequencyPerDay(ac.getFrequencyPerDay())
                .lastActionDate(ac.getLastActionDate())
                .validUntil(ac.getValidUntil())
                .consentStatus(ConsentStatus.valueOf(ac.getSpiConsentStatus().name()))
                .access(mapToAccountAccess(ac.getAccess()))
            )
            .orElse(null);
    }

    public Optional<ConsentStatus> mapToConsentStatus(SpiConsentStatus spiConsentStatus) {
        return Optional.ofNullable(spiConsentStatus)
            .map(status -> ConsentStatus.valueOf(status.name()));
    }

    public ActionStatus mapActionStatusError(MessageErrorCode error, boolean withBalance, TypeAccess access) {
        ActionStatus actionStatus = ActionStatus.FAILURE_ACCOUNT;
        if (error == MessageErrorCode.ACCESS_EXCEEDED) {
            actionStatus = ActionStatus.CONSENT_LIMIT_EXCEEDED;
        } else if (error == MessageErrorCode.CONSENT_EXPIRED) {
            actionStatus = ActionStatus.CONSENT_INVALID_STATUS;
        } else if (error == MessageErrorCode.CONSENT_UNKNOWN_400) {
            actionStatus = ActionStatus.CONSENT_NOT_FOUND;
        } else if (error == MessageErrorCode.CONSENT_INVALID) {
            if (access == TypeAccess.TRANSACTION) {
                actionStatus = ActionStatus.FAILURE_TRANSACTION;
            } else if (access == TypeAccess.BALANCE || withBalance) {
                actionStatus = ActionStatus.FAILURE_BALANCE;
            }
        }
        return actionStatus;
    }

    //Domain
    private AccountAccess mapToAccountAccess(SpiAccountAccess access) {
        return Optional.ofNullable(access)
            .map(aa ->
                new AccountAccess()
                    .accounts(accountMapper.mapToAccountReferences(aa.getAccounts()))
                    .balances(accountMapper.mapToAccountReferences(aa.getBalances()))
                    .transactions(accountMapper.mapToAccountReferences(aa.getTransactions()))
                    .availableAccounts(mapToAvailableAccountsEnum(aa.getAvailableAccounts()))
                    .allPsd2(mapToAllPsd2Enum(aa.getAllPsd2()))
            )
            .orElse(null);
    }

    private AccountAccess.AvailableAccountsEnum mapToAvailableAccountsEnum(SpiAccountAccessType accessType) {
        return Optional.ofNullable(accessType)
            .map(at -> AccountAccess.AvailableAccountsEnum.valueOf(at.name()))
            .orElse(null);
    }

    private AccountAccess.AllPsd2Enum mapToAllPsd2Enum(SpiAccountAccessType accessType) {
        return Optional.ofNullable(accessType)
            .map(at -> AccountAccess.AllPsd2Enum.valueOf(at.name()))
            .orElse(null);
    }

    //Spi
    private SpiAccountAccess mapToSpiAccountAccess(AccountAccess access) {
        return Optional.ofNullable(access)
            .map(aa -> {
                SpiAccountAccess spiAccountAccess = new SpiAccountAccess();
                spiAccountAccess.setAccounts(accountMapper.mapToSpiAccountReferences(aa.getAccounts()));
                spiAccountAccess.setBalances(accountMapper.mapToSpiAccountReferences(aa.getBalances()));
                spiAccountAccess.setTransactions(accountMapper.mapToSpiAccountReferences(aa.getTransactions()));
                spiAccountAccess.setAvailableAccounts(mapToSpiAccountAccessType(aa.getAvailableAccounts()));
                spiAccountAccess.setAllPsd2(mapToSpiAccountAccessType(aa.getAllPsd2()));
                return spiAccountAccess;
            })
            .orElse(null);
    }

    private SpiAccountAccessType mapToSpiAccountAccessType(Enum accessType) {
        return Optional.ofNullable(accessType)
            .map(at -> SpiAccountAccessType.valueOf(at.name()))
            .orElse(null);

    }

    private AisAccountAccessInfo mapToAisAccountAccessInfo(AccountAccess access) {
        AisAccountAccessInfo accessInfo = new AisAccountAccessInfo();
        accessInfo.setAccounts(Optional.ofNullable(access.getAccounts())
            .map(this::mapToListAccountInfo)
            .orElse(Collections.emptyList()));

        accessInfo.setBalances(Optional.ofNullable(access.getBalances())
            .map(this::mapToListAccountInfo)
            .orElse(Collections.emptyList()));

        accessInfo.setTransactions(Optional.ofNullable(access.getTransactions())
            .map(this::mapToListAccountInfo)
            .orElse(Collections.emptyList()));

        accessInfo.setAvailableAccounts(Optional.ofNullable(access.getAvailableAccounts())
            .map(AccountAccess.AvailableAccountsEnum::name)
            .orElse(null));
        accessInfo.setAllPsd2(Optional.ofNullable(access.getAllPsd2())
            .map(AccountAccess.AllPsd2Enum::name)
            .orElse(null));

        return accessInfo;
    }

    private List<AccountInfo> mapToListAccountInfo(List<Object> refs) {
        return refs.stream()
            .map(this::mapToAccountInfo)
            .collect(Collectors.toList());
    }

    private AccountInfo mapToAccountInfo(Object ref) {
        if (!(ref instanceof AccountReferenceIban)) {
            return null;
        }

        AccountInfo info = new AccountInfo();
        info.setIban(((AccountReferenceIban) ref).getIban());
        info.setCurrency(((AccountReferenceIban) ref).getCurrency());
        return info;
    }
}
