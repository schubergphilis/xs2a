import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { PisService } from '../../service/pis.service';
import { Banking } from '../../models/banking.model';
import { AccountConsent } from '../../models/accountConsent';
import ConsentStatusEnum = AccountConsent.ConsentStatusEnum;

@Component({
  selector: 'app-tan-confirmation-page',
  templateUrl: './pis-tan-confirmation-page.component.html'
})
export class PisTanConfirmationPageComponent implements OnInit {
  tan: string;
  consentId: string;
  paymentId: string;
  tanError: boolean;



  constructor(private route: ActivatedRoute, private router: Router, private bankingService: PisService) { }


  ngOnInit() {
    this.route.queryParams
      .subscribe(params => { this.getBankingDetailsFromUrl(params); });
    let bankingData = <Banking>({ tan: this.tan, consentId: this.consentId, paymentId: this.paymentId });
    this.bankingService.saveData(bankingData);
    // this.bankingService.getSinglePayments().subscribe();
    // this.bankingService.generateTan().subscribe();
  }

  getBankingDetailsFromUrl(params: Params) {
    this.consentId = params['consentId'];
    this.paymentId = params['paymentId'];
  }


  onClickContinue() {
    this.bankingService.validateTan(this.tan)
      .subscribe(
        success => {
          this.bankingService.updateConsentStatus(ConsentStatusEnum.VALID).subscribe();
          this.router.navigate(['pis/consentconfirmationsuccessful']);
        },
        error => {
          if (error.error.message === 'WRONG_TAN') {
              this.tanError = true;
            }
            if (error.error.message === 'LIMIT_EXCEEDED') {
              this.router.navigate(['pis/tanconfirmationerror']);
            }
        }
      );
  }

  onClickCancel() {
    this.bankingService.updateConsentStatus(ConsentStatusEnum.REVOKEDBYPSU)
      .subscribe();
    this.router.navigate(['pis/tanconfirmationcanceled']);
  }
}
