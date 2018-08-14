package de.adorsys.aspsp.xs2a.data.test;

import de.adorsys.aspsp.xs2a.consent.api.ConsentStatus;
import de.adorsys.aspsp.xs2a.consent.api.TypeAccess;
import de.adorsys.aspsp.xs2a.domain.AccountAccess;
import de.adorsys.aspsp.xs2a.domain.AisAccount;
import de.adorsys.aspsp.xs2a.domain.AisConsent;
import de.adorsys.aspsp.xs2a.repository.AisConsentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ConsentTestData {

    private AisConsentRepository aisConsentRepository;

    public ConsentTestData (AisConsentRepository aisConsentRepository) {
        this.aisConsentRepository = aisConsentRepository;
        fillConsents();
    }

    //create consents with different status for testing purposes
    private void fillConsents() {
        aisConsentRepository.save(getConsent("cucumber-c54a-3322-5555-939f26c20001", ConsentStatus.RECEIVED, 1, 1, LocalDate.parse("2020-11-10"), "d9e71419-24e4-4c5a-8d93-fcc23153aaff", "tpp01", false, getAccounts("DE52500105173911841934")));
        aisConsentRepository.save(getConsent("cucumber-c54a-3322-5555-939f26c20002", ConsentStatus.VALID, 1, 1, LocalDate.parse("2020-11-10"), "d9e71419-24e4-4c5a-8d93-fcc23153aaff", "tpp01", false, getAccounts("DE28500105174342597929")));
        aisConsentRepository.save(getConsent("cucumber-c54a-3322-5555-939f26c20003", ConsentStatus.REJECTED, 1, 1, LocalDate.parse("2020-11-10"), "d9e71419-24e4-4c5a-8d93-fcc23153aaff", "tpp01", false, getAccounts("DE75500105172377958695")));
        aisConsentRepository.save(getConsent("cucumber-c54a-3322-5555-939f26c20004", ConsentStatus.REVOKED_BY_PSU, 1,1, LocalDate.parse("2020-11-10"), "d9e71419-24e4-4c5a-8d93-fcc23153aaff", "tpp01", false, getAccounts("DE50500105177788564243")));
        aisConsentRepository.save(getConsent("cucumber-c54a-3322-5555-939f26c20005", ConsentStatus.TERMINATED_BY_TPP, 1, 1, LocalDate.parse("2020-11-10"), "d9e71419-24e4-4c5a-8d93-fcc23153aaff", "tpp01", false, getAccounts("DE55500105177514478625")));
        aisConsentRepository.save(getConsent("cucumber-c54a-3322-5555-939f26c20006", ConsentStatus.EXPIRED, 1, 1, LocalDate.parse("2017-11-10"), "d9e71419-24e4-4c5a-8d93-fcc23153aaff", "tpp01", false, getAccounts("DE48500105172747665854")));
    }

    private AisConsent getConsent(String consentId, ConsentStatus status, int expectedFrequency, int tppFrequency, LocalDate expire, String psuId, String tppId, boolean recurring, List<AisAccount> accounts) {
        AisConsent consent = new AisConsent();

        consent.setExternalId(consentId);
        consent.setConsentStatus(status);
        consent.setExpectedFrequencyPerDay(expectedFrequency);
        consent.setTppFrequencyPerDay(tppFrequency);
        consent.setUsageCounter(0);
        consent.setRequestDateTime(LocalDateTime.now());
        consent.setExpireDate(expire);
        consent.setPsuId(psuId);
        consent.setTppId(tppId);
        consent.addAccounts(accounts);
        consent.setRecurringIndicator(recurring);
        consent.setTppRedirectPreferred(true);
        consent.setCombinedServiceIndicator(false);
        return consent;
    }

    private List<AisAccount> getAccounts(String iban) {
        Set <AccountAccess> accesses = new HashSet<>(Arrays.asList( new AccountAccess(Currency.getInstance("EUR"), TypeAccess.BALANCE), new AccountAccess(Currency.getInstance("EUR"), TypeAccess.TRANSACTION)));
        return Arrays.asList(new AisAccount(iban, accesses));
    }
}
