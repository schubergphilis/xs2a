# PSD2 Validator

Implementation of different validator for the XS2A

## Certificate Validator
XS2A need to check and validate de Qseal Certificate, for that we need to add this project as dependency and execute as follow
```
CertificateValidatorFactory certValdFact = new CertificateValidatorFactory(blockedCertBucket, rootCertificates, intermediateCertificates)
certValdFact.validate(encodedCert)
```
- blockedCertBucket: bucket for maintaining blocked certificate by the ASPSP
- rootCertificates: bucket for maintaining root certificate of QTSP, 
- intermediateCertificates: bucket for maintaining intermediate certificate of QTSP, in case of certificate not directly signed by root CA

These buckets are type of SimpleCertificateBucket, which is a class to maintain a list of certificate. 


## Signature Validator
In some case, some ASPSP may need to verify signature of the incoming TPP request
```
TppSignatureValidator.verifySignature(signature, encodedCert, headers) -> true | false

```
signature is the signature included in the request
encodedCert is the certificate used to sign
headers is the map of headers request, the header variable name is the key and the contain is the value.

## Role Validator
XS2A must be able to check if each TPP incoming request should be processed 
that means on the other hand check if TPP has the correct role for the request.
```
RoleValidator.check(path)

```