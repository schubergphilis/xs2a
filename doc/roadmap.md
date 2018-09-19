# Roadmap

## Releases

### version 1.7 (release date: 14.09.2018)

- Implementation of specification 1.2 according to the yml file from Berlin Group.
- Support of Embedded SCA approach for single payment:
    - 	Embedded SCA Approach without SCA method
    - Embedded SCA Approach with only one SCA method available 
    -  Embedded SCA Approach with Selection of an SCA method 
- Implementation of Start Authorization Process for Embedded SCA approach
- Add new endpoint to CMS to enable ASPSP to modify Account Access at AIS Consent providing consent Id.
- Consent request Type is added to the Consent
- SCA method for PSU on ASPSP-Mock is added

## Plan

### version 1.8 (planned date: 28.09.2018)

- Update bulk payments and consent requests according to specification 1.2
- Support of Embedded SCA approach for bulk payment.
- Support of Embedded SCA approach for periodic payment.
- Support of Embedded SCA approach for consent request.
- Move keycloak from xs2a module to spi-mock.
- Add Qwac certificate filter.
- Support get transaction information for a given account. Embedded approach.


### version 1.9 (planned date: 12.10.2018)

- Add validation of TPP data
- Update of Payment cancellation request according to specification 1.2
- Support of Signing baskets.
- Change logic of implicit/explicit method of authorisation
- Update BLOB field in the Consent. Support bytearray and base64 encoding.
- Validation of Consent (expiration date).
- Prototype online banking (PSU-ASPSP interface)
    -	Payment initiation
    -	Account information service. Bank offered.
    -	Account information service. Dedicated accounts.
- Get list of reachable accounts. Embedded approach.
- Get balances for a given account. Embedded approach.

### version 1.10 (Planned date: 26.10.2018)

-	Cancellation of (future/recurring) payments. Embedded approach.
-	Get account details of the list of accessible accounts. Embedded approach.
-	Get a confirmation on the availability of funds. Embedded approach.
-	Confirmation of funds request. Add interface on side of SPI.

### version 1.11 (Planned date: 09.11.2018)
-	Consent access Validator
-	ASPSP-Mock. Develop Certificate black-list
-	Support of multicurrency account
-	Multitier support

### version 1.12 (Planned date: 23.11.2018)

- Bug fixing. Empty links in the responses.
- ASPSP-Mock. Download link support.
- Bug fixing. Embedded, redirect, oAuth 2.0

### version 1.13 (Planned date 07.12.2018)
-	Decoupled SCA approach support
-	Support of Berlin Group XS2A Specification 1.3

### version 1.14	(Planned date 21.12.2018)

-	Support XML based endpoints
-	ASPSP-Mock. Support of 4 types of OTP.

