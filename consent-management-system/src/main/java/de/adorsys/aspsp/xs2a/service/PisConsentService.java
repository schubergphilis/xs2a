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

package de.adorsys.aspsp.xs2a.service;

import de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus;
import de.adorsys.aspsp.xs2a.consent.api.CmsScaMethod;
import de.adorsys.aspsp.xs2a.consent.api.UpdateConsentAspspDataRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.PisConsentAspspDataResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.CreatePisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.GetPisConsentAuthorisationResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.UpdatePisConsentPsuDataRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorisation.UpdatePisConsentPsuDataResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.CreatePisConsentResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.PisConsentRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.proto.PisConsentResponse;
import de.adorsys.aspsp.xs2a.domain.pis.PisConsent;
import de.adorsys.aspsp.xs2a.domain.pis.PisConsentAuthorisation;
import de.adorsys.aspsp.xs2a.domain.pis.PisPaymentData;
import de.adorsys.aspsp.xs2a.repository.PisConsentAuthorisationRepository;
import de.adorsys.aspsp.xs2a.repository.PisConsentRepository;
import de.adorsys.aspsp.xs2a.repository.PisPaymentDataRepository;
import de.adorsys.aspsp.xs2a.service.mapper.PisConsentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

import static de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus.RECEIVED;
import static de.adorsys.aspsp.xs2a.consent.api.CmsConsentStatus.VALID;
import static de.adorsys.aspsp.xs2a.consent.api.CmsScaStatus.SCAMETHODSELECTED;
import static de.adorsys.aspsp.xs2a.consent.api.CmsScaStatus.STARTED;

@Service
@RequiredArgsConstructor
public class PisConsentService {
    private final PisConsentRepository pisConsentRepository;
    private final PisConsentMapper pisConsentMapper;
    private final PisConsentAuthorisationRepository pisConsentAuthorisationRepository;
    private final PisPaymentDataRepository pisPaymentDataRepository;

    /**
     * Creates new pis consent with full information about payment
     *
     * @param request Consists information about payments.
     * @return Response containing identifier of consent
     */
    public Optional<CreatePisConsentResponse> createPaymentConsent(PisConsentRequest request) {
        return pisConsentMapper.mapToPisConsent(request)
                   .map(pisConsentRepository::save)
                   .map(r -> new CreatePisConsentResponse(r.getExternalId()));
    }

    /**
     * Retrieves consent status from pis consent by consent identifier
     *
     * @param consentId String representation of pis consent identifier
     * @return Information about the status of a consent
     */
    public Optional<CmsConsentStatus> getConsentStatusById(String consentId) {
        return getPisConsentById(consentId)
                   .map(PisConsent::getConsentStatus);
    }

    /**
     * Reads full information of pis consent by consent identifier
     *
     * @param consentId String representation of pis consent identifier
     * @return Response containing full information about pis consent
     */
    public Optional<PisConsentResponse> getConsentById(String consentId) {
        return getPisConsentById(consentId)
                   .flatMap(pisConsentMapper::mapToPisConsentResponse);
    }

    /**
     * Updates pis consent status by consent identifier
     *
     * @param consentId String representation of pis consent identifier
     * @param status    new consent status
     * @return Response containing result of status changing
     */
    public Optional<Boolean> updateConsentStatusById(String consentId, CmsConsentStatus status) {
        return getActualPisConsent(consentId)
                   .map(con -> setStatusAndSaveConsent(con, status))
                   .map(con -> con.getConsentStatus() == status);
    }

    /**
     *
     * Get Pis aspsp consent data by consent id
     * @param consentId id of the consent
     * @return Response containing aspsp consent data
     */
    public Optional<PisConsentAspspDataResponse> getAspspConsentDataByConsentId(String consentId) {
        return getPisConsentById(consentId)
                   .map(this::prepareAspspConsentData);
    }

    /**
     * Get Pis aspsp consent data by payment id
     *
     * @param paymentId id of the payment
     * @return Response containing aspsp consent data
     */
    public Optional<PisConsentAspspDataResponse> getAspspConsentDataByPaymentId(String paymentId) {
        return pisPaymentDataRepository.findByPaymentId(paymentId)
                   .map(PisPaymentData::getConsent)
                   .map(this::prepareAspspConsentData);
    }

