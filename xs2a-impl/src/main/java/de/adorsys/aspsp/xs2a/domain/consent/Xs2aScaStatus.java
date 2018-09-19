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

package de.adorsys.aspsp.xs2a.domain.consent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Xs2aScaStatus {
    RECEIVED("received"),
    PSUIDENTIFIED("psuIdentified"),
    PSUAUTHENTICATED("psuAuthenticated"),
    SCAMETHODSELECTED("scaMethodSelected"),
    STARTED("started"),
    FINALISED("finalised"),
    FAILED("failed"),
    EXEMPTED("exempted");

    private String value;

    Xs2aScaStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Xs2aScaStatus fromValue(String text) {
        for (Xs2aScaStatus b : Xs2aScaStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
