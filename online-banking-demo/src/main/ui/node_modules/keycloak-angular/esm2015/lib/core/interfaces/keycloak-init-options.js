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
 * Definitions file from KeycloakInitOptions, from keycloak-js library.
 * @record
 */
export function KeycloakInitOptions() { }
function KeycloakInitOptions_tsickle_Closure_declarations() {
    /**
     * Specifies an action to do on load.
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.onLoad;
    /**
     * Set an initial value for the token.
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.token;
    /**
     * Set an initial value for the refresh token.
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.refreshToken;
    /**
     * Set an initial value for the id token (only together with `token` or
     * `refreshToken`).
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.idToken;
    /**
     * Set an initial value for skew between local time and Keycloak server in
     * seconds (only together with `token` or `refreshToken`).
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.timeSkew;
    /**
     * Set to enable/disable monitoring login state.
     * \@default true
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.checkLoginIframe;
    /**
     * Set the interval to check login state (in seconds).
     * \@default 5
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.checkLoginIframeInterval;
    /**
     * Set the OpenID Connect response mode to send to Keycloak upon login.
     * \@default fragment After successful authentication Keycloak will redirect
     *                   to JavaScript application with OpenID Connect parameters
     *                   added in URL fragment. This is generally safer and
     *                   recommended over query.
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.responseMode;
    /**
     * Set the OpenID Connect flow.
     * \@default standard
     * @type {?|undefined}
     */
    KeycloakInitOptions.prototype.flow;
}

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWstaW5pdC1vcHRpb25zLmpzIiwic291cmNlUm9vdCI6Im5nOi8va2V5Y2xvYWstYW5ndWxhci8iLCJzb3VyY2VzIjpbImxpYi9jb3JlL2ludGVyZmFjZXMva2V5Y2xvYWstaW5pdC1vcHRpb25zLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiIiLCJzb3VyY2VzQ29udGVudCI6WyIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgTWF1cmljaW8gR2VtZWxsaSBWaWdvbG8gYW5kIGNvbnRyaWJ1dG9ycy5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vZ2l0aHViLmNvbS9tYXVyaWNpb3ZpZ29sby9rZXljbG9hay1hbmd1bGFyL0xJQ0VOU0VcbiAqL1xuXG4vKipcbiAqIEtleWNsb2FrIG9ubG9hZCBvcHRpb25zOiAnbG9naW4tcmVxdWlyZWQnIG9yICdjaGVjay1zc28nXG4gKi9cbmV4cG9ydCB0eXBlIEtleWNsb2FrT25Mb2FkID0gJ2xvZ2luLXJlcXVpcmVkJyB8ICdjaGVjay1zc28nO1xuLyoqXG4gKiBLZXljbG9hayByZXNwb25zZSBtb2RlIG9wdGlvbnM6ICdxdWVyeScgb3IgJ2ZyYWdtZW50J1xuICovXG5leHBvcnQgdHlwZSBLZXljbG9ha1Jlc3BvbnNlTW9kZSA9ICdxdWVyeScgfCAnZnJhZ21lbnQnO1xuLyoqXG4gKiBLZXljbG9hayByZXNwb25zZSB0eXBlIG9wdGlvbnM6ICdjb2RlJyBvciAnaWRfdG9rZW4gdG9rZW4nIG9yICdjb2RlIGlkX3Rva2VuIHRva2VuJ1xuICovXG5leHBvcnQgdHlwZSBLZXljbG9ha1Jlc3BvbnNlVHlwZSA9ICdjb2RlJyB8ICdpZF90b2tlbiB0b2tlbicgfCAnY29kZSBpZF90b2tlbiB0b2tlbic7XG4vKipcbiAqIEtleWNsb2FrIGZsb3c6ICdzdGFuZGFyZCcgb3IgJ2ltcGxpY2l0JyBvciAnaHlicmlkJ1xuICovXG5leHBvcnQgdHlwZSBLZXljbG9ha0Zsb3cgPSAnc3RhbmRhcmQnIHwgJ2ltcGxpY2l0JyB8ICdoeWJyaWQnO1xuXG4vKipcbiAqIERlZmluaXRpb25zIGZpbGUgZnJvbSBLZXljbG9ha0luaXRPcHRpb25zLCBmcm9tIGtleWNsb2FrLWpzIGxpYnJhcnkuXG4gKi9cbmV4cG9ydCBpbnRlcmZhY2UgS2V5Y2xvYWtJbml0T3B0aW9ucyB7XG4gIC8qKlxuICAgKiBTcGVjaWZpZXMgYW4gYWN0aW9uIHRvIGRvIG9uIGxvYWQuXG4gICAqL1xuICBvbkxvYWQ/OiBLZXljbG9ha09uTG9hZDtcbiAgLyoqXG4gICAqIFNldCBhbiBpbml0aWFsIHZhbHVlIGZvciB0aGUgdG9rZW4uXG4gICAqL1xuICB0b2tlbj86IHN0cmluZztcbiAgLyoqXG4gICAqIFNldCBhbiBpbml0aWFsIHZhbHVlIGZvciB0aGUgcmVmcmVzaCB0b2tlbi5cbiAgICovXG4gIHJlZnJlc2hUb2tlbj86IHN0cmluZztcbiAgLyoqXG4gICAqIFNldCBhbiBpbml0aWFsIHZhbHVlIGZvciB0aGUgaWQgdG9rZW4gKG9ubHkgdG9nZXRoZXIgd2l0aCBgdG9rZW5gIG9yXG4gICAqIGByZWZyZXNoVG9rZW5gKS5cbiAgICovXG4gIGlkVG9rZW4/OiBzdHJpbmc7XG4gIC8qKlxuICAgKiBTZXQgYW4gaW5pdGlhbCB2YWx1ZSBmb3Igc2tldyBiZXR3ZWVuIGxvY2FsIHRpbWUgYW5kIEtleWNsb2FrIHNlcnZlciBpblxuICAgKiBzZWNvbmRzIChvbmx5IHRvZ2V0aGVyIHdpdGggYHRva2VuYCBvciBgcmVmcmVzaFRva2VuYCkuXG4gICAqL1xuICB0aW1lU2tldz86IG51bWJlcjtcbiAgLyoqXG4gICAqIFNldCB0byBlbmFibGUvZGlzYWJsZSBtb25pdG9yaW5nIGxvZ2luIHN0YXRlLlxuICAgKiBAZGVmYXVsdCB0cnVlXG4gICAqL1xuICBjaGVja0xvZ2luSWZyYW1lPzogYm9vbGVhbjtcbiAgLyoqXG4gICAqIFNldCB0aGUgaW50ZXJ2YWwgdG8gY2hlY2sgbG9naW4gc3RhdGUgKGluIHNlY29uZHMpLlxuICAgKiBAZGVmYXVsdCA1XG4gICAqL1xuICBjaGVja0xvZ2luSWZyYW1lSW50ZXJ2YWw/OiBudW1iZXIgfCBhbnk7XG4gIC8qKlxuICAgKiBTZXQgdGhlIE9wZW5JRCBDb25uZWN0IHJlc3BvbnNlIG1vZGUgdG8gc2VuZCB0byBLZXljbG9hayB1cG9uIGxvZ2luLlxuICAgKiBAZGVmYXVsdCBmcmFnbWVudCBBZnRlciBzdWNjZXNzZnVsIGF1dGhlbnRpY2F0aW9uIEtleWNsb2FrIHdpbGwgcmVkaXJlY3RcbiAgICogICAgICAgICAgICAgICAgICAgdG8gSmF2YVNjcmlwdCBhcHBsaWNhdGlvbiB3aXRoIE9wZW5JRCBDb25uZWN0IHBhcmFtZXRlcnNcbiAgICogICAgICAgICAgICAgICAgICAgYWRkZWQgaW4gVVJMIGZyYWdtZW50LiBUaGlzIGlzIGdlbmVyYWxseSBzYWZlciBhbmRcbiAgICogICAgICAgICAgICAgICAgICAgcmVjb21tZW5kZWQgb3ZlciBxdWVyeS5cbiAgICovXG4gIHJlc3BvbnNlTW9kZT86IEtleWNsb2FrUmVzcG9uc2VNb2RlO1xuICAvKipcbiAgICogU2V0IHRoZSBPcGVuSUQgQ29ubmVjdCBmbG93LlxuICAgKiBAZGVmYXVsdCBzdGFuZGFyZFxuICAgKi9cbiAgZmxvdz86IEtleWNsb2FrRmxvdztcbn1cbiJdfQ==