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
 * keycloak-angular initialization options.
 * @record
 */
export function KeycloakOptions() { }
function KeycloakOptions_tsickle_Closure_declarations() {
    /**
     * Configs to init the keycloak-js library. If undefined, will look for a keycloak.json file
     * at root of the project.
     * If not undefined, can be a string meaning the url to the keycloak.json file or an object
     * of {\@link KeycloakConfig}. Use this configuration if you want to specify the keycloak server,
     * realm, clientId. This is usefull if you have different configurations for production, stage
     * and development environments. Hint: Make use of Angular environment configuration.
     * @type {?|undefined}
     */
    KeycloakOptions.prototype.config;
    /**
     * Options to initialize the adapter. Used by keycloak-js.
     * @type {?|undefined}
     */
    KeycloakOptions.prototype.initOptions;
    /**
     * By default all requests made by Angular HttpClient will be intercepted in order to
     * add the bearer in the Authorization Http Header. However, if this is a not desired
     * feature, the enableBearerInterceptor must be false.
     *
     * Briefly, if enableBearerInterceptor === false, the bearer will not be added
     * to the authorization header.
     *
     * The default value is true.
     * @type {?|undefined}
     */
    KeycloakOptions.prototype.enableBearerInterceptor;
    /**
     * Forces the execution of loadUserProfile after the keycloak initialization considering that the
     * user logged in.
     * This option is recommended if is desirable to have the user details at the beginning,
     * so after the login, the loadUserProfile function will be called and it's value cached.
     *
     * The default value is true.
     * @type {?|undefined}
     */
    KeycloakOptions.prototype.loadUserProfileAtStartUp;
    /**
     * String Array to exclude the urls that should not have the Authorization Header automatically
     * added. This library makes use of Angular Http Interceptor, to automatically add the Bearer
     * token to the request.
     * @type {?|undefined}
     */
    KeycloakOptions.prototype.bearerExcludedUrls;
    /**
     * This value will be used as the Authorization Http Header name. The default value is
     * **Authorization**. If the backend expects requests to have a token in a different header, you
     * should change this value, i.e: **JWT-Authorization**. This will result in a Http Header
     * Authorization as "JWT-Authorization: bearer <token>".
     * @type {?|undefined}
     */
    KeycloakOptions.prototype.authorizationHeaderName;
    /**
     * This value will be included in the Authorization Http Header param. The default value is
     * **bearer**, which will result in a Http Header Authorization as "Authorization: bearer <token>".
     * If any other value is needed by the backend in the authorization header, you should change this
     * value, i.e: **Bearer**.
     *
     * Warning: this value must be in compliance with the keycloak server instance and the adapter.
     * @type {?|undefined}
     */
    KeycloakOptions.prototype.bearerPrefix;
}

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWstb3B0aW9ucy5qcyIsInNvdXJjZVJvb3QiOiJuZzovL2tleWNsb2FrLWFuZ3VsYXIvIiwic291cmNlcyI6WyJsaWIvY29yZS9pbnRlcmZhY2VzL2tleWNsb2FrLW9wdGlvbnMudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsInNvdXJjZXNDb250ZW50IjpbIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBNYXVyaWNpbyBHZW1lbGxpIFZpZ29sbyBhbmQgY29udHJpYnV0b3JzLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGEgTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9naXRodWIuY29tL21hdXJpY2lvdmlnb2xvL2tleWNsb2FrLWFuZ3VsYXIvTElDRU5TRVxuICovXG5cbmltcG9ydCB7IEtleWNsb2FrSW5pdE9wdGlvbnMgfSBmcm9tICcuL2tleWNsb2FrLWluaXQtb3B0aW9ucyc7XG5pbXBvcnQgeyBLZXljbG9ha0NvbmZpZyB9IGZyb20gJy4va2V5Y2xvYWstY29uZmlnJztcblxuLyoqXG4gKiBrZXljbG9hay1hbmd1bGFyIGluaXRpYWxpemF0aW9uIG9wdGlvbnMuXG4gKi9cbmV4cG9ydCBpbnRlcmZhY2UgS2V5Y2xvYWtPcHRpb25zIHtcbiAgLyoqXG4gICAqIENvbmZpZ3MgdG8gaW5pdCB0aGUga2V5Y2xvYWstanMgbGlicmFyeS4gSWYgdW5kZWZpbmVkLCB3aWxsIGxvb2sgZm9yIGEga2V5Y2xvYWsuanNvbiBmaWxlXG4gICAqIGF0IHJvb3Qgb2YgdGhlIHByb2plY3QuXG4gICAqIElmIG5vdCB1bmRlZmluZWQsIGNhbiBiZSBhIHN0cmluZyBtZWFuaW5nIHRoZSB1cmwgdG8gdGhlIGtleWNsb2FrLmpzb24gZmlsZSBvciBhbiBvYmplY3RcbiAgICogb2Yge0BsaW5rIEtleWNsb2FrQ29uZmlnfS4gVXNlIHRoaXMgY29uZmlndXJhdGlvbiBpZiB5b3Ugd2FudCB0byBzcGVjaWZ5IHRoZSBrZXljbG9hayBzZXJ2ZXIsXG4gICAqIHJlYWxtLCBjbGllbnRJZC4gVGhpcyBpcyB1c2VmdWxsIGlmIHlvdSBoYXZlIGRpZmZlcmVudCBjb25maWd1cmF0aW9ucyBmb3IgcHJvZHVjdGlvbiwgc3RhZ2VcbiAgICogYW5kIGRldmVsb3BtZW50IGVudmlyb25tZW50cy4gSGludDogTWFrZSB1c2Ugb2YgQW5ndWxhciBlbnZpcm9ubWVudCBjb25maWd1cmF0aW9uLlxuICAgKi9cbiAgY29uZmlnPzogc3RyaW5nIHwgS2V5Y2xvYWtDb25maWc7XG4gIC8qKlxuICAgKiBPcHRpb25zIHRvIGluaXRpYWxpemUgdGhlIGFkYXB0ZXIuIFVzZWQgYnkga2V5Y2xvYWstanMuXG4gICAqL1xuICBpbml0T3B0aW9ucz86IEtleWNsb2FrSW5pdE9wdGlvbnM7XG4gIC8qKlxuICAgKiBCeSBkZWZhdWx0IGFsbCByZXF1ZXN0cyBtYWRlIGJ5IEFuZ3VsYXIgSHR0cENsaWVudCB3aWxsIGJlIGludGVyY2VwdGVkIGluIG9yZGVyIHRvXG4gICAqIGFkZCB0aGUgYmVhcmVyIGluIHRoZSBBdXRob3JpemF0aW9uIEh0dHAgSGVhZGVyLiBIb3dldmVyLCBpZiB0aGlzIGlzIGEgbm90IGRlc2lyZWRcbiAgICogZmVhdHVyZSwgdGhlIGVuYWJsZUJlYXJlckludGVyY2VwdG9yIG11c3QgYmUgZmFsc2UuXG4gICAqXG4gICAqIEJyaWVmbHksIGlmIGVuYWJsZUJlYXJlckludGVyY2VwdG9yID09PSBmYWxzZSwgdGhlIGJlYXJlciB3aWxsIG5vdCBiZSBhZGRlZFxuICAgKiB0byB0aGUgYXV0aG9yaXphdGlvbiBoZWFkZXIuXG4gICAqXG4gICAqIFRoZSBkZWZhdWx0IHZhbHVlIGlzIHRydWUuXG4gICAqL1xuICBlbmFibGVCZWFyZXJJbnRlcmNlcHRvcj86IGJvb2xlYW47XG4gIC8qKlxuICAgKiBGb3JjZXMgdGhlIGV4ZWN1dGlvbiBvZiBsb2FkVXNlclByb2ZpbGUgYWZ0ZXIgdGhlIGtleWNsb2FrIGluaXRpYWxpemF0aW9uIGNvbnNpZGVyaW5nIHRoYXQgdGhlXG4gICAqIHVzZXIgbG9nZ2VkIGluLlxuICAgKiBUaGlzIG9wdGlvbiBpcyByZWNvbW1lbmRlZCBpZiBpcyBkZXNpcmFibGUgdG8gaGF2ZSB0aGUgdXNlciBkZXRhaWxzIGF0IHRoZSBiZWdpbm5pbmcsXG4gICAqIHNvIGFmdGVyIHRoZSBsb2dpbiwgdGhlIGxvYWRVc2VyUHJvZmlsZSBmdW5jdGlvbiB3aWxsIGJlIGNhbGxlZCBhbmQgaXQncyB2YWx1ZSBjYWNoZWQuXG4gICAqXG4gICAqIFRoZSBkZWZhdWx0IHZhbHVlIGlzIHRydWUuXG4gICAqL1xuICBsb2FkVXNlclByb2ZpbGVBdFN0YXJ0VXA/OiBib29sZWFuO1xuICAvKipcbiAgICogU3RyaW5nIEFycmF5IHRvIGV4Y2x1ZGUgdGhlIHVybHMgdGhhdCBzaG91bGQgbm90IGhhdmUgdGhlIEF1dGhvcml6YXRpb24gSGVhZGVyIGF1dG9tYXRpY2FsbHlcbiAgICogYWRkZWQuIFRoaXMgbGlicmFyeSBtYWtlcyB1c2Ugb2YgQW5ndWxhciBIdHRwIEludGVyY2VwdG9yLCB0byBhdXRvbWF0aWNhbGx5IGFkZCB0aGUgQmVhcmVyXG4gICAqIHRva2VuIHRvIHRoZSByZXF1ZXN0LlxuICAgKi9cbiAgYmVhcmVyRXhjbHVkZWRVcmxzPzogc3RyaW5nW107XG4gIC8qKlxuICAgKiBUaGlzIHZhbHVlIHdpbGwgYmUgdXNlZCBhcyB0aGUgQXV0aG9yaXphdGlvbiBIdHRwIEhlYWRlciBuYW1lLiBUaGUgZGVmYXVsdCB2YWx1ZSBpc1xuICAgKiAqKkF1dGhvcml6YXRpb24qKi4gSWYgdGhlIGJhY2tlbmQgZXhwZWN0cyByZXF1ZXN0cyB0byBoYXZlIGEgdG9rZW4gaW4gYSBkaWZmZXJlbnQgaGVhZGVyLCB5b3VcbiAgICogc2hvdWxkIGNoYW5nZSB0aGlzIHZhbHVlLCBpLmU6ICoqSldULUF1dGhvcml6YXRpb24qKi4gVGhpcyB3aWxsIHJlc3VsdCBpbiBhIEh0dHAgSGVhZGVyXG4gICAqIEF1dGhvcml6YXRpb24gYXMgXCJKV1QtQXV0aG9yaXphdGlvbjogYmVhcmVyIDx0b2tlbj5cIi5cbiAgICovXG4gIGF1dGhvcml6YXRpb25IZWFkZXJOYW1lPzogc3RyaW5nO1xuICAvKipcbiAgICogVGhpcyB2YWx1ZSB3aWxsIGJlIGluY2x1ZGVkIGluIHRoZSBBdXRob3JpemF0aW9uIEh0dHAgSGVhZGVyIHBhcmFtLiBUaGUgZGVmYXVsdCB2YWx1ZSBpc1xuICAgKiAqKmJlYXJlcioqLCB3aGljaCB3aWxsIHJlc3VsdCBpbiBhIEh0dHAgSGVhZGVyIEF1dGhvcml6YXRpb24gYXMgXCJBdXRob3JpemF0aW9uOiBiZWFyZXIgPHRva2VuPlwiLlxuICAgKiBJZiBhbnkgb3RoZXIgdmFsdWUgaXMgbmVlZGVkIGJ5IHRoZSBiYWNrZW5kIGluIHRoZSBhdXRob3JpemF0aW9uIGhlYWRlciwgeW91IHNob3VsZCBjaGFuZ2UgdGhpc1xuICAgKiB2YWx1ZSwgaS5lOiAqKkJlYXJlcioqLlxuICAgKlxuICAgKiBXYXJuaW5nOiB0aGlzIHZhbHVlIG11c3QgYmUgaW4gY29tcGxpYW5jZSB3aXRoIHRoZSBrZXljbG9hayBzZXJ2ZXIgaW5zdGFuY2UgYW5kIHRoZSBhZGFwdGVyLlxuICAgKi9cbiAgYmVhcmVyUHJlZml4Pzogc3RyaW5nO1xufVxuIl19