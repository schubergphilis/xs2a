# Roadmap

## version 1.8 (Planned date: 28.09.2018)
- Update bulk payments and consent requests according to specification 1.2
- Support of Embedded SCA approach for bulk payment.
- Support of Embedded SCA approach for periodic payment.
- Support of Embedded SCA approach for consent request.
- Validate Qwac certificate.
- Support get transaction information for a given account. Embedded approach.
- Migration to package and Maven GroupId "de.adorsys.psd2": aspsp-profile, aspsp-mockserver
- Fix the account consent information request to return link information.
- Add endpoint updateBLOB for PIS consent to Consent Management Service.

## version 1.9 (planned date: 12.10.2018)
- Confirmation of funds request. Add interface on side of SPI.
- Add validation of TPP data
- Update of Payment cancellation request according to specification 1.2
- Support of Signing baskets.
- Change logic of implicit/explicit method of authorisation
- Update BLOB field in the Consent. Support bytearray and base64 encoding.
- Validation of Consent (expiration date).
- Prototype online banking (PSU-ASPSP interface)
    - Payment initiation
    - Account information service. Bank offered.
    - Account information service. Dedicated accounts.
- Get list of reachable accounts. Embedded approach.
- Get balances for a given account. Embedded approach.
- Migration to package and Maven GroupId "de.adorsys.psd2": XS2A and Consent Management
- Remove empty links in the responses.

## version 1.10 (Planned date: 26.10.2018)
- Cancellation of (future/recurring) payments. Embedded approach.
- Get account details of the list of accessible accounts. Embedded approach.
- Get a confirmation on the availability of funds. Embedded approach.
- Support encryption of aspspConsentData.
- PIS Support a matrix payment-product/payment-type in aspsp-profile and corresponding services.


