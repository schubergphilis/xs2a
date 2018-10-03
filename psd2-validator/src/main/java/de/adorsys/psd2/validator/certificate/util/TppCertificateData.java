package de.adorsys.psd2.validator.certificate.util;

import lombok.Data;

import java.util.List;

@Data
public class TppCertificateData {

	private String pspAuthorisationNumber;
	
	private List<TppRole> pspRoles;
	
	private String pspName;
	
	private String pspAuthorityName;
	
	private String pspAuthorityCountry;
	
	private String pspAuthorityId;
	
}
