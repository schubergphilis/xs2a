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

package de.adorsys.consent.consentmanagement.domain;

import lombok.Data;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "ais_consent")
public class AisConsent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false )
    protected Long id;

    @Column(name = "recurring_indicator")
    private boolean recurringIndicator;

    @Column(name = "request_date")
    private Date requestDate;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "transaction_status")
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "consent_status")
    @Enumerated(value = EnumType.STRING)
    private ConsentStatus consentStatus;

    private int frequencyPerDay;
    private boolean withBalance;
    private boolean tppRedirectPreferred;

    @OneToMany(mappedBy = "сonsent", orphanRemoval = true)
    private List<AisAccount> accounts = new ArrayList<>();

    public void addAccount(AisAccount account) {
        this.accounts.add(account);
        account.setСonsent(this);
    }
}