    private PisConsentAspspDataResponse prepareAspspConsentData(PisConsent consent) {
        PisConsentAspspDataResponse response = new PisConsentAspspDataResponse();
        response.setAspspConsentData(consent.getAspspConsentData());
        response.setConsentId(consent.getExternalId());
        return response;
    }

    /**
     * Update PIS consent aspsp consent data by id
     *
     * @param request   Aspsp provided pis consent data
     * @param consentId id of the consent to be updated
     * @return String consent id
     */
    @Transactional
    public Optional<String> updateAspspConsentData(String consentId, UpdateConsentAspspDataRequest request) {
        return getActualPisConsent(consentId)
                   .map(cons -> updateAspspConsentData(request, cons));
    }

    /**
     * Create consent authorisation
     *
     * @param paymentId
     * @return String authorisation id
     */
    @Transactional
    public Optional<CreatePisConsentAuthorisationResponse> createAuthorisation(String paymentId) {
        return pisPaymentDataRepository.findByPaymentIdAndConsent_ConsentStatus(paymentId, RECEIVED)
                   .map(pisConsent -> saveNewAuthorisation(pisConsent.getConsent()))
                   .map(c -> new CreatePisConsentAuthorisationResponse(c.getExternalId()));
    }

    public Optional<UpdatePisConsentPsuDataResponse> updateConsentAuthorisation(String authorisationId, UpdatePisConsentPsuDataRequest request) {
        Optional<PisConsentAuthorisation> pisConsentAuthorisationOptional = pisConsentAuthorisationRepository.findByExternalId(
            authorisationId);
        if (pisConsentAuthorisationOptional.isPresent()) {
            PisConsentAuthorisation consentAuthorisation = pisConsentAuthorisationOptional.get();

            byte[] bytes = Optional.ofNullable(request.getCmsAspspConsentData())
                               .map(CmsAspspConsentData::getBody)
                               .orElse(null);
            consentAuthorisation.getConsent().setAspspConsentData(bytes);

            if (SCAMETHODSELECTED == request.getScaStatus()) {
                String chosenMethod = request.getAuthenticationMethodId();
                if (StringUtils.isNotBlank(chosenMethod)) {
                    consentAuthorisation.setChosenScaMethod(CmsScaMethod.valueOf(chosenMethod));
                }
            }
            consentAuthorisation.setScaStatus(request.getScaStatus());
            pisConsentAuthorisationRepository.save(consentAuthorisation);
        }
        return pisConsentAuthorisationOptional.map(pisConsentMapper::mapToUpdatePisConsentPsuDataResponse);
    }

    public Optional<GetPisConsentAuthorisationResponse> getPisConsentAuthorisationById(String authorisationId) {
        return pisConsentAuthorisationRepository.findByExternalId(authorisationId)
                   .map(pisConsentMapper::mapToGetPisConsentAuthorisationResponse);
    }

    private Optional<PisConsent> getPisConsentById(String consentId) {
        return Optional.ofNullable(consentId)
                   .flatMap(pisConsentRepository::findByExternalId);
    }

    private PisConsent setStatusAndSaveConsent(PisConsent consent, CmsConsentStatus status) {
        consent.setConsentStatus(status);
        return pisConsentRepository.save(consent);
    }

    private Optional<PisConsent> getActualPisConsent(String consentId) {
        return Optional.ofNullable(consentId)
                   .flatMap(c -> pisConsentRepository.findByExternalIdAndConsentStatusIn(consentId, EnumSet.of(RECEIVED, VALID)));
    }

    /**
     * Creates PIS consent authorisation entity and stores it into database
     *
     * @param pisConsent PIS Consent, for which authorisation is performed
     * @return PisConsentAuthorisation
     */
    private PisConsentAuthorisation saveNewAuthorisation(PisConsent pisConsent) {
        PisConsentAuthorisation consentAuthorisation = new PisConsentAuthorisation();
        consentAuthorisation.setExternalId(UUID.randomUUID().toString());
        consentAuthorisation.setConsent(pisConsent);
        consentAuthorisation.setScaStatus(STARTED);
        return pisConsentAuthorisationRepository.save(consentAuthorisation);
    }

    private String updateAspspConsentData(UpdateConsentAspspDataRequest request, PisConsent consent) {
        consent.setAspspConsentData(request.getAspspConsentData());
        PisConsent savedConsent = pisConsentRepository.save(consent);
        return savedConsent.getExternalId();
    }
}
