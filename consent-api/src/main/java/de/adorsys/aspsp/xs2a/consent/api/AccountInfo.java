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

package de.adorsys.aspsp.xs2a.consent.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import java.util.Currency;

@Value
@ApiModel(description = "Account information", value = "AccountInfo")
public class AccountInfo {

    @ApiModelProperty(value = "The account identifier which can be used on payload-level to address specific accounts", example = "DE2310010010156789")
    private String accountTypeId;

    @ApiModelProperty(value = "The type of account identifier", example = "IBAN")
    private AccountType accountType;

    @ApiModelProperty(value = "ISO 4217 currency code", example = "EUR")
    private Currency currency;
}
