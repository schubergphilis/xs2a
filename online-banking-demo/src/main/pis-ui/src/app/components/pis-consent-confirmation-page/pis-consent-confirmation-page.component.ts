import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, UrlSegment } from '@angular/router';
import { PisService } from '../../service/pis.service';
import { Banking } from '../../models/banking.model';
import { SinglePayment } from '../../models/singlePayment';
import { AccountConsent } from '../../models/accountConsent';
import ConsentStatusEnum = AccountConsent.ConsentStatusEnum;

@Component({
  selector: 'app-consent-confirmation-page',
  templateUrl: './pis-consent-confirmation-page.component.html'
})
export class PisConsentConfirmationPageComponent implements OnInit {
  singlePayments: SinglePayment;
  tan: string;
  paymentId: string;
  consentId: string;
  amount: number;

  constructor(private route: ActivatedRoute, private router: Router, private bankingService: PisService) {
  }


  ngOnInit() {
    this.route.url
      .subscribe(params => {
        this.getBankingDetailsFromUrl(params);
      });

    let bankingData = <Banking>({tan: this.tan, consentId: this.consentId, paymentId: this.paymentId});
    this.bankingService.saveData(bankingData);
    this.getSinglePayments();
  }

  getSinglePayments(){
    this.bankingService.getConsentById().subscribe(data => {
      console.log('get', data);
      this.singlePayments = data;
    });
  }

  getBankingDetailsFromUrl(params: UrlSegment[]) {
    this.consentId = params[1].toString();
    this.paymentId = atob(params[2].toString());
  }

  createQueryParams() {
    return {
      consentId: this.consentId,
      paymentId: this.paymentId,
    };
  }

  onClickContinue() {
    this.bankingService.updateConsentStatus(ConsentStatusEnum.RECEIVED)
      .subscribe(data=>{
        console.log('post 11', data);
      });
    this.bankingService.generateTan().subscribe();
    this.router.navigate(['pis/tanconfirmation'], {
      queryParams: this.createQueryParams()
    });
  }

  onClickCancel() {
    this.bankingService.updateConsentStatus(ConsentStatusEnum.REVOKEDBYPSU)
      .subscribe();
    this.router.navigate(['pis/consentconfirmationdenied']);
  }
}
