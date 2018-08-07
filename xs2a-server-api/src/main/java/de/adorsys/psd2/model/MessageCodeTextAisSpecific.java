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

package de.adorsys.psd2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets messageCodeTextAisSpecific
 */
public enum MessageCodeTextAisSpecific {
    CONSENT_INVALID("CONSENT_INVALID"), SESSIONS_NOT_SUPPORTED("SESSIONS_NOT_SUPPORTED"), ACCESS_EXCEEDED(
        "ACCESS_EXCEEDED"), REQUESTED_FORMATS_INVALID("REQUESTED_FORMATS_INVALID");

    private String value;

    MessageCodeTextAisSpecific(String value) {
        this.value = value;
    }

    @JsonCreator
    public static MessageCodeTextAisSpecific fromValue(String text) {
        for (MessageCodeTextAisSpecific b : MessageCodeTextAisSpecific.values()) {
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
