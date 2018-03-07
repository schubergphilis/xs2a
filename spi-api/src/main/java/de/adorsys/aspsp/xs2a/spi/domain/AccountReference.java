package de.adorsys.aspsp.xs2a.spi.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "AccountResponse Reference", value = "AccountReference")
@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class AccountReference {

	@ApiModelProperty(value = "IBAN: This data element can be used in the body of the AisInformationRequestBody Request Message for retrieving account access consent from this payment account", example = "1111111111")
	private String iban;

	@ApiModelProperty(value = "BBAN: This data elements is used for payment accounts which have no IBAN", example = "1111111111")
	private String bban;

	@ApiModelProperty(value = "PAN: Primary AccountResponse Number (PAN) of a card, can be tokenized by the ASPSP due to PCI DSS requirements.", example = "1111")
	private String pan;

	@ApiModelProperty(value = "MSISDN: An alias to access a payment account via a registered mobile phone number. This alias might be needed e.g. in the payment initiation service, cp. Section 5.3.1. The support of this alias must be explicitly documented by the ASPSP for the corresponding API calls.", example = "0172/1111111")
	private String msisdn;
}