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

package de.adorsys.aspsp.xs2a.web.aspect.header;

import de.adorsys.aspsp.xs2a.service.profile.AspspProfileServiceWrapper;
import de.adorsys.psd2.aspsp.profile.domain.ScaApproach;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
public abstract class HeaderController {
    private final AspspProfileServiceWrapper aspspProfileServiceWrapper;
    private final HttpHeaders headers = new HttpHeaders();

    protected HttpHeaders addHeader(String name, String value) {
        headers.add(name, value);
        return headers;
    }

    protected HttpHeaders addXRequestIdHeader(String xRequestId) {
        return addHeader("x-request-id", xRequestId);
    }

    protected HttpHeaders addAspspScaApproachHeader() {
        ScaApproach fixedApproach = aspspProfileServiceWrapper.getScaApproach();
        if (fixedApproach == ScaApproach.OAUTH) {
            fixedApproach = ScaApproach.REDIRECT;
        }
        return addHeader("aspsp-sca-approach", fixedApproach.name());
    }

    protected HttpHeaders addStartAuthorizarionHeaders(String xRequestId) {
        addXRequestIdHeader(xRequestId);
        return addAspspScaApproachHeader();

    }

}
