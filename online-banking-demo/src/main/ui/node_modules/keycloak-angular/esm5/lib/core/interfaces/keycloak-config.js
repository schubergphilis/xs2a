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
/**
 * This is the interface containing the attributes for the keycloak configuration in case
 * you don't specify a keycloak.json file in your project.
 * @record
 */
export function KeycloakConfig() { }
function KeycloakConfig_tsickle_Closure_declarations() {
    /**
     * Keycloak server url, for example: http://localhost:8080/auth
     * @type {?}
     */
    KeycloakConfig.prototype.url;
    /**
     * Realm name, ie.: myrealm
     * @type {?}
     */
    KeycloakConfig.prototype.realm;
    /**
     * Client ID, ie.: myapp
     * @type {?}
     */
    KeycloakConfig.prototype.clientId;
    /**
     * The credentials object contains the secret property that should be used depending on
     * which flow and access type was chosen.
     * @type {?|undefined}
     */
    KeycloakConfig.prototype.credentials;
}

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWstY29uZmlnLmpzIiwic291cmNlUm9vdCI6Im5nOi8va2V5Y2xvYWstYW5ndWxhci8iLCJzb3VyY2VzIjpbImxpYi9jb3JlL2ludGVyZmFjZXMva2V5Y2xvYWstY29uZmlnLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiIiLCJzb3VyY2VzQ29udGVudCI6WyIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgTWF1cmljaW8gR2VtZWxsaSBWaWdvbG8gYW5kIGNvbnRyaWJ1dG9ycy5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vZ2l0aHViLmNvbS9tYXVyaWNpb3ZpZ29sby9rZXljbG9hay1hbmd1bGFyL0xJQ0VOU0VcbiAqL1xuXG4vKipcbiAqIENyZWRlbnRpYWxzIHR5cGUuIFNob3VsZCBiZSB1c2VkIHdoZW4gVGhlIEFjY2VzcyBUeXBlIGlzIGNvbmZpZ3VyZWQgYXMgQ29uZmlkZW50aWFsLCBhc1xuICogbWVudGlvbmVkIGluIHRoZSBrZXljbG9haydzIGRvY3VtZW50YXRpb246XG4gKiBodHRwOi8vd3d3LmtleWNsb2FrLm9yZy9kb2NzL2xhdGVzdC9zZWN1cmluZ19hcHBzL2luZGV4Lmh0bWwjX2NvbmZpZ3VyaW5nX2FfY2xpZW50X2Zvcl91c2Vfd2l0aF9jbGllbnRfcmVnaXN0cmF0aW9uX2NsaVxuICovXG5leHBvcnQgdHlwZSBDcmVkZW50aWFscyA9IHtcbiAgLyoqXG4gICAqIFNlY3JldCBvciBTaWduZWQgSldULiBQbGVhc2UsIGNhdXRpb24gd2hlcmUgeW91IHN0b3JlIHRoaXMgc2Vuc2l0aXZlIGluZm9ybWF0aW9uIVxuICAgKi9cbiAgc2VjcmV0OiBzdHJpbmc7XG59O1xuXG4vKipcbiAqIFRoaXMgaXMgdGhlIGludGVyZmFjZSBjb250YWluaW5nIHRoZSBhdHRyaWJ1dGVzIGZvciB0aGUga2V5Y2xvYWsgY29uZmlndXJhdGlvbiBpbiBjYXNlXG4gKiB5b3UgZG9uJ3Qgc3BlY2lmeSBhIGtleWNsb2FrLmpzb24gZmlsZSBpbiB5b3VyIHByb2plY3QuXG4gKi9cbmV4cG9ydCBpbnRlcmZhY2UgS2V5Y2xvYWtDb25maWcge1xuICAvKipcbiAgICogS2V5Y2xvYWsgc2VydmVyIHVybCwgZm9yIGV4YW1wbGU6IGh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hdXRoXG4gICAqL1xuICB1cmw6IHN0cmluZztcbiAgLyoqXG4gICAqIFJlYWxtIG5hbWUsIGllLjogbXlyZWFsbVxuICAgKi9cbiAgcmVhbG06IHN0cmluZztcbiAgLyoqXG4gICAqIENsaWVudCBJRCwgaWUuOiBteWFwcFxuICAgKi9cbiAgY2xpZW50SWQ6IHN0cmluZztcbiAgLyoqXG4gICAqIFRoZSBjcmVkZW50aWFscyBvYmplY3QgY29udGFpbnMgdGhlIHNlY3JldCBwcm9wZXJ0eSB0aGF0IHNob3VsZCBiZSB1c2VkIGRlcGVuZGluZyBvblxuICAgKiB3aGljaCBmbG93IGFuZCBhY2Nlc3MgdHlwZSB3YXMgY2hvc2VuLlxuICAgKi9cbiAgY3JlZGVudGlhbHM/OiBDcmVkZW50aWFscztcbn1cbiJdfQ==