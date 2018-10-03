package de.adorsys.psd2.validator.certificate.util;

import lombok.Data;

import java.util.List;

@Data
public class TppCertificateData {
    private String pspAuthorisationNumber;
    private List<TppRole> pspRoles;
    private String name;
    private String pspAuthorityName;
    private String pspAuthorityId;
    private String country;
    private String organisation;
    private String organisationUnit;
    private String city;
    private String state;
}
