package de.adorsys.aspsp.xs2a.domain;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Supported AccountReference Fileds", value = "SupportedAccountReferenceFields")
public enum SupportedAccountReferenceFields {
    IBAN,
    BBAN,
    PAN,
    MASKEDPAN,
    MSISDN
}
