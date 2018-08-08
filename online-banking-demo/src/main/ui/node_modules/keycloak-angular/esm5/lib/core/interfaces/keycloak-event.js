/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes} checked by tsc
 */
/**
 * @license
 * Copyright Mauricio Gemelli Vigolo and contributors.
 *
 * Use of this source code is governed by a MIT-style license that can be
 * found in the LICENSE file at https://github.com/mauriciovigolo/keycloak-angular/LICENSE
 */
/** @enum {number} */
var KeycloakEventType = {
    /**
       * Called if there was an error during authentication.
       */
    OnAuthError: 0,
    /**
       * Called if the user is logged out
       * (will only be called if the session status iframe is enabled, or in Cordova mode).
       */
    OnAuthLogout: 1,
    /**
       * Called if there was an error while trying to refresh the token.
       */
    OnAuthRefreshError: 2,
    /**
       * Called when the token is refreshed.
       */
    OnAuthRefreshSuccess: 3,
    /**
       * Called when a user is successfully authenticated.
       */
    OnAuthSuccess: 4,
    /**
       * Called when the adapter is initialized.
       */
    OnReady: 5,
    /**
       * Called when the access token is expired. If a refresh token is available the token
       * can be refreshed with updateToken, or in cases where it is not (that is, with implicit flow)
       * you can redirect to login screen to obtain a new access token.
       */
    OnTokenExpired: 6,
};
export { KeycloakEventType };
KeycloakEventType[KeycloakEventType.OnAuthError] = 'OnAuthError';
KeycloakEventType[KeycloakEventType.OnAuthLogout] = 'OnAuthLogout';
KeycloakEventType[KeycloakEventType.OnAuthRefreshError] = 'OnAuthRefreshError';
KeycloakEventType[KeycloakEventType.OnAuthRefreshSuccess] = 'OnAuthRefreshSuccess';
KeycloakEventType[KeycloakEventType.OnAuthSuccess] = 'OnAuthSuccess';
KeycloakEventType[KeycloakEventType.OnReady] = 'OnReady';
KeycloakEventType[KeycloakEventType.OnTokenExpired] = 'OnTokenExpired';
/**
 * Structure of an event triggered by Keycloak, contains it's type
 * and arguments (if any).
 * @record
 */
