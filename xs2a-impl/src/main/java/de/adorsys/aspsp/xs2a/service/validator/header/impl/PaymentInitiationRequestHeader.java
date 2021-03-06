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

package de.adorsys.aspsp.xs2a.service.validator.header.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.adorsys.aspsp.xs2a.domain.ContentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Payment initiation request header", value = "PaymentInitiationRequestHeader")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInitiationRequestHeader extends CommonRequestHeader {

    @ApiModelProperty(value = "Content type", example = "application/json")
    @JsonProperty(value = "content-type")
    private ContentType contentType;

    @ApiModelProperty(value = "Might be mandated in the ASPSP's documentation, if OAuth is not chosen as Pre-Step", required = false, example = "PSU-1234")
    @JsonProperty(value = "psu-id")
    private String psuId;

    @ApiModelProperty(value = "Type of the PSU-ID, needed in scenarios where PSUs have several PSU-IDs as access possibility.", required = false, example = "type")
    @JsonProperty(value = "psu-id-type")
    private String psuIdType;

    @ApiModelProperty(value = "Contained if not yet contained in the first request, and mandated by the ASPSP in the related response. This field is relevant only in a corporate context", required = false, example = "6558e7e6-f72f-407a-9f22-763ad0921915")
    @JsonProperty(value = "psu-corporate-id")
    private String psuCorporateId;

    @ApiModelProperty(value = "Might be mandated by the ASPSP in addition if the PSU-Corporate-ID is contained", required = false, example = "type")
    @JsonProperty(value = "psu-corporate-id-type")
    private String psuCorporateIdType;

    @ApiModelProperty(value = "This data element may be contained, if the payment initiation transaction is part of a session, i.e. combined AIS/PIS service. This then contains the consentId of the related AIS consent, which was performed prior to this payment initiation.", required = false, example = "91306384-e37a-4536-a51e-1ced42e37a5c")
    @JsonProperty(value = "psu-consent-id")
    private String psuConsentId;

    @ApiModelProperty(value = "The forwarded Agent header field of the http request between PSU and TPP", required = false, example = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0)")
    @JsonProperty(value = "psu-agent")
    private String psuAgent;

    @ApiModelProperty(value = "The forwarded IP Address header field consists of the corresponding http request IP Address field between PSU and TPP", required = true, example = "192.168.8.78 example")
    @JsonProperty(value = "psu-ip-address")
//  @NotNull TODO Add check whether psu-ip-address is present in payment initiation request header https://git.adorsys.de/adorsys/xs2a/aspsp-xs2a/issues/341
    private String psuIpAddress;

    @ApiModelProperty(value = "The forwarded IP Address header field consists of the corresponding http request IP Address field between PSU and TPP", required = false, example = "GEO:52.506931,13.144558")
    @JsonProperty(value = "psu-geo-location")
    private String psuGeoLocation;

    @ApiModelProperty(value = "URI of the TPP, where the transaction flow shall be redirected to after a Redirect", required = false, example = "https://www.example.com/authentication/b51de9d9-2baa-4012-9447-e3f57f1f363c")
    @JsonProperty(value = "tpp-redirect-uri")
    private String tppRedirectUri;
}
