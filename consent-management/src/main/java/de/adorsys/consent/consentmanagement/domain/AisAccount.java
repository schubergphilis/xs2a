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

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "ais_account")
public class AisAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false )
    protected Long id;

    @Column(name = "psu_id")
    private String psuId;
    private String iban;
    private String bban;
    private String pan;
    private String msisdn;

    @Column(name ="masked_pan")
    private String maskedPan;

    @ElementCollection
    @CollectionTable(name="ais_account_access", joinColumns=@JoinColumn(name="account_id"))
    private List<AisAccountAccess> accesses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consent_id")
    private AisConsent сonsent;
}
