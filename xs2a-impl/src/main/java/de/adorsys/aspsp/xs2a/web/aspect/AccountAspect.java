/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.aspsp.xs2a.web.aspect;

import de.adorsys.aspsp.xs2a.web12.AccountController;
import de.adorsys.psd2.model.AccountDetails;
import de.adorsys.psd2.model.AccountReport;
import de.adorsys.psd2.model.LinksAccountDetails;
import de.adorsys.psd2.model.LinksAccountReport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class AccountAspect extends AbstractLinkAspect<AccountController> {

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController.readAccountDetails(..)) && args(consentId, accountId, withBalance, ..)", returning = "result")
    public ResponseEntity<AccountDetails> invokeReadAccountDetailsAspect(ResponseEntity<AccountDetails> result, String consentId, String accountId, boolean withBalance) {
        if (!hasError(result)) {
            AccountDetails body = result.getBody();
            body.setLinks(buildLinksForAccountDetails(body, withBalance));
        }
        return new ResponseEntity<>(result.getBody(), result.getHeaders(), result.getStatusCode());
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController.getAccounts(..)) && args(consentId, withBalance, ..)", returning = "result")
    public ResponseEntity<Map<String, List<AccountDetails>>> invokeGetAccountsAspect(ResponseEntity<Map<String, List<AccountDetails>>> result, String consentId, boolean withBalance) {
        if (!hasError(result)) {
            Map<String, List<AccountDetails>> body = result.getBody();
            setLinksToAccountsMap(body, withBalance);
        }
        return new ResponseEntity<>(result.getBody(), result.getHeaders(), result.getStatusCode());
    }

    @AfterReturning(pointcut = "execution(* de.adorsys.aspsp.xs2a.web12.AccountController.getTransactions(..)) && args(accountId,..)", returning = "result")
    public ResponseEntity<AccountReport> invokeGetTransactionsAspect(ResponseEntity<AccountReport> result, String accountId) {
        if (!hasError(result)) {
            AccountReport body = result.getBody();
            body.setLinks(buildLinksForAccountReport(body, accountId));
        }
        return new ResponseEntity<>(result.getBody(), result.getHeaders(), result.getStatusCode());
    }

    private LinksAccountDetails buildLinksForAccountDetails(AccountDetails accountDetails, boolean withBalance) {
        Class controller = getController();

        LinksAccountDetails links = new LinksAccountDetails();
        if (withBalance) {
            links.setBalances(linkTo(controller).slash(accountDetails.getResourceId()).slash("balances").toString());
        }
        links.setTransactions(linkTo(controller).slash(accountDetails.getResourceId()).slash("transactions").toString());

        return links;
    }

    private LinksAccountReport buildLinksForAccountReport(AccountReport accountReport, String accountId) {
        Class controller = getController();

        LinksAccountReport links = new LinksAccountReport();
        links.setAccount(linkTo(controller).slash(accountId).toString());

        return links;
    }

    private Map<String, List<AccountDetails>> setLinksToAccountsMap(Map<String, List<AccountDetails>> map, boolean withBalance) {
        map.entrySet().forEach(list -> updateAccountLinks(list.getValue(), withBalance));
        return map;
    }

    private List<AccountDetails> updateAccountLinks(List<AccountDetails> accountDetailsList, boolean withBalance) {
        return accountDetailsList.stream()
            .map(acc -> setLinksToAccount(acc, withBalance))
            .collect(Collectors.toList());
    }

    private AccountDetails setLinksToAccount(AccountDetails accountDetails, boolean withBalance) {
        accountDetails.setLinks(buildLinksForAccountDetails(accountDetails, withBalance));
        return accountDetails;
    }
}
