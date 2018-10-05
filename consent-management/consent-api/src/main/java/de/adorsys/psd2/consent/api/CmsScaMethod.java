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

package de.adorsys.psd2.consent.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum CmsScaMethod {
    SMS_OTP("SMS_OTP"),
    CHIP_OTP("CHIP_OTP"),
    PHOTO_OTP("PHOTO_OTP"),
    PUSH_OTP("PUSH_OTP");

    private String authenticationMethodId;
    private final static Map<String, CmsScaMethod> container = new HashMap<>();


    @JsonCreator
    CmsScaMethod(String authenticationMethodId) {
        this.authenticationMethodId = authenticationMethodId;
    }

    static {
        for (CmsScaMethod type : values()) {
            container.put(type.getAuthenticationMethodId(), type);
        }
    }

    public String getAuthenticationMethodId() {
        return authenticationMethodId;
    }

    @JsonIgnore
    public static Optional<CmsScaMethod> getByAuthenticationMethodId(String name) {
        return Optional.ofNullable(container.get(name));
    }
}
