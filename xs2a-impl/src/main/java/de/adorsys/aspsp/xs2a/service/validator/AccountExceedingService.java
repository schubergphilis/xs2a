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

package de.adorsys.aspsp.xs2a.service.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class AccountExceedingService {

    private final int accountAccessMaxFrequencyPerDay;
    private final HashMap<String, Integer> accessToAccountCounterMap;

    public boolean exceededAccessPerDay(String consentId) {
        Integer counter = accessToAccountCounterMap.get(consentId);
        int counterValue = counter == null ? 0 : counter;

        if (counterValue < accountAccessMaxFrequencyPerDay) {
            accessToAccountCounterMap.put(consentId, ++counterValue);
            return false;
        } else {
            return true;
        }
    }
}
