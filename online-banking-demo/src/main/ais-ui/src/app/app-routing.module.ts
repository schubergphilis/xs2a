import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AisTanConfirmationPageComponent } from './components/ais-tan-confirmation-page/ais-tan-confirmation-page.component';
import { AisTanConfirmationErrorComponent } from './components/ais-tan-confirmation-error/ais-tan-confirmation-error.component';
import { AisTanConfirmationCanceledComponent } from './components/ais-tan-confirmation-canceled/ais-tan-confirmation-canceled.component';
import { AisConsentConfirmationPageComponent } from './components/ais-consent-confirmation-page/ais-consent-confirmation-page.component';
import { AisConsentConfirmationDeniedComponent } from './components/ais-consent-confirmation-denied/ais-consent-confirmation-denied.component';
import { AppAuthGuard } from './app.authguard';
import { AisTanConfirmationSuccessfulComponent } from './components/ais-tan-confirmation-successful/ais-tan-confirmation-successful.component';
import { AisConsentConfirmationErrorComponent } from './components/ais-consent-confirmation-error/ais-consent-confirmation-error.component';
import { AisHelpPageComponent } from './components/ais-help-page/ais-help-page.component';



const routes: Routes = [
  { path: 'ais', component: AisHelpPageComponent},
  { path: 'ais/consentconfirmationerror', component: AisConsentConfirmationErrorComponent },
  { path: 'ais/consentconfirmationdenied', component: AisConsentConfirmationDeniedComponent },
  { path: 'ais/tanconfirmation', component: AisTanConfirmationPageComponent},
  { path: 'ais/tanconfirmationcanceled', component: AisTanConfirmationCanceledComponent },
  { path: 'ais/tanconfirmationerror', component: AisTanConfirmationErrorComponent },
  { path: 'ais/tanconfirmationsuccessful', component: AisTanConfirmationSuccessfulComponent },
  { path: 'ais/:consentId', component: AisConsentConfirmationPageComponent},

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
