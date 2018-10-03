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

package de.adorsys.aspsp.xs2a.service.authorisation.ais;

import de.adorsys.aspsp.xs2a.domain.MessageErrorCode;
import de.adorsys.aspsp.xs2a.domain.consent.*;
import de.adorsys.aspsp.xs2a.service.AisConsentDataService;
import de.adorsys.aspsp.xs2a.service.consent.AisConsentService;
import de.adorsys.aspsp.xs2a.service.mapper.consent.Xs2aAisConsentMapper;
import de.adorsys.aspsp.xs2a.spi.domain.SpiResponse;
import de.adorsys.aspsp.xs2a.spi.domain.authorisation.SpiAuthorisationStatus;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.domain.consent.SpiConsentStatus;
import de.adorsys.aspsp.xs2a.spi.domain.psu.SpiScaMethod;
import de.adorsys.aspsp.xs2a.spi.service.AccountSpi;
import de.adorsys.psd2.model.ScaStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static de.adorsys.aspsp.xs2a.domain.consent.ConsentAuthorisationResponseLinkType.*;

@Service
@RequiredArgsConstructor
public class EmbeddedAisAuthorisationService implements AisAuthorisationService {
    private final AccountSpi accountSpi;
    private final AisConsentService aisConsentService;
    private final Xs2aAisConsentMapper aisConsentMapper;
    private final AisConsentDataService aisConsentDataService;

    @Override
    public Optional<CreateConsentAuthorisationResponse> createConsentAuthorisation(String psuId, String consentId) {
        return createConsentAuthorisationAndGetResponse(ScaStatus.STARTED, START_AUTHORISATION_WITH_PSU_AUTHENTICATION, consentId, psuId);
    }

    @Override
    public AccountConsentAuthorisation getAccountConsentAuthorisationById(String authorisationId, String consentId) {
        return aisConsentService.getAccountConsentAuthorisationById(authorisationId, consentId);
    }

    // TODO cover with unit tests https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/334
    @Override
    public UpdateConsentPsuDataResponse updateConsentPsuData(UpdateConsentPsuDataReq updatePsuData, AccountConsentAuthorisation consentAuthorisation) {
        UpdateConsentPsuDataResponse response = createResponseFromUpdatePsuData(updatePsuData, consentAuthorisation);
        aisConsentService.updateConsentAuthorisation(aisConsentMapper.mapToSpiUpdateConsentPsuDataReq(response, updatePsuData));
        return response;
    }

    private UpdateConsentPsuDataResponse createResponseFromUpdatePsuData(UpdateConsentPsuDataReq request, AccountConsentAuthorisation consentAuthorisation) {
        UpdateConsentPsuDataResponse response = new UpdateConsentPsuDataResponse();
        AspspConsentData aspspConsentData = aisConsentDataService.getAspspConsentDataByConsentId(request.getConsentId());

        if (isPsuAuthenticationStage(request, consentAuthorisation)) {
            SpiResponse<SpiAuthorisationStatus> authorisationStatusSpiResponse = accountSpi.authorisePsu(request.getPsuId(), request.getPassword(), aspspConsentData);
            aisConsentDataService.updateAspspConsentData(authorisationStatusSpiResponse.getAspspConsentData());

            if (authorisationStatusSpiResponse.getPayload() == SpiAuthorisationStatus.FAILURE) {
                response.setScaStatus(Xs2aScaStatus.FAILED);
                return response;
            }

            response.setPsuId(request.getPsuId());
            response.setPassword(request.getPassword());

            SpiResponse<List<SpiScaMethod>> spiResponse = accountSpi.readAvailableScaMethods(request.getPsuId(), request.getPassword(), aisConsentDataService.getAspspConsentDataByConsentId(request.getConsentId()));
            aisConsentDataService.updateAspspConsentData(authorisationStatusSpiResponse.getAspspConsentData());
            proceedResponseForAvailableMethods(response, spiResponse.getPayload(), request.getConsentId());
            return response;
        }

        if (isScaMethodSelectionStage(request, consentAuthorisation)) {
            accountSpi.performStrongUserAuthorisation(request.getPsuId(), aspspConsentData);
            response.setChosenScaMethod(request.getAuthenticationMethodId());
            response.setScaStatus(Xs2aScaStatus.SCAMETHODSELECTED);
            response.setResponseLinkType(START_AUTHORISATION_WITH_TRANSACTION_AUTHORISATION);
            return response;
        }

        if (isAuthorisationStage(request)) {
            accountSpi.applyStrongUserAuthorisation(aisConsentMapper.mapToSpiAccountConfirmation(request), aspspConsentData);
            response.setScaAuthenticationData(request.getScaAuthenticationData());
            response.setScaStatus(Xs2aScaStatus.FINALISED);
            response.setResponseLinkType(START_AUTHORISATION_WITH_AUTHENTICATION_METHOD_SELECTION);
            aisConsentService.updateConsentStatus(request.getConsentId(), SpiConsentStatus.VALID);
            return response;
        }

        return response;
    }

