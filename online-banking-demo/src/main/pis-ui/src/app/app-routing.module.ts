import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PisTanConfirmationPageComponent } from './components/pis-tan-confirmation-page/pis-tan-confirmation-page.component';
import { PisTanConfirmationErrorComponent } from './components/pis-tan-confirmation-error/pis-tan-confirmation-error.component';
import { PisTanConfirmationCanceledComponent } from './components/pis-tan-confirmation-canceled/pis-tan-confirmation-canceled.component';
import { PisConsentConfirmationPageComponent } from './components/pis-consent-confirmation-page/pis-consent-confirmation-page.component';
import { PisConsentConfirmationDeniedComponent } from './components/pis-consent-confirmation-denied/pis-consent-confirmation-denied.component';
import { PisConsentConfirmationSuccessfulComponent } from './components/pis-consent-confirmation-successful/pis-consent-confirmation-successful.component';
import { AppAuthGuard } from './app.authguard';
import { PisHelpPageComponent } from './components/pis-help-page/pis-help-page.component';



const routes: Routes = [
  { path: 'pis', component: PisHelpPageComponent},
  { path: 'pis/:consentId/:paymentId', component: PisConsentConfirmationPageComponent },
  { path: 'pis/tanconfirmationcanceled', component: PisTanConfirmationCanceledComponent },
  { path: 'pis/tanconfirmationerror', component: PisTanConfirmationErrorComponent },
  { path: 'pis/tanconfirmation', component: PisTanConfirmationPageComponent },
  { path: 'pis/consentconfirmationdenied', component: PisConsentConfirmationDeniedComponent },
  { path: 'pis/consentconfirmationsuccessful', component: PisConsentConfirmationSuccessfulComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  providers: [
    AppAuthGuard
  ]
})
export class AppRoutingModule { }
