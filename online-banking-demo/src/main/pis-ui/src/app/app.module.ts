import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER, LOCALE_ID } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { PisTanConfirmationPageComponent } from './components/pis-tan-confirmation-page/pis-tan-confirmation-page.component';
import { PisTanConfirmationErrorComponent } from './components/pis-tan-confirmation-error/pis-tan-confirmation-error.component';
import { PisTanConfirmationCanceledComponent } from './components/pis-tan-confirmation-canceled/pis-tan-confirmation-canceled.component';
import { PisConsentConfirmationPageComponent } from './components/pis-consent-confirmation-page/pis-consent-confirmation-page.component';
import { PisConsentConfirmationDeniedComponent } from './components/pis-consent-confirmation-denied/pis-consent-confirmation-denied.component';
import { PisConsentConfirmationSuccessfulComponent } from './components/pis-consent-confirmation-successful/pis-consent-confirmation-successful.component';
import { initializer } from './utils/app-init';
import { KeycloakAngularModule, KeycloakService } from '../../node_modules/keycloak-angular';
import { FormsModule } from '@angular/forms';
import { PisConsentConfirmationErrorComponent } from './components/pis-consent-confirmation-error/pis-consent-confirmation-error.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { registerLocaleData } from '@angular/common';
import localeDE from '@angular/common/locales/de';
import { PisHelpPageComponent } from './components/pis-help-page/pis-help-page.component';

registerLocaleData(localeDE);

@NgModule({
  declarations: [
    AppComponent,
    PisTanConfirmationPageComponent,
    PisTanConfirmationErrorComponent,
    PisTanConfirmationCanceledComponent,
    PisConsentConfirmationPageComponent,
    PisConsentConfirmationDeniedComponent,
    PisConsentConfirmationSuccessfulComponent,
    PisConsentConfirmationErrorComponent,
    PisHelpPageComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    KeycloakAngularModule,
    FormsModule,
    NgbModule.forRoot(),
  ],
  providers: [{
    provide: APP_INITIALIZER,
    useFactory: initializer,
    multi: true,
    deps: [KeycloakService],
  }, {
    provide: LOCALE_ID, useValue: 'de'
  },
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
