import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Banking } from '../models/banking.model';
import { environment as env } from '../../environments/environment';
import { SinglePayment } from '../models/singlePayment';

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
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.put(`${env.mockServerUrl}`, body, { headers: headers });
  }

  updateConsentStatus(decision: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.put(`${env.mockServerUrl}/${this.savedData.consentId}/${decision}`, {}, { headers: headers });
  }

  saveData(data) {
    this.savedData = data;
  }

  generateTan(): Observable<any> {
    return this.httpClient.post(`${env.mockServerUrl}` + '/aspsp', {});
  }

  getConsentById(): Observable<SinglePayment> {
    return this.httpClient.get<SinglePayment>(`${env.consentManagmentUrl}` + this.savedData.consentId);
  }
}
