import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Banking } from '../models/banking.model';
import { SinglePayments } from '../models/models';
import { environment as env } from '../../environments/environment';
import { SinglePayment } from '../models/singlePayment';
import { CreateConsentRequest } from '../models/createConsentRequest';
import { Payments } from '../models/payments';


@Injectable({
  providedIn: 'root'
})
export class BankingService {
  savedData = new Banking();
  GENERATE_TAN_URL = `${env.mockServerUrl}/consent/confirmation/pis/aspsp`;
  SET_CONSENT_STATUS_URL = `${env.mockServerUrl}/consent/confirmation/pis`;
  test : SinglePayment;
  pay: Payments;

  constructor(private httpClient: HttpClient) {
    // Payments

    this.test = {
      "aspspConsentData": "zzzzzzzz",
      "paymentProduct": "sepa-credit-transfers",
      "paymentType": "SINGLE",
      "payments":  [{
        "amount": 1000,
        "creditorAccount": {
          "bban": 89370400440532010000,
          "currency": "EUR",
          "iban": "DE89370400440532013000",
          "maskedPan": "2356xxxxxx1234",
          "msisdn": "+49(0)911 360698-0",
          "pan": "2356 5746 3217 1234"
        },
        "creditorAddress": {
          "buildingNumber": "123-34",
          "city": "Nürnberg",
          "country": "Germany",
          "postalCode": 90431,
          "street": "Herrnstraße"
        },
        "creditorAgent": "Telekom",
        "creditorName": "Telekom",
        "currency": "EUR",
        "dayOfExecution": 14,
        "debtorAccount": {
          "bban": 89370400440532010000,
          "currency": "EUR",
          "iban": "DE89370400440532013000",
          "maskedPan": "2356xxxxxx1234",
          "msisdn": "+49(0)911 360698-0",
          "pan": "2356 5746 3217 1234"
        },
        "endDate": "2020-03-03",
        "endToEndIdentification": "RI-123456789",
        "executionId": 32454656712432,
        "executionRule": "latest",
        "frequency": "ANNUAL",
        "paymentId": 32454656712432,
        "purposeCode": "BCENECEQ",
        "remittanceInformationStructured": {
          "reference": "Ref Number Merchant",
          "referenceIssuer": "reference issuer",
          "referenceType": "reference type"
        },
        "remittanceInformationUnstructured": "Ref. Number TELEKOM-1222",
        "requestedExecutionDate": "2020-01-01",
        "requestedExecutionTime": "2020-01-01T15:30:35.035Z",
        "startDate": "2020-01-01",
        "ultimateCreditor": "Telekom",
        "ultimateDebtor": "Mueller"
      }],
      "tppInfo": {
        "nationalCompetentAuthority": "National competent authority",
        "nokRedirectUri": "Nok redirect URI",
        "redirectUri": "Redirect URI",
        "registrationNumber": "1234_registrationNumber",
        "tppName": "Tpp company",
        "tppRole": "Tpp role"
      }
    };
  }

  validateTan(tan: string): Observable<any> {
    const body = {
      tanNumber: tan,
      consentId: this.savedData.consentId,
      paymentId: this.savedData.paymentId
    };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.put(this.TAN_URL, body, { headers: headers });
  }

  setConsentStatus(decision: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.put(`${this.SET_CONSENT_STATUS_URL}/${this.savedData.consentId}/${decision}`, {}, { headers: headers });
  }

  saveData(data) {
    this.savedData = data;
  }

  generateTan(): Observable<any> {
    return this.httpClient.post(this.GENERATE_TAN_URL, {});
  }

  getSinglePayments(): Observable<SinglePayment> {
    return this.httpClient.get<SinglePayment>(env.consentManagmentUrl + this.savedData.consentId);
  }

  createPaymentConsent():Observable<any> {
    return this.httpClient.post<any>(env.consentManagmentUrl, this.test);
  }

}
