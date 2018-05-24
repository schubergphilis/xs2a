package de.adorsys.psd2.validator.certificate.role;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import com.nimbusds.jose.util.X509CertUtils;

public class RoleExtractor {
	
	public List<String> getRolesFromCert(String encodedCert) {
		
		X509Certificate cert = X509CertUtils.parse(encodedCert);
		
		return null;
	}
	
public Map<String, String> getPsd2AttrFromCert(String encodedCert) {
		
		X509Certificate cert = X509CertUtils.parse(encodedCert);
		
		
		return null;
	}

}
