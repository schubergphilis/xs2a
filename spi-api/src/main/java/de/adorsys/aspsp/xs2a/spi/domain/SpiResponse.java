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

package de.adorsys.aspsp.xs2a.spi.domain;

import de.adorsys.aspsp.xs2a.spi.domain.consent.AspspConsentData;
import lombok.NonNull;
import lombok.Value;

import static de.adorsys.aspsp.xs2a.spi.domain.SpiResponseStatus.FAILED;
import static de.adorsys.aspsp.xs2a.spi.domain.SpiResponseStatus.SUCCESS;

@Value
public class SpiResponse<T> {
    /**
     * Business object that is returned in scope of request
     */
    private T payload;

    /**
     * Consent data - a binary data that is stored in a consent management system. Is not parsed by XS2A layer.
     * May be used by SPI layer to store state information linked to a workflow. May be encrypted in case of need.
     */
    private AspspConsentData aspspConsentData;

    /**
     * A status of execution result. Is used to provide correct answer to TPP.
     */
    @NonNull
    private SpiResponseStatus responseStatus;

    /**
     * An optional message that can be returned to explain response status in details.
     * XS2A Service may use it to provide the error explanation to TPP
     */
    private String message;

    public SpiResponse(T payload, AspspConsentData aspspConsentData, SpiResponseStatus responseStatus, String message) {
        this.payload = payload;
        this.aspspConsentData = aspspConsentData;
        this.responseStatus = responseStatus;
        this.message = message;
    }

    public SpiResponse(T payload, AspspConsentData aspspConsentData) {
        this(payload, aspspConsentData, SUCCESS, null);
    }

    public boolean hasError() {
        return responseStatus != SUCCESS;
    }

    public boolean isSuccessful() {
        return responseStatus == SUCCESS;
    }

    private SpiResponse(SpiResponseBuilder<T> builder) {
        this.payload = builder.payload;
        this.aspspConsentData = builder.aspspConsentData;
        this.responseStatus = builder.responseStatus;
        this.message = builder.message;
    }

    public static <T> SpiResponseBuilder<T> builder() {
        return new SpiResponseBuilder<>();
    }

    public static class SpiResponseBuilder<T> {
        private T payload;
        private AspspConsentData aspspConsentData;
        private SpiResponseStatus responseStatus = SUCCESS;
        private String message;

        public SpiResponseBuilder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public SpiResponseBuilder<T> aspspConsentData(AspspConsentData aspspConsentData) {
            this.aspspConsentData = aspspConsentData;
            return this;
        }

        public SpiResponseBuilder<T> responseStatus(SpiResponseStatus responseStatus) {
            this.responseStatus = responseStatus;
            return this;
        }

        public SpiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public SpiResponse<T> success() {
            return new SpiResponse<>(this.responseStatus(SUCCESS));
        }

        public SpiResponse<T> success(T payload, AspspConsentData aspspConsentData) {
            return this
                       .payload(payload)
                       .aspspConsentData(aspspConsentData)
                       .success();

        }

        public SpiResponse<T> success(AspspConsentData aspspConsentData) {
            return this
                       .aspspConsentData(aspspConsentData)
                       .success();
        }

        public SpiResponse<T> fail() {
            return new SpiResponse<>(this.responseStatus(FAILED));
        }

        public SpiResponse<T> fail(T payload, AspspConsentData aspspConsentData, String message) {
            this.payload = payload;
            this.aspspConsentData = aspspConsentData;
            this.responseStatus = FAILED;
            this.message = message;
            return this
                       .payload(payload)
                       .aspspConsentData(aspspConsentData)
                       .message(message)
                       .fail();
        }

        public SpiResponse<T> fail(T payload, AspspConsentData aspspConsentData) {
            return this.fail(payload, aspspConsentData, null);
        }

        public SpiResponse<T> fail(AspspConsentData aspspConsentData, String message) {
            return fail(null, aspspConsentData, message);

        }

        public SpiResponse<T> fail(AspspConsentData aspspConsentData) {
            return fail(null, null, null);
        }
    }
}