export function KeycloakEvent() { }
function KeycloakEvent_tsickle_Closure_declarations() {
    /**
     * Event type as described at {\@link KeycloakEventType}.
     * @type {?}
     */
    KeycloakEvent.prototype.type;
    /**
     * Arguments from the keycloak-js event function.
     * @type {?|undefined}
     */
    KeycloakEvent.prototype.args;
}

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWstZXZlbnQuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9rZXljbG9hay1hbmd1bGFyLyIsInNvdXJjZXMiOlsibGliL2NvcmUvaW50ZXJmYWNlcy9rZXljbG9hay1ldmVudC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7O0lBZ0JFLGNBQVc7Ozs7O0lBS1gsZUFBWTs7OztJQUlaLHFCQUFrQjs7OztJQUlsQix1QkFBb0I7Ozs7SUFJcEIsZ0JBQWE7Ozs7SUFJYixVQUFPOzs7Ozs7SUFNUCxpQkFBYzs7O29DQTNCZCxXQUFXO29DQUtYLFlBQVk7b0NBSVosa0JBQWtCO29DQUlsQixvQkFBb0I7b0NBSXBCLGFBQWE7b0NBSWIsT0FBTztvQ0FNUCxjQUFjIiwic291cmNlc0NvbnRlbnQiOlsiLyoqXG4gKiBAbGljZW5zZVxuICogQ29weXJpZ2h0IE1hdXJpY2lvIEdlbWVsbGkgVmlnb2xvIGFuZCBjb250cmlidXRvcnMuXG4gKlxuICogVXNlIG9mIHRoaXMgc291cmNlIGNvZGUgaXMgZ292ZXJuZWQgYnkgYSBNSVQtc3R5bGUgbGljZW5zZSB0aGF0IGNhbiBiZVxuICogZm91bmQgaW4gdGhlIExJQ0VOU0UgZmlsZSBhdCBodHRwczovL2dpdGh1Yi5jb20vbWF1cmljaW92aWdvbG8va2V5Y2xvYWstYW5ndWxhci9MSUNFTlNFXG4gKi9cblxuLyoqXG4gKiBLZXljbG9hayBldmVudCB0eXBlcywgYXMgZGVzY3JpYmVkIGF0IHRoZSBrZXljbG9hay1qcyBkb2N1bWVudGF0aW9uOlxuICogaHR0cHM6Ly93d3cua2V5Y2xvYWsub3JnL2RvY3MvbGF0ZXN0L3NlY3VyaW5nX2FwcHMvaW5kZXguaHRtbCNjYWxsYmFjay1ldmVudHNcbiAqL1xuZXhwb3J0IGVudW0gS2V5Y2xvYWtFdmVudFR5cGUge1xuICAvKipcbiAgICogQ2FsbGVkIGlmIHRoZXJlIHdhcyBhbiBlcnJvciBkdXJpbmcgYXV0aGVudGljYXRpb24uXG4gICAqL1xuICBPbkF1dGhFcnJvcixcbiAgLyoqXG4gICAqIENhbGxlZCBpZiB0aGUgdXNlciBpcyBsb2dnZWQgb3V0XG4gICAqICh3aWxsIG9ubHkgYmUgY2FsbGVkIGlmIHRoZSBzZXNzaW9uIHN0YXR1cyBpZnJhbWUgaXMgZW5hYmxlZCwgb3IgaW4gQ29yZG92YSBtb2RlKS5cbiAgICovXG4gIE9uQXV0aExvZ291dCxcbiAgLyoqXG4gICAqIENhbGxlZCBpZiB0aGVyZSB3YXMgYW4gZXJyb3Igd2hpbGUgdHJ5aW5nIHRvIHJlZnJlc2ggdGhlIHRva2VuLlxuICAgKi9cbiAgT25BdXRoUmVmcmVzaEVycm9yLFxuICAvKipcbiAgICogQ2FsbGVkIHdoZW4gdGhlIHRva2VuIGlzIHJlZnJlc2hlZC5cbiAgICovXG4gIE9uQXV0aFJlZnJlc2hTdWNjZXNzLFxuICAvKipcbiAgICogQ2FsbGVkIHdoZW4gYSB1c2VyIGlzIHN1Y2Nlc3NmdWxseSBhdXRoZW50aWNhdGVkLlxuICAgKi9cbiAgT25BdXRoU3VjY2VzcyxcbiAgLyoqXG4gICAqIENhbGxlZCB3aGVuIHRoZSBhZGFwdGVyIGlzIGluaXRpYWxpemVkLlxuICAgKi9cbiAgT25SZWFkeSxcbiAgLyoqXG4gICAqIENhbGxlZCB3aGVuIHRoZSBhY2Nlc3MgdG9rZW4gaXMgZXhwaXJlZC4gSWYgYSByZWZyZXNoIHRva2VuIGlzIGF2YWlsYWJsZSB0aGUgdG9rZW5cbiAgICogY2FuIGJlIHJlZnJlc2hlZCB3aXRoIHVwZGF0ZVRva2VuLCBvciBpbiBjYXNlcyB3aGVyZSBpdCBpcyBub3QgKHRoYXQgaXMsIHdpdGggaW1wbGljaXQgZmxvdylcbiAgICogeW91IGNhbiByZWRpcmVjdCB0byBsb2dpbiBzY3JlZW4gdG8gb2J0YWluIGEgbmV3IGFjY2VzcyB0b2tlbi5cbiAgICovXG4gIE9uVG9rZW5FeHBpcmVkXG59XG5cbi8qKlxuICogU3RydWN0dXJlIG9mIGFuIGV2ZW50IHRyaWdnZXJlZCBieSBLZXljbG9haywgY29udGFpbnMgaXQncyB0eXBlXG4gKiBhbmQgYXJndW1lbnRzIChpZiBhbnkpLlxuICovXG5leHBvcnQgaW50ZXJmYWNlIEtleWNsb2FrRXZlbnQge1xuICAvKipcbiAgICogRXZlbnQgdHlwZSBhcyBkZXNjcmliZWQgYXQge0BsaW5rIEtleWNsb2FrRXZlbnRUeXBlfS5cbiAgICovXG4gIHR5cGU6IEtleWNsb2FrRXZlbnRUeXBlO1xuICAvKipcbiAgICogQXJndW1lbnRzIGZyb20gdGhlIGtleWNsb2FrLWpzIGV2ZW50IGZ1bmN0aW9uLlxuICAgKi9cbiAgYXJncz86IGFueTtcbn1cbiJdfQ==