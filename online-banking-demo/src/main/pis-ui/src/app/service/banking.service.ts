import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Banking } from '../models/banking.model';
import { SinglePayments } from '../models/models';
import { environment as env } from '../../environments/environment';
import { SinglePayment } from '../models/singlePayment';
import { CreateConsentRequestNew } from '../models/createConsentRequestNew';
import { PaymentsNew } from '../models/paymentsNew';


@Injectable({
  providedIn: 'root'
})
export class BankingService {
  savedData = new Banking();

  constructor(private httpClient: HttpClient) {
  }

  validateTan(tan: string): Observable<any> {
    const body = {
      tanNumber: tan,
      psuId: "aspsp",
      consentId: this.savedData.consentId,
      paymentId: this.savedData.paymentId
    };
    console.log('iio log1 ', this.savedData.consentId,this.savedData.paymentId, tan);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.put(env.mockServerUrl, body, { headers: headers });
  }

  setConsentStatus(decision: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    console.log('iio log2 ', this.savedData.consentId, decision);
    return this.httpClient.put(`${env.mockServerUrl}/${this.savedData.consentId}/${decision}`, {}, { headers: headers });
  }

  saveData(data) {
    this.savedData = data;
  }

  generateTan(): Observable<any> {
    return this.httpClient.post(env.mockServerUrl + '/aspsp', {});
  }

  getSinglePayments(): Observable<SinglePayment> {
    return this.httpClient.get<SinglePayment>(env.consentManagmentUrl + this.savedData.consentId);
  }

  createPaymentConsent():Observable<any> {
    return this.httpClient.post<any>(env.consentManagmentUrl, {});
  }

}
