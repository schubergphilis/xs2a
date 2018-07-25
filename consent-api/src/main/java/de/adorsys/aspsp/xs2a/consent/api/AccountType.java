package de.adorsys.aspsp.xs2a.consent.api;

public enum AccountType {
    IBAN, // This data element can be used for retrieving account access consent from this account.
    BBAN, // This data element is used for payment accounts which have no IBAN.
    PAN,  // Primary Account Number (PAN) of a card, can be tokenised by the ASPSP due to PCI DSS requirements.
    MASKEDPAN, // Primary Account Number (PAN) of a card in a masked form.
    MSISDN  // An alias to access a payment account via a registered mobile phone number.
}
