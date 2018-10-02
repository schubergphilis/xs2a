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

package de.adorsys.aspsp.xs2a.service.authorization;

import de.adorsys.aspsp.xs2a.config.factory.ScaStage;
import de.adorsys.aspsp.xs2a.consent.api.CmsAspspConsentData;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorization.GetPisConsentAuthorizationResponse;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorization.UpdatePisConsentPsuDataRequest;
import de.adorsys.aspsp.xs2a.consent.api.pis.authorization.UpdatePisConsentPsuDataResponse;
import de.adorsys.aspsp.xs2a.service.PisConsentDataService;
import de.adorsys.aspsp.xs2a.service.authorization.pis.PisAuthorizationService;
import de.adorsys.aspsp.xs2a.service.mapper.consent.SpiCmsPisMapper;
import de.adorsys.aspsp.xs2a.spi.domain.SpiResponse;
import de.adorsys.aspsp.xs2a.spi.domain.authorization.SpiAuthorizationStatus;
import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import de.adorsys.aspsp.xs2a.spi.domain.psu.SpiScaMethod;
import de.adorsys.aspsp.xs2a.spi.service.PaymentSpi;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.adorsys.aspsp.xs2a.consent.api.CmsScaStatus.*;

@Service("STARTED")
public class ScaStartAuthorizationStage extends ScaStage<UpdatePisConsentPsuDataRequest, GetPisConsentAuthorizationResponse, UpdatePisConsentPsuDataResponse> {

    public ScaStartAuthorizationStage(PaymentSpi paymentSpi, PisAuthorizationService pisAuthorizationService, SpiCmsPisMapper spiCmsPisMapper, PisConsentDataService pisConsentDataService) {
        super(paymentSpi, pisAuthorizationService, spiCmsPisMapper, pisConsentDataService);
    }

    @Override
    public UpdatePisConsentPsuDataResponse apply(UpdatePisConsentPsuDataRequest request, GetPisConsentAuthorizationResponse pisConsentAuthorisationResponse) {

        AspspConsentData aspspConsentData = pisConsentDataService.getConsentDataByPaymentId(request.getPaymentId());
        SpiResponse<SpiAuthorizationStatus> authorisationStatusSpiResponse = paymentSpi.authorizePsu(request.getPsuId(),
                                                                                                     request.getPassword(),
                                                                                                     aspspConsentData
                                                                                                    );
        aspspConsentData = authorisationStatusSpiResponse.getAspspConsentData();
        pisConsentDataService.updateConsentData(aspspConsentData);

        if (SpiAuthorizationStatus.FAILURE == authorisationStatusSpiResponse.getPayload()) {
            return new UpdatePisConsentPsuDataResponse(FAILED);
        }
        request.setCmsAspspConsentData(new CmsAspspConsentData(aspspConsentData.getAspspConsentData()));
        SpiResponse<List<SpiScaMethod>> listAvailablescaMethodResponse = paymentSpi.readAvailableScaMethod(request.getPsuId(),
                                                                                            aspspConsentData
                                                                                           );
        aspspConsentData = listAvailablescaMethodResponse.getAspspConsentData();
        pisConsentDataService.updateConsentData(aspspConsentData);
        List<SpiScaMethod> spiScaMethods = listAvailablescaMethodResponse.getPayload();

        if (CollectionUtils.isEmpty(spiScaMethods)) {
            SpiResponse<String> executePaymentResponse = paymentSpi.executePayment(pisConsentAuthorisationResponse.getPaymentType(),
                                                                              pisConsentAuthorisationResponse.getPayments(),
                                                                              aspspConsentData
                                                                             );
            aspspConsentData = executePaymentResponse.getAspspConsentData();
            pisConsentDataService.updateConsentData(aspspConsentData);
            request.setScaStatus(FINALISED);
            return pisAuthorizationService.doUpdatePisConsentAuthorization(request);

        } else if (isSingleScaMethod(spiScaMethods)) {

            aspspConsentData = paymentSpi.performStrongUserAuthorization(request.getPsuId(),
                                                                         spiScaMethods.get(0),
                                                                         aspspConsentData
                                                                         )
                                   .getAspspConsentData();
            pisConsentDataService.updateConsentData(aspspConsentData);
            request.setScaStatus(SCAMETHODSELECTED);
            request.setAuthenticationMethodId(spiScaMethods.get(0).name());
            return pisAuthorizationService.doUpdatePisConsentAuthorization(request);

        } else if (isMultipleScaMethods(spiScaMethods)) {
            request.setScaStatus(PSUAUTHENTICATED);
            UpdatePisConsentPsuDataResponse response = pisAuthorizationService.doUpdatePisConsentAuthorization(request);
            response.setAvailableScaMethods(spiCmsPisMapper.mapToCmsScaMethods(spiScaMethods));
            return response;

        }
        return new UpdatePisConsentPsuDataResponse(FAILED);
    }

    private boolean isSingleScaMethod(List<SpiScaMethod> spiScaMethods) {
        return spiScaMethods.size() == 1;
    }

    private boolean isMultipleScaMethods(List<SpiScaMethod> spiScaMethods) {
        return spiScaMethods.size() > 1;
    }
}