    private void proceedResponseForAvailableMethods(UpdateConsentPsuDataResponse response, List<SpiScaMethod> availableMethods, String consentId) {
        if (CollectionUtils.isNotEmpty(availableMethods)) {
            if (isMultipleScaMethods(availableMethods)) {
                response.setAvailableScaMethods(aisConsentMapper.mapToCmsScaMethods(availableMethods));
                response.setScaStatus(Xs2aScaStatus.PSUAUTHENTICATED);
                response.setResponseLinkType(START_AUTHORISATION_WITH_AUTHENTICATION_METHOD_SELECTION);
            } else {
                accountSpi.performStrongUserAuthorisation(response.getPsuId(), aisConsentDataService.getAspspConsentDataByConsentId(consentId));
                response.setChosenScaMethod(availableMethods.get(0).name());
                response.setScaStatus(Xs2aScaStatus.SCAMETHODSELECTED);
                response.setResponseLinkType(START_AUTHORISATION_WITH_TRANSACTION_AUTHORISATION);
            }
        } else {
            response.setScaStatus(Xs2aScaStatus.FAILED);
            response.setErrorCode(MessageErrorCode.SCA_METHOD_UNKNOWN);
            aisConsentService.updateConsentStatus(consentId, SpiConsentStatus.REJECTED);
        }
    }

    private Optional<CreateConsentAuthorisationResponse> createConsentAuthorisationAndGetResponse(ScaStatus scaStatus, ConsentAuthorisationResponseLinkType linkType, String consentId, String psuId) {
        return aisConsentService.createAisConsentAuthorisation(consentId, Xs2aScaStatus.valueOf(scaStatus.name()), psuId)
                   .map(authId -> {
                       CreateConsentAuthorisationResponse resp = new CreateConsentAuthorisationResponse();

                       resp.setConsentId(consentId);
                       resp.setAuthorisationId(authId);
                       resp.setScaStatus(scaStatus);
                       resp.setResponseLinkType(linkType);

                       return resp;
                   });
    }

    private boolean isPsuAuthenticationStage(UpdateConsentPsuDataReq updatePsuData, AccountConsentAuthorisation spiAuthorisation) {
        return spiAuthorisation.getPassword() == null && updatePsuData.getPassword() != null;
    }

    private boolean isScaMethodSelectionStage(UpdateConsentPsuDataReq updatePsuData, AccountConsentAuthorisation spiAuthorisation) {
        return spiAuthorisation.getAuthenticationMethodId() == null && updatePsuData.getAuthenticationMethodId() != null;
    }

    private boolean isAuthorisationStage(UpdateConsentPsuDataReq updatePsuData) {
        return updatePsuData.getScaAuthenticationData() != null;
    }

    private boolean isMultipleScaMethods(List<SpiScaMethod> availableMethods) {
        return availableMethods.size() > 1;
    }
}
