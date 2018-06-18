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

package de.adorsys.aspsp.xs2a.domain.code;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@ApiModel(description = "IBAN", value = "The IBAN associated to the account.")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IBAN {
    //todo change property type to newly created Iban object and make validation accordingly  https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/42
    @ApiModelProperty(value = "IBAN code", example = "IBAN")
    private String code;
}