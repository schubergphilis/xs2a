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

import de.adorsys.aspsp.xs2a.service.ConsentService;
import de.adorsys.psd2.model.AccountAccess;
import de.adorsys.psd2.model.AccountReferenceIban;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateConsentReqTest {

    private static final String IBAN = "IBAN ";

    @Test
    public void getAccountReferences_all() {
        //Given:
        AccountAccess access = getAccess(getRefs(1), getRefs(2), getRefs(3));
        //When:
        Set<Object> result = ConsentService.getAccountReferences(access);
        //Then:
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void getAccountReferences_1_null() {
        //Given:
        AccountAccess access = getAccess(null, getRefs(2), getRefs(3));
        //When:
        Set<Object> result = ConsentService.getAccountReferences(access);
        //Then:
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void getAccountReferences_all_null() {
        //Given:
        AccountAccess access = getAccess(null, null, null);
        //When:
        Set<Object> result = ConsentService.getAccountReferences(access);
        //Then:
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void getAccountReferences_access_null() {
        //When:
        Set<Object> result = ConsentService.getAccountReferences(null);
        //Then:
        assertThat(result.size()).isEqualTo(0);
    }

    private AccountAccess getAccess(List<Object> accounts, List<Object> balances, List<Object> transactions) {
        return new AccountAccess().accounts(accounts).balances(balances).transactions(transactions);
    }

    private List<Object> getRefs(int qty) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            AccountReferenceIban reference = new AccountReferenceIban();
            reference.setIban(IBAN + i);
            list.add(reference);
        }
        return list;
    }
}
