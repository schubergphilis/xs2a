package de.adorsys.aspsp.xs2a.domain;

import de.adorsys.psd2.custom.AccountReference;

import java.util.Set;

public interface AccountReferenceCollector {
    Set<AccountReference> getAccountReferences();
}
