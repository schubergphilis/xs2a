(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@angular/core'), require('@angular/common/http'), require('keycloak-js'), require('rxjs'), require('rxjs/operators'), require('@angular/common')) :
    typeof define === 'function' && define.amd ? define('keycloak-angular', ['exports', '@angular/core', '@angular/common/http', 'keycloak-js', 'rxjs', 'rxjs/operators', '@angular/common'], factory) :
    (factory((global['keycloak-angular'] = {}),global.ng.core,global.ng.common.http,global.Keycloak,global.rxjs,global.rxjs.operators,global.ng.common));
}(this, (function (exports,core,http,Keycloak_,rxjs,operators,common) { 'use strict';

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
    KeycloakEventType[KeycloakEventType.OnAuthError] = 'OnAuthError';
    KeycloakEventType[KeycloakEventType.OnAuthLogout] = 'OnAuthLogout';
    KeycloakEventType[KeycloakEventType.OnAuthRefreshError] = 'OnAuthRefreshError';
    KeycloakEventType[KeycloakEventType.OnAuthRefreshSuccess] = 'OnAuthRefreshSuccess';
    KeycloakEventType[KeycloakEventType.OnAuthSuccess] = 'OnAuthSuccess';
    KeycloakEventType[KeycloakEventType.OnReady] = 'OnReady';
    KeycloakEventType[KeycloakEventType.OnTokenExpired] = 'OnTokenExpired';

    /*! *****************************************************************************
    Copyright (c) Microsoft Corporation. All rights reserved.
    Licensed under the Apache License, Version 2.0 (the "License"); you may not use
    this file except in compliance with the License. You may obtain a copy of the
    License at http://www.apache.org/licenses/LICENSE-2.0

    THIS CODE IS PROVIDED ON AN *AS IS* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION ANY IMPLIED
    WARRANTIES OR CONDITIONS OF TITLE, FITNESS FOR A PARTICULAR PURPOSE,
    MERCHANTABLITY OR NON-INFRINGEMENT.

    See the Apache Version 2.0 License for specific language governing permissions
    and limitations under the License.
    ***************************************************************************** */
    function __awaiter(thisArg, _arguments, P, generator) {
        return new (P || (P = Promise))(function (resolve, reject) {
            function fulfilled(value) { try {
                step(generator.next(value));
            }
            catch (e) {
                reject(e);
            } }
            function rejected(value) { try {
                step(generator["throw"](value));
            }
            catch (e) {
                reject(e);
            } }
            function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
            step((generator = generator.apply(thisArg, _arguments || [])).next());
        });
    }
    function __generator(thisArg, body) {
        var _ = { label: 0, sent: function () { if (t[0] & 1)
                throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
        return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function () { return this; }), g;
        function verb(n) { return function (v) { return step([n, v]); }; }
        function step(op) {
            if (f)
                throw new TypeError("Generator is already executing.");
            while (_)
                try {
                    if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done)
                        return t;
                    if (y = 0, t)
                        op = [op[0] & 2, t.value];
                    switch (op[0]) {
                        case 0:
                        case 1:
                            t = op;
                            break;
                        case 4:
                            _.label++;
                            return { value: op[1], done: false };
                        case 5:
                            _.label++;
                            y = op[1];
                            op = [0];
                            continue;
                        case 7:
                            op = _.ops.pop();
                            _.trys.pop();
                            continue;
                        default:
                            if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) {
                                _ = 0;
                                continue;
                            }
                            if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) {
                                _.label = op[1];
                                break;
                            }
                            if (op[0] === 6 && _.label < t[1]) {
                                _.label = t[1];
                                t = op;
                                break;
                            }
                            if (t && _.label < t[2]) {
                                _.label = t[2];
                                _.ops.push(op);
                                break;
                            }
                            if (t[2])
                                _.ops.pop();
                            _.trys.pop();
                            continue;
                    }
                    op = body.call(thisArg, _);
                }
                catch (e) {
                    op = [6, e];
                    y = 0;
                }
                finally {
                    f = t = 0;
                }
            if (op[0] & 5)
                throw op[1];
            return { value: op[0] ? op[1] : void 0, done: true };
        }
    }
    function __read(o, n) {
        var m = typeof Symbol === "function" && o[Symbol.iterator];
        if (!m)
            return o;
        var i = m.call(o), r, ar = [], e;
        try {
            while ((n === void 0 || n-- > 0) && !(r = i.next()).done)
                ar.push(r.value);
        }
        catch (error) {
            e = { error: error };
        }
        finally {
            try {
                if (r && !r.done && (m = i["return"]))
                    m.call(i);
            }
            finally {
                if (e)
                    throw e.error;
            }
        }
        return ar;
    }
    function __spread() {
        for (var ar = [], i = 0; i < arguments.length; i++)
            ar = ar.concat(__read(arguments[i]));
        return ar;
    }

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes} checked by tsc
     */
    /**
     * A simple guard implementation out of the box. This class should be inherited and
     * implemented by the application. The only method that should be implemented is #isAccessAllowed.
     * The reason for this is that the authorization flow is usually not unique, so in this way you will
     * have more freedom to customize your authorization flow.
     * @abstract
     */
    var /**
     * A simple guard implementation out of the box. This class should be inherited and
     * implemented by the application. The only method that should be implemented is #isAccessAllowed.
     * The reason for this is that the authorization flow is usually not unique, so in this way you will
     * have more freedom to customize your authorization flow.
     * @abstract
     */ KeycloakAuthGuard = (function () {
        function KeycloakAuthGuard(router, keycloakAngular) {
            this.router = router;
            this.keycloakAngular = keycloakAngular;
        }
        /**
         * CanActivate checks if the user is logged in and get the full list of roles (REALM + CLIENT)
         * of the logged user. This values are set to authenticated and roles params.
         *
         * @param route
         * @param state
         */
        /**
         * CanActivate checks if the user is logged in and get the full list of roles (REALM + CLIENT)
         * of the logged user. This values are set to authenticated and roles params.
         *
         * @param {?} route
         * @param {?} state
         * @return {?}
         */
        KeycloakAuthGuard.prototype.canActivate = /**
         * CanActivate checks if the user is logged in and get the full list of roles (REALM + CLIENT)
         * of the logged user. This values are set to authenticated and roles params.
         *
         * @param {?} route
         * @param {?} state
         * @return {?}
         */
            function (route, state) {
                var _this = this;
                return new Promise(function (resolve, reject) {
                    return __awaiter(_this, void 0, void 0, function () {
                        var _a, _b, result, error_1;
                        return __generator(this, function (_c) {
                            switch (_c.label) {
                                case 0:
                                    _c.trys.push([0, 4, , 5]);
                                    _a = this;
                                    return [4 /*yield*/, this.keycloakAngular.isLoggedIn()];
                                case 1:
                                    _a.authenticated = _c.sent();
                                    _b = this;
                                    return [4 /*yield*/, this.keycloakAngular.getUserRoles(true)];
                                case 2:
                                    _b.roles = _c.sent();
                                    return [4 /*yield*/, this.isAccessAllowed(route, state)];
                                case 3:
                                    result = _c.sent();
                                    resolve(result);
                                    return [3 /*break*/, 5];
                                case 4:
                                    error_1 = _c.sent();
                                    reject('An error happened during access validation. Details:' + error_1);
                                    return [3 /*break*/, 5];
                                case 5: return [2 /*return*/];
                            }
                        });
                    });
                });
            };
        return KeycloakAuthGuard;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes} checked by tsc
     */
    /** @type {?} */
    var Keycloak = Keycloak_;
    /**
     * Service to expose existent methods from the Keycloak JS adapter, adding new
     * functionalities to improve the use of keycloak in Angular v > 4.3 applications.
     *
     * This class should be injected in the application bootstrap, so the same instance will be used
     * along the web application.
     */
    var KeycloakService = (function () {
        function KeycloakService() {
            this._keycloakEvents$ = new rxjs.Subject();
        }
        /**
         * Sanitizes the bearer prefix, preparing it to be appended to
         * the token.
         *
         * @param {?} bearerPrefix
         * Prefix to be appended to the authorization header as
         * Authorization: <bearer-prefix> <token>.
         * @return {?}
         * The bearer prefix sanitized, meaning that it will follow the bearerPrefix
         * param as described in the library initilization or the default value bearer,
         * with a space append in the end for the token concatenation.
         */
        KeycloakService.prototype.sanitizeBearerPrefix = /**
         * Sanitizes the bearer prefix, preparing it to be appended to
         * the token.
         *
         * @param {?} bearerPrefix
         * Prefix to be appended to the authorization header as
         * Authorization: <bearer-prefix> <token>.
         * @return {?}
         * The bearer prefix sanitized, meaning that it will follow the bearerPrefix
         * param as described in the library initilization or the default value bearer,
         * with a space append in the end for the token concatenation.
         */
            function (bearerPrefix) {
                /** @type {?} */
                var prefix = (bearerPrefix || 'bearer').trim();
                return prefix.concat(' ');
            };
        /**
         * Sets default value to true if it is undefined or null.
         *
         * @param {?} value - boolean value to be checked
         * @return {?}
         */
        KeycloakService.prototype.ifUndefinedIsTrue = /**
         * Sets default value to true if it is undefined or null.
         *
         * @param {?} value - boolean value to be checked
         * @return {?}
         */
            function (value) {
                /** @type {?} */
                var returnValue = value;
                if (returnValue === undefined || returnValue === null) {
                    returnValue = true;
                }
                return returnValue;
            };
        /**
         * Binds the keycloak-js events to the keycloakEvents Subject
         * which is a good way to monitor for changes, if needed.
         *
         * The keycloakEvents returns the keycloak-js event type and any
         * argument if the source function provides any.
         * @return {?}
         */
        KeycloakService.prototype.bindsKeycloakEvents = /**
         * Binds the keycloak-js events to the keycloakEvents Subject
         * which is a good way to monitor for changes, if needed.
         *
         * The keycloakEvents returns the keycloak-js event type and any
         * argument if the source function provides any.
         * @return {?}
         */
            function () {
                var _this = this;
                if (!this._instance) {
                    console.warn('Keycloak Angular events could not be registered as the keycloak instance is undefined.');
                    return;
                }
                this._instance.onAuthError = function (errorData) {
                    _this._keycloakEvents$.next({ args: errorData, type: KeycloakEventType.OnAuthError });
                };
                this._instance.onAuthLogout = function () {
                    _this._keycloakEvents$.next({ type: KeycloakEventType.OnAuthLogout });
                };
                this._instance.onAuthRefreshError = function () {
                    _this._keycloakEvents$.next({ type: KeycloakEventType.OnAuthRefreshError });
                };
                this._instance.onAuthSuccess = function () {
                    _this._keycloakEvents$.next({ type: KeycloakEventType.OnAuthSuccess });
                };
                this._instance.onTokenExpired = function () {
                    _this._keycloakEvents$.next({ type: KeycloakEventType.OnTokenExpired });
                };
                this._instance.onReady = function (authenticated) {
                    _this._keycloakEvents$.next({ args: authenticated, type: KeycloakEventType.OnReady });
                };
            };
        /**
         * Keycloak initialization. It should be called to initialize the adapter.
         * Options is a object with 2 main parameters: config and initOptions. The first one
         * will be used to create the Keycloak instance. The second one are options to initialize the
         * keycloak instance.
         *
         * @param options
         * Config: may be a string representing the keycloak URI or an object with the
         * following content:
         * - url: Keycloak json URL
         * - realm: realm name
         * - clientId: client id
         *
         * initOptions:
         * - onLoad: Specifies an action to do on load. Supported values are 'login-required' or
         * 'check-sso'.
         * - token: Set an initial value for the token.
         * - refreshToken: Set an initial value for the refresh token.
         * - idToken: Set an initial value for the id token (only together with token or refreshToken).
         * - timeSkew: Set an initial value for skew between local time and Keycloak server in seconds
         * (only together with token or refreshToken).
         * - checkLoginIframe: Set to enable/disable monitoring login state (default is true).
         * - checkLoginIframeInterval: Set the interval to check login state (default is 5 seconds).
         * - responseMode: Set the OpenID Connect response mode send to Keycloak server at login
         * request. Valid values are query or fragment . Default value is fragment, which means
         * that after successful authentication will Keycloak redirect to javascript application
         * with OpenID Connect parameters added in URL fragment. This is generally safer and
         * recommended over query.
         * - flow: Set the OpenID Connect flow. Valid values are standard, implicit or hybrid.
         *
         * enableBearerInterceptor:
         * Flag to indicate if the bearer will added to the authorization header.
         *
         * loadUserProfileInStartUp:
         * Indicates that the user profile should be loaded at the keycloak initialization,
         * just after the login.
         *
         * bearerExcludedUrls:
         * String Array to exclude the urls that should not have the Authorization Header automatically
         * added.
         *
         * authorizationHeaderName:
         * This value will be used as the Authorization Http Header name.
         *
         * bearerPrefix:
         * This value will be included in the Authorization Http Header param.
         *
         * @returns
         * A Promise with a boolean indicating if the initialization was successful.
         */
        /**
         * Keycloak initialization. It should be called to initialize the adapter.
         * Options is a object with 2 main parameters: config and initOptions. The first one
         * will be used to create the Keycloak instance. The second one are options to initialize the
         * keycloak instance.
         *
         * @param {?=} options
         * Config: may be a string representing the keycloak URI or an object with the
         * following content:
         * - url: Keycloak json URL
         * - realm: realm name
         * - clientId: client id
         *
         * initOptions:
         * - onLoad: Specifies an action to do on load. Supported values are 'login-required' or
         * 'check-sso'.
         * - token: Set an initial value for the token.
         * - refreshToken: Set an initial value for the refresh token.
         * - idToken: Set an initial value for the id token (only together with token or refreshToken).
         * - timeSkew: Set an initial value for skew between local time and Keycloak server in seconds
         * (only together with token or refreshToken).
         * - checkLoginIframe: Set to enable/disable monitoring login state (default is true).
         * - checkLoginIframeInterval: Set the interval to check login state (default is 5 seconds).
         * - responseMode: Set the OpenID Connect response mode send to Keycloak server at login
         * request. Valid values are query or fragment . Default value is fragment, which means
         * that after successful authentication will Keycloak redirect to javascript application
         * with OpenID Connect parameters added in URL fragment. This is generally safer and
         * recommended over query.
         * - flow: Set the OpenID Connect flow. Valid values are standard, implicit or hybrid.
         *
         * enableBearerInterceptor:
         * Flag to indicate if the bearer will added to the authorization header.
         *
         * loadUserProfileInStartUp:
         * Indicates that the user profile should be loaded at the keycloak initialization,
         * just after the login.
         *
         * bearerExcludedUrls:
         * String Array to exclude the urls that should not have the Authorization Header automatically
         * added.
         *
         * authorizationHeaderName:
         * This value will be used as the Authorization Http Header name.
         *
         * bearerPrefix:
         * This value will be included in the Authorization Http Header param.
         *
         * @return {?}
         * A Promise with a boolean indicating if the initialization was successful.
         */
        KeycloakService.prototype.init = /**
         * Keycloak initialization. It should be called to initialize the adapter.
         * Options is a object with 2 main parameters: config and initOptions. The first one
         * will be used to create the Keycloak instance. The second one are options to initialize the
         * keycloak instance.
         *
         * @param {?=} options
         * Config: may be a string representing the keycloak URI or an object with the
         * following content:
         * - url: Keycloak json URL
         * - realm: realm name
         * - clientId: client id
         *
         * initOptions:
         * - onLoad: Specifies an action to do on load. Supported values are 'login-required' or
         * 'check-sso'.
         * - token: Set an initial value for the token.
         * - refreshToken: Set an initial value for the refresh token.
         * - idToken: Set an initial value for the id token (only together with token or refreshToken).
         * - timeSkew: Set an initial value for skew between local time and Keycloak server in seconds
         * (only together with token or refreshToken).
         * - checkLoginIframe: Set to enable/disable monitoring login state (default is true).
         * - checkLoginIframeInterval: Set the interval to check login state (default is 5 seconds).
         * - responseMode: Set the OpenID Connect response mode send to Keycloak server at login
         * request. Valid values are query or fragment . Default value is fragment, which means
         * that after successful authentication will Keycloak redirect to javascript application
         * with OpenID Connect parameters added in URL fragment. This is generally safer and
         * recommended over query.
         * - flow: Set the OpenID Connect flow. Valid values are standard, implicit or hybrid.
         *
         * enableBearerInterceptor:
         * Flag to indicate if the bearer will added to the authorization header.
         *
         * loadUserProfileInStartUp:
         * Indicates that the user profile should be loaded at the keycloak initialization,
         * just after the login.
         *
         * bearerExcludedUrls:
         * String Array to exclude the urls that should not have the Authorization Header automatically
         * added.
         *
         * authorizationHeaderName:
         * This value will be used as the Authorization Http Header name.
         *
         * bearerPrefix:
         * This value will be included in the Authorization Http Header param.
         *
         * @return {?}
         * A Promise with a boolean indicating if the initialization was successful.
         */
            function (options) {
                var _this = this;
                if (options === void 0) {
                    options = {};
                }
                return new Promise(function (resolve, reject) {
                    _this._enableBearerInterceptor = _this.ifUndefinedIsTrue(options.enableBearerInterceptor);
                    _this._loadUserProfileAtStartUp = _this.ifUndefinedIsTrue(options.loadUserProfileAtStartUp);
                    _this._bearerExcludedUrls = options.bearerExcludedUrls || [];
                    _this._authorizationHeaderName = options.authorizationHeaderName || 'Authorization';
                    _this._bearerPrefix = _this.sanitizeBearerPrefix(options.bearerPrefix);
                    _this._silentRefresh = options.initOptions ? options.initOptions.flow === 'implicit' : false;
                    _this._instance = Keycloak(options.config);
                    _this.bindsKeycloakEvents();
                    _this._instance
                        .init(/** @type {?} */ ((options.initOptions)))
                        .success(function (authenticated) {
                        return __awaiter(_this, void 0, void 0, function () {
                            return __generator(this, function (_a) {
                                switch (_a.label) {
                                    case 0:
                                        if (!(authenticated && this._loadUserProfileAtStartUp))
                                            return [3 /*break*/, 2];
                                        return [4 /*yield*/, this.loadUserProfile()];
                                    case 1:
                                        _a.sent();
                                        _a.label = 2;
                                    case 2:
                                        resolve(authenticated);
                                        return [2 /*return*/];
                                }
                            });
                        });
                    })
                        .error(function (error) {
                        reject('An error happened during Keycloak initialization.');
                    });
                });
            };
        /**
         * Redirects to login form on (options is an optional object with redirectUri and/or
         * prompt fields).
         *
         * @param options
         * Object, where:
         *  - redirectUri: Specifies the uri to redirect to after login.
         *  - prompt:By default the login screen is displayed if the user is not logged-in to Keycloak.
         * To only authenticate to the application if the user is already logged-in and not display the
         * login page if the user is not logged-in, set this option to none. To always require
         * re-authentication and ignore SSO, set this option to login .
         *  - maxAge: Used just if user is already authenticated. Specifies maximum time since the
         * authentication of user happened. If user is already authenticated for longer time than
         * maxAge, the SSO is ignored and he will need to re-authenticate again.
         *  - loginHint: Used to pre-fill the username/email field on the login form.
         *  - action: If value is 'register' then user is redirected to registration page, otherwise to
         * login page.
         *  - locale: Specifies the desired locale for the UI.
         * @returns
         * A void Promise if the login is successful and after the user profile loading.
         */
        /**
         * Redirects to login form on (options is an optional object with redirectUri and/or
         * prompt fields).
         *
         * @param {?=} options
         * Object, where:
         *  - redirectUri: Specifies the uri to redirect to after login.
         *  - prompt:By default the login screen is displayed if the user is not logged-in to Keycloak.
         * To only authenticate to the application if the user is already logged-in and not display the
         * login page if the user is not logged-in, set this option to none. To always require
         * re-authentication and ignore SSO, set this option to login .
         *  - maxAge: Used just if user is already authenticated. Specifies maximum time since the
         * authentication of user happened. If user is already authenticated for longer time than
         * maxAge, the SSO is ignored and he will need to re-authenticate again.
         *  - loginHint: Used to pre-fill the username/email field on the login form.
         *  - action: If value is 'register' then user is redirected to registration page, otherwise to
         * login page.
         *  - locale: Specifies the desired locale for the UI.
         * @return {?}
         * A void Promise if the login is successful and after the user profile loading.
         */
        KeycloakService.prototype.login = /**
         * Redirects to login form on (options is an optional object with redirectUri and/or
         * prompt fields).
         *
         * @param {?=} options
         * Object, where:
         *  - redirectUri: Specifies the uri to redirect to after login.
         *  - prompt:By default the login screen is displayed if the user is not logged-in to Keycloak.
         * To only authenticate to the application if the user is already logged-in and not display the
         * login page if the user is not logged-in, set this option to none. To always require
         * re-authentication and ignore SSO, set this option to login .
         *  - maxAge: Used just if user is already authenticated. Specifies maximum time since the
         * authentication of user happened. If user is already authenticated for longer time than
         * maxAge, the SSO is ignored and he will need to re-authenticate again.
         *  - loginHint: Used to pre-fill the username/email field on the login form.
         *  - action: If value is 'register' then user is redirected to registration page, otherwise to
         * login page.
         *  - locale: Specifies the desired locale for the UI.
         * @return {?}
         * A void Promise if the login is successful and after the user profile loading.
         */
            function (options) {
                var _this = this;
                if (options === void 0) {
                    options = {};
                }
                return new Promise(function (resolve, reject) {
                    _this._instance
                        .login(options)
                        .success(function () {
                        return __awaiter(_this, void 0, void 0, function () {
                            return __generator(this, function (_a) {
                                switch (_a.label) {
                                    case 0:
                                        if (!this._loadUserProfileAtStartUp)
                                            return [3 /*break*/, 2];
                                        return [4 /*yield*/, this.loadUserProfile()];
                                    case 1:
                                        _a.sent();
                                        _a.label = 2;
                                    case 2:
                                        resolve();
                                        return [2 /*return*/];
                                }
                            });
                        });
                    })
                        .error(function (error) {
                        reject('An error happened during the login.');
                    });
                });
            };
        /**
         * Redirects to logout.
         *
         * @param redirectUri
         * Specifies the uri to redirect to after logout.
         * @returns
         * A void Promise if the logout was successful, cleaning also the userProfile.
         */
        /**
         * Redirects to logout.
         *
         * @param {?=} redirectUri
         * Specifies the uri to redirect to after logout.
         * @return {?}
         * A void Promise if the logout was successful, cleaning also the userProfile.
         */
        KeycloakService.prototype.logout = /**
         * Redirects to logout.
         *
         * @param {?=} redirectUri
         * Specifies the uri to redirect to after logout.
         * @return {?}
         * A void Promise if the logout was successful, cleaning also the userProfile.
         */
            function (redirectUri) {
                var _this = this;
                return new Promise(function (resolve, reject) {
                    /** @type {?} */
                    var options = {
                        redirectUri: redirectUri
                    };
                    _this._instance
                        .logout(options)
                        .success(function () {
                        _this._userProfile = /** @type {?} */ ((undefined));
                        resolve();
                    })
                        .error(function (error) {
                        reject('An error happened during logout.');
                    });
                });
            };
        /**
         * Redirects to registration form. Shortcut for login with option
         * action = 'register'. Options are same as for the login method but 'action' is set to
         * 'register'.
         *
         * @param options
         * login options
         * @returns
         * A void Promise if the register flow was successful.
         */
        /**
         * Redirects to registration form. Shortcut for login with option
         * action = 'register'. Options are same as for the login method but 'action' is set to
         * 'register'.
         *
         * @param {?=} options
         * login options
         * @return {?}
         * A void Promise if the register flow was successful.
         */
        KeycloakService.prototype.register = /**
         * Redirects to registration form. Shortcut for login with option
         * action = 'register'. Options are same as for the login method but 'action' is set to
         * 'register'.
         *
         * @param {?=} options
         * login options
         * @return {?}
         * A void Promise if the register flow was successful.
         */
            function (options) {
                var _this = this;
                if (options === void 0) {
                    options = { action: 'register' };
                }
                return new Promise(function (resolve, reject) {
                    _this._instance
                        .register(options)
                        .success(function () {
                        resolve();
                    })
                        .error(function () {
                        reject('An error happened during the register execution');
                    });
                });
            };
        /**
         * Check if the user has access to the specified role. It will look for roles in
         * realm and clientId, but will not check if the user is logged in for better performance.
         *
         * @param role
         * role name
         * @returns
         * A boolean meaning if the user has the specified Role.
         */
        /**
         * Check if the user has access to the specified role. It will look for roles in
         * realm and clientId, but will not check if the user is logged in for better performance.
         *
         * @param {?} role
         * role name
         * @return {?}
         * A boolean meaning if the user has the specified Role.
         */
        KeycloakService.prototype.isUserInRole = /**
         * Check if the user has access to the specified role. It will look for roles in
         * realm and clientId, but will not check if the user is logged in for better performance.
         *
         * @param {?} role
         * role name
         * @return {?}
         * A boolean meaning if the user has the specified Role.
         */
            function (role) {
                /** @type {?} */
                var hasRole;
                hasRole = this._instance.hasResourceRole(role);
                if (!hasRole) {
                    hasRole = this._instance.hasRealmRole(role);
                }
                return hasRole;
            };
        /**
         * Return the roles of the logged user. The allRoles parameter, with default value
         * true, will return the clientId and realm roles associated with the logged user. If set to false
         * it will only return the user roles associated with the clientId.
         *
         * @param allRoles
         * Flag to set if all roles should be returned.(Optional: default value is true)
         * @returns
         * Array of Roles associated with the logged user.
         */
        /**
         * Return the roles of the logged user. The allRoles parameter, with default value
         * true, will return the clientId and realm roles associated with the logged user. If set to false
         * it will only return the user roles associated with the clientId.
         *
         * @param {?=} allRoles
         * Flag to set if all roles should be returned.(Optional: default value is true)
         * @return {?}
         * Array of Roles associated with the logged user.
         */
        KeycloakService.prototype.getUserRoles = /**
         * Return the roles of the logged user. The allRoles parameter, with default value
         * true, will return the clientId and realm roles associated with the logged user. If set to false
         * it will only return the user roles associated with the clientId.
         *
         * @param {?=} allRoles
         * Flag to set if all roles should be returned.(Optional: default value is true)
         * @return {?}
         * Array of Roles associated with the logged user.
         */
            function (allRoles) {
                if (allRoles === void 0) {
                    allRoles = true;
                }
                /** @type {?} */
                var roles = [];
                if (this._instance.resourceAccess) {
                    for (var key in this._instance.resourceAccess) {
                        if (this._instance.resourceAccess.hasOwnProperty(key)) {
                            /** @type {?} */
                            var resourceAccess = this._instance.resourceAccess[key];
                            /** @type {?} */
                            var clientRoles = resourceAccess['roles'] || [];
                            roles = roles.concat(clientRoles);
                        }
                    }
                }
                if (allRoles && this._instance.realmAccess) {
                    /** @type {?} */
                    var realmRoles = this._instance.realmAccess['roles'] || [];
                    roles.push.apply(roles, __spread(realmRoles));
                }
                return roles;
            };
        /**
         * Check if user is logged in.
         *
         * @returns
         * A boolean that indicates if the user is logged in.
         */
        /**
         * Check if user is logged in.
         *
         * @return {?}
         * A boolean that indicates if the user is logged in.
         */
        KeycloakService.prototype.isLoggedIn = /**
         * Check if user is logged in.
         *
         * @return {?}
         * A boolean that indicates if the user is logged in.
         */
            function () {
                var _this = this;
                return new Promise(function (resolve, reject) {
                    return __awaiter(_this, void 0, void 0, function () {
                        var error_1;
                        return __generator(this, function (_a) {
                            switch (_a.label) {
                                case 0:
                                    _a.trys.push([0, 2, , 3]);
                                    return [4 /*yield*/, this.updateToken(20)];
                                case 1:
                                    _a.sent();
                                    resolve(true);
                                    return [3 /*break*/, 3];
                                case 2:
                                    error_1 = _a.sent();
                                    resolve(false);
                                    return [3 /*break*/, 3];
                                case 3: return [2 /*return*/];
                            }
                        });
                    });
                });
            };
        /**
         * Returns true if the token has less than minValidity seconds left before
         * it expires.
         *
         * @param minValidity
         * Seconds left. (minValidity) is optional. Default value is 0.
         * @returns
         * Boolean indicating if the token is expired.
         */
        /**
         * Returns true if the token has less than minValidity seconds left before
         * it expires.
         *
         * @param {?=} minValidity
         * Seconds left. (minValidity) is optional. Default value is 0.
         * @return {?}
         * Boolean indicating if the token is expired.
         */
        KeycloakService.prototype.isTokenExpired = /**
         * Returns true if the token has less than minValidity seconds left before
         * it expires.
         *
         * @param {?=} minValidity
         * Seconds left. (minValidity) is optional. Default value is 0.
         * @return {?}
         * Boolean indicating if the token is expired.
         */
            function (minValidity) {
                if (minValidity === void 0) {
                    minValidity = 0;
                }
                return this._instance.isTokenExpired(minValidity);
            };
        /**
         * If the token expires within minValidity seconds the token is refreshed. If the
         * session status iframe is enabled, the session status is also checked.
         * Returns a promise telling if the token was refreshed or not. If the session is not active
         * anymore, the promise is rejected.
         *
         * @param minValidity
         * Seconds left. (minValidity is optional, if not specified 5 is used)
         * @returns
         * Promise with a boolean indicating if the token was succesfully updated.
         */
        /**
         * If the token expires within minValidity seconds the token is refreshed. If the
         * session status iframe is enabled, the session status is also checked.
         * Returns a promise telling if the token was refreshed or not. If the session is not active
         * anymore, the promise is rejected.
         *
         * @param {?=} minValidity
         * Seconds left. (minValidity is optional, if not specified 5 is used)
         * @return {?}
         * Promise with a boolean indicating if the token was succesfully updated.
         */
        KeycloakService.prototype.updateToken = /**
         * If the token expires within minValidity seconds the token is refreshed. If the
         * session status iframe is enabled, the session status is also checked.
         * Returns a promise telling if the token was refreshed or not. If the session is not active
         * anymore, the promise is rejected.
         *
         * @param {?=} minValidity
         * Seconds left. (minValidity is optional, if not specified 5 is used)
         * @return {?}
         * Promise with a boolean indicating if the token was succesfully updated.
         */
            function (minValidity) {
                var _this = this;
                if (minValidity === void 0) {
                    minValidity = 5;
                }
                return new Promise(function (resolve, reject) {
                    return __awaiter(_this, void 0, void 0, function () {
                        return __generator(this, function (_a) {
                            // TODO: this is a workaround until the silent refresh (issue #43)
                            // is not implemented, avoiding the redirect loop.
                            if (this._silentRefresh) {
                                if (this.isTokenExpired()) {
                                    reject('Failed to refresh the token, or the session is expired');
                                }
                                else {
                                    resolve(true);
                                }
                                return [2 /*return*/];
                            }
                            if (!this._instance) {
                                reject();
                                return [2 /*return*/];
                            }
                            this._instance
                                .updateToken(minValidity)
                                .success(function (refreshed) {
                                resolve(refreshed);
                            })
                                .error(function (error) {
                                reject('Failed to refresh the token, or the session is expired');
                            });
                            return [2 /*return*/];
                        });
                    });
                });
            };
        /**
         * Loads the users profile.
         * Returns promise to set functions to be invoked if the profile was loaded successfully, or if
         * the profile could not be loaded.
         *
         * @param forceReload
         * If true will force the loadUserProfile even if its already loaded.
         * @returns
         * A promise with the KeycloakProfile data loaded.
         */
        /**
         * Loads the users profile.
         * Returns promise to set functions to be invoked if the profile was loaded successfully, or if
         * the profile could not be loaded.
         *
         * @param {?=} forceReload
         * If true will force the loadUserProfile even if its already loaded.
         * @return {?}
         * A promise with the KeycloakProfile data loaded.
         */
        KeycloakService.prototype.loadUserProfile = /**
         * Loads the users profile.
         * Returns promise to set functions to be invoked if the profile was loaded successfully, or if
         * the profile could not be loaded.
         *
         * @param {?=} forceReload
         * If true will force the loadUserProfile even if its already loaded.
         * @return {?}
         * A promise with the KeycloakProfile data loaded.
         */
            function (forceReload) {
                var _this = this;
                if (forceReload === void 0) {
                    forceReload = false;
                }
                return new Promise(function (resolve, reject) {
                    return __awaiter(_this, void 0, void 0, function () {
                        var _this = this;
                        return __generator(this, function (_a) {
                            switch (_a.label) {
                                case 0:
                                    if (this._userProfile && !forceReload) {
                                        resolve(this._userProfile);
                                        return [2 /*return*/];
                                    }
                                    return [4 /*yield*/, this.isLoggedIn()];
                                case 1:
                                    if (!(_a.sent())) {
                                        reject('The user profile was not loaded as the user is not logged in.');
                                        return [2 /*return*/];
                                    }
                                    this._instance
                                        .loadUserProfile()
                                        .success(function (result) {
                                        _this._userProfile = /** @type {?} */ (result);
                                        resolve(_this._userProfile);
                                    })
                                        .error(function (err) {
                                        reject('The user profile could not be loaded.');
                                    });
                                    return [2 /*return*/];
                            }
                        });
                    });
                });
            };
        /**
         * Returns the authenticated token, calling updateToken to get a refreshed one if
         * necessary. If the session is expired this method calls the login method for a new login.
         *
         * @returns
         * Promise with the generated token.
         */
        /**
         * Returns the authenticated token, calling updateToken to get a refreshed one if
         * necessary. If the session is expired this method calls the login method for a new login.
         *
         * @return {?}
         * Promise with the generated token.
         */
        KeycloakService.prototype.getToken = /**
         * Returns the authenticated token, calling updateToken to get a refreshed one if
         * necessary. If the session is expired this method calls the login method for a new login.
         *
         * @return {?}
         * Promise with the generated token.
         */
            function () {
                var _this = this;
                return new Promise(function (resolve, reject) {
                    return __awaiter(_this, void 0, void 0, function () {
                        var error_2;
                        return __generator(this, function (_a) {
                            switch (_a.label) {
                                case 0:
                                    _a.trys.push([0, 2, , 3]);
                                    return [4 /*yield*/, this.updateToken(10)];
                                case 1:
                                    _a.sent();
                                    resolve(this._instance.token);
                                    return [3 /*break*/, 3];
                                case 2:
                                    error_2 = _a.sent();
                                    this.login();
                                    return [3 /*break*/, 3];
                                case 3: return [2 /*return*/];
                            }
                        });
                    });
                });
            };
        /**
         * Returns the logged username.
         *
         * @returns
         * The logged username.
         */
        /**
         * Returns the logged username.
         *
         * @return {?}
         * The logged username.
         */
        KeycloakService.prototype.getUsername = /**
         * Returns the logged username.
         *
         * @return {?}
         * The logged username.
         */
            function () {
                if (!this._userProfile) {
                    throw new Error('User not logged in or user profile was not loaded.');
                }
                return /** @type {?} */ ((this._userProfile.username));
            };
        /**
         * Clear authentication state, including tokens. This can be useful if application
         * has detected the session was expired, for example if updating token fails.
         * Invoking this results in onAuthLogout callback listener being invoked.
         */
        /**
         * Clear authentication state, including tokens. This can be useful if application
         * has detected the session was expired, for example if updating token fails.
         * Invoking this results in onAuthLogout callback listener being invoked.
         * @return {?}
         */
        KeycloakService.prototype.clearToken = /**
         * Clear authentication state, including tokens. This can be useful if application
         * has detected the session was expired, for example if updating token fails.
         * Invoking this results in onAuthLogout callback listener being invoked.
         * @return {?}
         */
            function () {
                this._instance.clearToken();
            };
        /**
         * Adds a valid token in header. The key & value format is:
         * Authorization Bearer <token>.
         * If the headers param is undefined it will create the Angular headers object.
         *
         * @param headers
         * Updated header with Authorization and Keycloak token.
         * @returns
         * An observable with with the HTTP Authorization header and the current token.
         */
        /**
         * Adds a valid token in header. The key & value format is:
         * Authorization Bearer <token>.
         * If the headers param is undefined it will create the Angular headers object.
         *
         * @param {?=} headersArg
         * @return {?}
         * An observable with with the HTTP Authorization header and the current token.
         */
        KeycloakService.prototype.addTokenToHeader = /**
         * Adds a valid token in header. The key & value format is:
         * Authorization Bearer <token>.
         * If the headers param is undefined it will create the Angular headers object.
         *
         * @param {?=} headersArg
         * @return {?}
         * An observable with with the HTTP Authorization header and the current token.
         */
            function (headersArg) {
                var _this = this;
                return rxjs.Observable.create(function (observer) {
                    return __awaiter(_this, void 0, void 0, function () {
                        var headers, token, error_3;
                        return __generator(this, function (_a) {
                            switch (_a.label) {
                                case 0:
                                    headers = headersArg;
                                    if (!headers) {
                                        headers = new http.HttpHeaders();
                                    }
                                    _a.label = 1;
                                case 1:
                                    _a.trys.push([1, 3, , 4]);
                                    return [4 /*yield*/, this.getToken()];
                                case 2:
                                    token = _a.sent();
                                    headers = headers.set(this._authorizationHeaderName, this._bearerPrefix + token);
                                    observer.next(headers);
                                    observer.complete();
                                    return [3 /*break*/, 4];
                                case 3:
                                    error_3 = _a.sent();
                                    observer.error(error_3);
                                    return [3 /*break*/, 4];
                                case 4: return [2 /*return*/];
                            }
                        });
                    });
                });
            };
        /**
         * Returns the original Keycloak instance, if you need any customization that
         * this Angular service does not support yet. Use with caution.
         *
         * @returns
         * The KeycloakInstance from keycloak-js.
         */
        /**
         * Returns the original Keycloak instance, if you need any customization that
         * this Angular service does not support yet. Use with caution.
         *
         * @return {?}
         * The KeycloakInstance from keycloak-js.
         */
        KeycloakService.prototype.getKeycloakInstance = /**
         * Returns the original Keycloak instance, if you need any customization that
         * this Angular service does not support yet. Use with caution.
         *
         * @return {?}
         * The KeycloakInstance from keycloak-js.
         */
            function () {
                return this._instance;
            };
        Object.defineProperty(KeycloakService.prototype, "bearerExcludedUrls", {
            /**
             * Returns the excluded URLs that should not be considered by
             * the http interceptor which automatically adds the authorization header in the Http Request.
             *
             * @returns
             * The excluded urls that must not be intercepted by the KeycloakBearerInterceptor.
             */
            get: /**
             * Returns the excluded URLs that should not be considered by
             * the http interceptor which automatically adds the authorization header in the Http Request.
             *
             * @return {?}
             * The excluded urls that must not be intercepted by the KeycloakBearerInterceptor.
             */ function () {
                return this._bearerExcludedUrls;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(KeycloakService.prototype, "enableBearerInterceptor", {
            /**
             * Flag to indicate if the bearer will be added to the authorization header.
             *
             * @returns
             * Returns if the bearer interceptor was set to be disabled.
             */
            get: /**
             * Flag to indicate if the bearer will be added to the authorization header.
             *
             * @return {?}
             * Returns if the bearer interceptor was set to be disabled.
             */ function () {
                return this._enableBearerInterceptor;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(KeycloakService.prototype, "keycloakEvents$", {
            /**
             * Keycloak subject to monitor the events triggered by keycloak-js.
             * The following events as available (as described at keycloak docs -
             * https://www.keycloak.org/docs/latest/securing_apps/index.html#callback-events):
             * - OnAuthError
             * - OnAuthLogout
             * - OnAuthRefreshError
             * - OnAuthRefreshSuccess
             * - OnAuthSuccess
             * - OnReady
             * - OnTokenExpire
             * In each occurrence of any of these, this subject will return the event type,
             * described at {@link KeycloakEventType} enum and the function args from the keycloak-js
             * if provided any.
             *
             * @returns
             * A subject with the {@link KeycloakEvent} which describes the event type and attaches the
             * function args.
             */
            get: /**
             * Keycloak subject to monitor the events triggered by keycloak-js.
             * The following events as available (as described at keycloak docs -
             * https://www.keycloak.org/docs/latest/securing_apps/index.html#callback-events):
             * - OnAuthError
             * - OnAuthLogout
             * - OnAuthRefreshError
             * - OnAuthRefreshSuccess
             * - OnAuthSuccess
             * - OnReady
             * - OnTokenExpire
             * In each occurrence of any of these, this subject will return the event type,
             * described at {\@link KeycloakEventType} enum and the function args from the keycloak-js
             * if provided any.
             *
             * @return {?}
             * A subject with the {\@link KeycloakEvent} which describes the event type and attaches the
             * function args.
             */ function () {
                return this._keycloakEvents$;
            },
            enumerable: true,
            configurable: true
        });
        KeycloakService.decorators = [
            { type: core.Injectable },
        ];
        /** @nocollapse */
        KeycloakService.ctorParameters = function () { return []; };
        return KeycloakService;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes} checked by tsc
     */
    /**
     * This interceptor includes the bearer by default in all HttpClient requests.
     *
     * If you need to exclude some URLs from adding the bearer, please, take a look
     * at the {\@link KeycloakOptions} bearerExcludedUrls property.
     */
    var KeycloakBearerInterceptor = (function () {
        /**
         * KeycloakBearerInterceptor constructor.
         *
         * @param keycloak - Injected KeycloakService instance.
         */
        function KeycloakBearerInterceptor(keycloak) {
            this.keycloak = keycloak;
        }
        /**
         * @return {?}
         */
        KeycloakBearerInterceptor.prototype.loadExcludedUrlsRegex = /**
         * @return {?}
         */
            function () {
                /** @type {?} */
                var excludedUrls = this.keycloak.bearerExcludedUrls;
                this.excludedUrlsRegex = excludedUrls.map(function (urlPattern) { return new RegExp(urlPattern, 'i'); }) || [];
            };
        /**
         * Intercept implementation that checks if the request url matches the excludedUrls.
         * If not, adds the Authorization header to the request.
         *
         * @param {?} req
         * @param {?} next
         * @return {?}
         */
        KeycloakBearerInterceptor.prototype.intercept = /**
         * Intercept implementation that checks if the request url matches the excludedUrls.
         * If not, adds the Authorization header to the request.
         *
         * @param {?} req
         * @param {?} next
         * @return {?}
         */
            function (req, next) {
                // If keycloak service is not initialized yet, or the interceptor should not be execute
                if (!this.keycloak || !this.keycloak.enableBearerInterceptor) {
                    return next.handle(req);
                }
                if (!this.excludedUrlsRegex) {
                    this.loadExcludedUrlsRegex();
                }
                /** @type {?} */
                var urlRequest = req.url;
                /** @type {?} */
                var shallPass = !!this.excludedUrlsRegex.find(function (regex) { return regex.test(urlRequest); });
                if (shallPass) {
                    return next.handle(req);
                }
                return this.keycloak.addTokenToHeader(req.headers).pipe(operators.mergeMap(function (headersWithBearer) {
                    /** @type {?} */
                    var kcReq = req.clone({ headers: headersWithBearer });
                    return next.handle(kcReq);
                }));
            };
        KeycloakBearerInterceptor.decorators = [
            { type: core.Injectable },
        ];
        /** @nocollapse */
        KeycloakBearerInterceptor.ctorParameters = function () {
            return [
                { type: KeycloakService }
            ];
        };
        return KeycloakBearerInterceptor;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes} checked by tsc
     */
    var CoreModule = (function () {
        function CoreModule() {
        }
        CoreModule.decorators = [
            { type: core.NgModule, args: [{
                        imports: [common.CommonModule],
                        providers: [
                            KeycloakService,
                            {
                                provide: http.HTTP_INTERCEPTORS,
                                useClass: KeycloakBearerInterceptor,
                                multi: true
                            }
                        ]
                    },] },
        ];
        return CoreModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes} checked by tsc
     */
    var KeycloakAngularModule = (function () {
        function KeycloakAngularModule() {
        }
        KeycloakAngularModule.decorators = [
            { type: core.NgModule, args: [{
                        imports: [CoreModule]
                    },] },
        ];
        return KeycloakAngularModule;
    }());

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes} checked by tsc
     */

    /**
     * @fileoverview added by tsickle
     * @suppress {checkTypes} checked by tsc
     */

    exports.KeycloakEventType = KeycloakEventType;
    exports.KeycloakAuthGuard = KeycloakAuthGuard;
    exports.KeycloakService = KeycloakService;
    exports.Keycloak = Keycloak;
    exports.KeycloakBearerInterceptor = KeycloakBearerInterceptor;
    exports.CoreModule = CoreModule;
    exports.KeycloakAngularModule = KeycloakAngularModule;

    Object.defineProperty(exports, '__esModule', { value: true });

})));

//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWstYW5ndWxhci51bWQuanMubWFwIiwic291cmNlcyI6WyJuZzovL2tleWNsb2FrLWFuZ3VsYXIvbGliL2NvcmUvaW50ZXJmYWNlcy9rZXljbG9hay1ldmVudC50cyIsbnVsbCwibmc6Ly9rZXljbG9hay1hbmd1bGFyL2xpYi9jb3JlL3NlcnZpY2VzL2tleWNsb2FrLWF1dGgtZ3VhcmQudHMiLCJuZzovL2tleWNsb2FrLWFuZ3VsYXIvbGliL2NvcmUvc2VydmljZXMva2V5Y2xvYWsuc2VydmljZS50cyIsIm5nOi8va2V5Y2xvYWstYW5ndWxhci9saWIvY29yZS9pbnRlcmNlcHRvcnMva2V5Y2xvYWstYmVhcmVyLmludGVyY2VwdG9yLnRzIiwibmc6Ly9rZXljbG9hay1hbmd1bGFyL2xpYi9jb3JlL2NvcmUubW9kdWxlLnRzIiwibmc6Ly9rZXljbG9hay1hbmd1bGFyL2xpYi9rZXljbG9hay1hbmd1bGFyLm1vZHVsZS50cyJdLCJzb3VyY2VzQ29udGVudCI6WyIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgTWF1cmljaW8gR2VtZWxsaSBWaWdvbG8gYW5kIGNvbnRyaWJ1dG9ycy5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vZ2l0aHViLmNvbS9tYXVyaWNpb3ZpZ29sby9rZXljbG9hay1hbmd1bGFyL0xJQ0VOU0VcbiAqL1xuXG4vKipcbiAqIEtleWNsb2FrIGV2ZW50IHR5cGVzLCBhcyBkZXNjcmliZWQgYXQgdGhlIGtleWNsb2FrLWpzIGRvY3VtZW50YXRpb246XG4gKiBodHRwczovL3d3dy5rZXljbG9hay5vcmcvZG9jcy9sYXRlc3Qvc2VjdXJpbmdfYXBwcy9pbmRleC5odG1sI2NhbGxiYWNrLWV2ZW50c1xuICovXG5leHBvcnQgZW51bSBLZXljbG9ha0V2ZW50VHlwZSB7XG4gIC8qKlxuICAgKiBDYWxsZWQgaWYgdGhlcmUgd2FzIGFuIGVycm9yIGR1cmluZyBhdXRoZW50aWNhdGlvbi5cbiAgICovXG4gIE9uQXV0aEVycm9yLFxuICAvKipcbiAgICogQ2FsbGVkIGlmIHRoZSB1c2VyIGlzIGxvZ2dlZCBvdXRcbiAgICogKHdpbGwgb25seSBiZSBjYWxsZWQgaWYgdGhlIHNlc3Npb24gc3RhdHVzIGlmcmFtZSBpcyBlbmFibGVkLCBvciBpbiBDb3Jkb3ZhIG1vZGUpLlxuICAgKi9cbiAgT25BdXRoTG9nb3V0LFxuICAvKipcbiAgICogQ2FsbGVkIGlmIHRoZXJlIHdhcyBhbiBlcnJvciB3aGlsZSB0cnlpbmcgdG8gcmVmcmVzaCB0aGUgdG9rZW4uXG4gICAqL1xuICBPbkF1dGhSZWZyZXNoRXJyb3IsXG4gIC8qKlxuICAgKiBDYWxsZWQgd2hlbiB0aGUgdG9rZW4gaXMgcmVmcmVzaGVkLlxuICAgKi9cbiAgT25BdXRoUmVmcmVzaFN1Y2Nlc3MsXG4gIC8qKlxuICAgKiBDYWxsZWQgd2hlbiBhIHVzZXIgaXMgc3VjY2Vzc2Z1bGx5IGF1dGhlbnRpY2F0ZWQuXG4gICAqL1xuICBPbkF1dGhTdWNjZXNzLFxuICAvKipcbiAgICogQ2FsbGVkIHdoZW4gdGhlIGFkYXB0ZXIgaXMgaW5pdGlhbGl6ZWQuXG4gICAqL1xuICBPblJlYWR5LFxuICAvKipcbiAgICogQ2FsbGVkIHdoZW4gdGhlIGFjY2VzcyB0b2tlbiBpcyBleHBpcmVkLiBJZiBhIHJlZnJlc2ggdG9rZW4gaXMgYXZhaWxhYmxlIHRoZSB0b2tlblxuICAgKiBjYW4gYmUgcmVmcmVzaGVkIHdpdGggdXBkYXRlVG9rZW4sIG9yIGluIGNhc2VzIHdoZXJlIGl0IGlzIG5vdCAodGhhdCBpcywgd2l0aCBpbXBsaWNpdCBmbG93KVxuICAgKiB5b3UgY2FuIHJlZGlyZWN0IHRvIGxvZ2luIHNjcmVlbiB0byBvYnRhaW4gYSBuZXcgYWNjZXNzIHRva2VuLlxuICAgKi9cbiAgT25Ub2tlbkV4cGlyZWRcbn1cblxuLyoqXG4gKiBTdHJ1Y3R1cmUgb2YgYW4gZXZlbnQgdHJpZ2dlcmVkIGJ5IEtleWNsb2FrLCBjb250YWlucyBpdCdzIHR5cGVcbiAqIGFuZCBhcmd1bWVudHMgKGlmIGFueSkuXG4gKi9cbmV4cG9ydCBpbnRlcmZhY2UgS2V5Y2xvYWtFdmVudCB7XG4gIC8qKlxuICAgKiBFdmVudCB0eXBlIGFzIGRlc2NyaWJlZCBhdCB7QGxpbmsgS2V5Y2xvYWtFdmVudFR5cGV9LlxuICAgKi9cbiAgdHlwZTogS2V5Y2xvYWtFdmVudFR5cGU7XG4gIC8qKlxuICAgKiBBcmd1bWVudHMgZnJvbSB0aGUga2V5Y2xvYWstanMgZXZlbnQgZnVuY3Rpb24uXG4gICAqL1xuICBhcmdzPzogYW55O1xufVxuIiwiLyohICoqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqXHJcbkNvcHlyaWdodCAoYykgTWljcm9zb2Z0IENvcnBvcmF0aW9uLiBBbGwgcmlnaHRzIHJlc2VydmVkLlxyXG5MaWNlbnNlZCB1bmRlciB0aGUgQXBhY2hlIExpY2Vuc2UsIFZlcnNpb24gMi4wICh0aGUgXCJMaWNlbnNlXCIpOyB5b3UgbWF5IG5vdCB1c2VcclxudGhpcyBmaWxlIGV4Y2VwdCBpbiBjb21wbGlhbmNlIHdpdGggdGhlIExpY2Vuc2UuIFlvdSBtYXkgb2J0YWluIGEgY29weSBvZiB0aGVcclxuTGljZW5zZSBhdCBodHRwOi8vd3d3LmFwYWNoZS5vcmcvbGljZW5zZXMvTElDRU5TRS0yLjBcclxuXHJcblRISVMgQ09ERSBJUyBQUk9WSURFRCBPTiBBTiAqQVMgSVMqIEJBU0lTLCBXSVRIT1VUIFdBUlJBTlRJRVMgT1IgQ09ORElUSU9OUyBPRiBBTllcclxuS0lORCwgRUlUSEVSIEVYUFJFU1MgT1IgSU1QTElFRCwgSU5DTFVESU5HIFdJVEhPVVQgTElNSVRBVElPTiBBTlkgSU1QTElFRFxyXG5XQVJSQU5USUVTIE9SIENPTkRJVElPTlMgT0YgVElUTEUsIEZJVE5FU1MgRk9SIEEgUEFSVElDVUxBUiBQVVJQT1NFLFxyXG5NRVJDSEFOVEFCTElUWSBPUiBOT04tSU5GUklOR0VNRU5ULlxyXG5cclxuU2VlIHRoZSBBcGFjaGUgVmVyc2lvbiAyLjAgTGljZW5zZSBmb3Igc3BlY2lmaWMgbGFuZ3VhZ2UgZ292ZXJuaW5nIHBlcm1pc3Npb25zXHJcbmFuZCBsaW1pdGF0aW9ucyB1bmRlciB0aGUgTGljZW5zZS5cclxuKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKiogKi9cclxuLyogZ2xvYmFsIFJlZmxlY3QsIFByb21pc2UgKi9cclxuXHJcbnZhciBleHRlbmRTdGF0aWNzID0gZnVuY3Rpb24oZCwgYikge1xyXG4gICAgZXh0ZW5kU3RhdGljcyA9IE9iamVjdC5zZXRQcm90b3R5cGVPZiB8fFxyXG4gICAgICAgICh7IF9fcHJvdG9fXzogW10gfSBpbnN0YW5jZW9mIEFycmF5ICYmIGZ1bmN0aW9uIChkLCBiKSB7IGQuX19wcm90b19fID0gYjsgfSkgfHxcclxuICAgICAgICBmdW5jdGlvbiAoZCwgYikgeyBmb3IgKHZhciBwIGluIGIpIGlmIChiLmhhc093blByb3BlcnR5KHApKSBkW3BdID0gYltwXTsgfTtcclxuICAgIHJldHVybiBleHRlbmRTdGF0aWNzKGQsIGIpO1xyXG59O1xyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9fZXh0ZW5kcyhkLCBiKSB7XHJcbiAgICBleHRlbmRTdGF0aWNzKGQsIGIpO1xyXG4gICAgZnVuY3Rpb24gX18oKSB7IHRoaXMuY29uc3RydWN0b3IgPSBkOyB9XHJcbiAgICBkLnByb3RvdHlwZSA9IGIgPT09IG51bGwgPyBPYmplY3QuY3JlYXRlKGIpIDogKF9fLnByb3RvdHlwZSA9IGIucHJvdG90eXBlLCBuZXcgX18oKSk7XHJcbn1cclxuXHJcbmV4cG9ydCB2YXIgX19hc3NpZ24gPSBmdW5jdGlvbigpIHtcclxuICAgIF9fYXNzaWduID0gT2JqZWN0LmFzc2lnbiB8fCBmdW5jdGlvbiBfX2Fzc2lnbih0KSB7XHJcbiAgICAgICAgZm9yICh2YXIgcywgaSA9IDEsIG4gPSBhcmd1bWVudHMubGVuZ3RoOyBpIDwgbjsgaSsrKSB7XHJcbiAgICAgICAgICAgIHMgPSBhcmd1bWVudHNbaV07XHJcbiAgICAgICAgICAgIGZvciAodmFyIHAgaW4gcykgaWYgKE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbChzLCBwKSkgdFtwXSA9IHNbcF07XHJcbiAgICAgICAgfVxyXG4gICAgICAgIHJldHVybiB0O1xyXG4gICAgfVxyXG4gICAgcmV0dXJuIF9fYXNzaWduLmFwcGx5KHRoaXMsIGFyZ3VtZW50cyk7XHJcbn1cclxuXHJcbmV4cG9ydCBmdW5jdGlvbiBfX3Jlc3QocywgZSkge1xyXG4gICAgdmFyIHQgPSB7fTtcclxuICAgIGZvciAodmFyIHAgaW4gcykgaWYgKE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbChzLCBwKSAmJiBlLmluZGV4T2YocCkgPCAwKVxyXG4gICAgICAgIHRbcF0gPSBzW3BdO1xyXG4gICAgaWYgKHMgIT0gbnVsbCAmJiB0eXBlb2YgT2JqZWN0LmdldE93blByb3BlcnR5U3ltYm9scyA9PT0gXCJmdW5jdGlvblwiKVxyXG4gICAgICAgIGZvciAodmFyIGkgPSAwLCBwID0gT2JqZWN0LmdldE93blByb3BlcnR5U3ltYm9scyhzKTsgaSA8IHAubGVuZ3RoOyBpKyspIGlmIChlLmluZGV4T2YocFtpXSkgPCAwKVxyXG4gICAgICAgICAgICB0W3BbaV1dID0gc1twW2ldXTtcclxuICAgIHJldHVybiB0O1xyXG59XHJcblxyXG5leHBvcnQgZnVuY3Rpb24gX19kZWNvcmF0ZShkZWNvcmF0b3JzLCB0YXJnZXQsIGtleSwgZGVzYykge1xyXG4gICAgdmFyIGMgPSBhcmd1bWVudHMubGVuZ3RoLCByID0gYyA8IDMgPyB0YXJnZXQgOiBkZXNjID09PSBudWxsID8gZGVzYyA9IE9iamVjdC5nZXRPd25Qcm9wZXJ0eURlc2NyaXB0b3IodGFyZ2V0LCBrZXkpIDogZGVzYywgZDtcclxuICAgIGlmICh0eXBlb2YgUmVmbGVjdCA9PT0gXCJvYmplY3RcIiAmJiB0eXBlb2YgUmVmbGVjdC5kZWNvcmF0ZSA9PT0gXCJmdW5jdGlvblwiKSByID0gUmVmbGVjdC5kZWNvcmF0ZShkZWNvcmF0b3JzLCB0YXJnZXQsIGtleSwgZGVzYyk7XHJcbiAgICBlbHNlIGZvciAodmFyIGkgPSBkZWNvcmF0b3JzLmxlbmd0aCAtIDE7IGkgPj0gMDsgaS0tKSBpZiAoZCA9IGRlY29yYXRvcnNbaV0pIHIgPSAoYyA8IDMgPyBkKHIpIDogYyA+IDMgPyBkKHRhcmdldCwga2V5LCByKSA6IGQodGFyZ2V0LCBrZXkpKSB8fCByO1xyXG4gICAgcmV0dXJuIGMgPiAzICYmIHIgJiYgT2JqZWN0LmRlZmluZVByb3BlcnR5KHRhcmdldCwga2V5LCByKSwgcjtcclxufVxyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9fcGFyYW0ocGFyYW1JbmRleCwgZGVjb3JhdG9yKSB7XHJcbiAgICByZXR1cm4gZnVuY3Rpb24gKHRhcmdldCwga2V5KSB7IGRlY29yYXRvcih0YXJnZXQsIGtleSwgcGFyYW1JbmRleCk7IH1cclxufVxyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9fbWV0YWRhdGEobWV0YWRhdGFLZXksIG1ldGFkYXRhVmFsdWUpIHtcclxuICAgIGlmICh0eXBlb2YgUmVmbGVjdCA9PT0gXCJvYmplY3RcIiAmJiB0eXBlb2YgUmVmbGVjdC5tZXRhZGF0YSA9PT0gXCJmdW5jdGlvblwiKSByZXR1cm4gUmVmbGVjdC5tZXRhZGF0YShtZXRhZGF0YUtleSwgbWV0YWRhdGFWYWx1ZSk7XHJcbn1cclxuXHJcbmV4cG9ydCBmdW5jdGlvbiBfX2F3YWl0ZXIodGhpc0FyZywgX2FyZ3VtZW50cywgUCwgZ2VuZXJhdG9yKSB7XHJcbiAgICByZXR1cm4gbmV3IChQIHx8IChQID0gUHJvbWlzZSkpKGZ1bmN0aW9uIChyZXNvbHZlLCByZWplY3QpIHtcclxuICAgICAgICBmdW5jdGlvbiBmdWxmaWxsZWQodmFsdWUpIHsgdHJ5IHsgc3RlcChnZW5lcmF0b3IubmV4dCh2YWx1ZSkpOyB9IGNhdGNoIChlKSB7IHJlamVjdChlKTsgfSB9XHJcbiAgICAgICAgZnVuY3Rpb24gcmVqZWN0ZWQodmFsdWUpIHsgdHJ5IHsgc3RlcChnZW5lcmF0b3JbXCJ0aHJvd1wiXSh2YWx1ZSkpOyB9IGNhdGNoIChlKSB7IHJlamVjdChlKTsgfSB9XHJcbiAgICAgICAgZnVuY3Rpb24gc3RlcChyZXN1bHQpIHsgcmVzdWx0LmRvbmUgPyByZXNvbHZlKHJlc3VsdC52YWx1ZSkgOiBuZXcgUChmdW5jdGlvbiAocmVzb2x2ZSkgeyByZXNvbHZlKHJlc3VsdC52YWx1ZSk7IH0pLnRoZW4oZnVsZmlsbGVkLCByZWplY3RlZCk7IH1cclxuICAgICAgICBzdGVwKChnZW5lcmF0b3IgPSBnZW5lcmF0b3IuYXBwbHkodGhpc0FyZywgX2FyZ3VtZW50cyB8fCBbXSkpLm5leHQoKSk7XHJcbiAgICB9KTtcclxufVxyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9fZ2VuZXJhdG9yKHRoaXNBcmcsIGJvZHkpIHtcclxuICAgIHZhciBfID0geyBsYWJlbDogMCwgc2VudDogZnVuY3Rpb24oKSB7IGlmICh0WzBdICYgMSkgdGhyb3cgdFsxXTsgcmV0dXJuIHRbMV07IH0sIHRyeXM6IFtdLCBvcHM6IFtdIH0sIGYsIHksIHQsIGc7XHJcbiAgICByZXR1cm4gZyA9IHsgbmV4dDogdmVyYigwKSwgXCJ0aHJvd1wiOiB2ZXJiKDEpLCBcInJldHVyblwiOiB2ZXJiKDIpIH0sIHR5cGVvZiBTeW1ib2wgPT09IFwiZnVuY3Rpb25cIiAmJiAoZ1tTeW1ib2wuaXRlcmF0b3JdID0gZnVuY3Rpb24oKSB7IHJldHVybiB0aGlzOyB9KSwgZztcclxuICAgIGZ1bmN0aW9uIHZlcmIobikgeyByZXR1cm4gZnVuY3Rpb24gKHYpIHsgcmV0dXJuIHN0ZXAoW24sIHZdKTsgfTsgfVxyXG4gICAgZnVuY3Rpb24gc3RlcChvcCkge1xyXG4gICAgICAgIGlmIChmKSB0aHJvdyBuZXcgVHlwZUVycm9yKFwiR2VuZXJhdG9yIGlzIGFscmVhZHkgZXhlY3V0aW5nLlwiKTtcclxuICAgICAgICB3aGlsZSAoXykgdHJ5IHtcclxuICAgICAgICAgICAgaWYgKGYgPSAxLCB5ICYmICh0ID0gb3BbMF0gJiAyID8geVtcInJldHVyblwiXSA6IG9wWzBdID8geVtcInRocm93XCJdIHx8ICgodCA9IHlbXCJyZXR1cm5cIl0pICYmIHQuY2FsbCh5KSwgMCkgOiB5Lm5leHQpICYmICEodCA9IHQuY2FsbCh5LCBvcFsxXSkpLmRvbmUpIHJldHVybiB0O1xyXG4gICAgICAgICAgICBpZiAoeSA9IDAsIHQpIG9wID0gW29wWzBdICYgMiwgdC52YWx1ZV07XHJcbiAgICAgICAgICAgIHN3aXRjaCAob3BbMF0pIHtcclxuICAgICAgICAgICAgICAgIGNhc2UgMDogY2FzZSAxOiB0ID0gb3A7IGJyZWFrO1xyXG4gICAgICAgICAgICAgICAgY2FzZSA0OiBfLmxhYmVsKys7IHJldHVybiB7IHZhbHVlOiBvcFsxXSwgZG9uZTogZmFsc2UgfTtcclxuICAgICAgICAgICAgICAgIGNhc2UgNTogXy5sYWJlbCsrOyB5ID0gb3BbMV07IG9wID0gWzBdOyBjb250aW51ZTtcclxuICAgICAgICAgICAgICAgIGNhc2UgNzogb3AgPSBfLm9wcy5wb3AoKTsgXy50cnlzLnBvcCgpOyBjb250aW51ZTtcclxuICAgICAgICAgICAgICAgIGRlZmF1bHQ6XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKCEodCA9IF8udHJ5cywgdCA9IHQubGVuZ3RoID4gMCAmJiB0W3QubGVuZ3RoIC0gMV0pICYmIChvcFswXSA9PT0gNiB8fCBvcFswXSA9PT0gMikpIHsgXyA9IDA7IGNvbnRpbnVlOyB9XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKG9wWzBdID09PSAzICYmICghdCB8fCAob3BbMV0gPiB0WzBdICYmIG9wWzFdIDwgdFszXSkpKSB7IF8ubGFiZWwgPSBvcFsxXTsgYnJlYWs7IH1cclxuICAgICAgICAgICAgICAgICAgICBpZiAob3BbMF0gPT09IDYgJiYgXy5sYWJlbCA8IHRbMV0pIHsgXy5sYWJlbCA9IHRbMV07IHQgPSBvcDsgYnJlYWs7IH1cclxuICAgICAgICAgICAgICAgICAgICBpZiAodCAmJiBfLmxhYmVsIDwgdFsyXSkgeyBfLmxhYmVsID0gdFsyXTsgXy5vcHMucHVzaChvcCk7IGJyZWFrOyB9XHJcbiAgICAgICAgICAgICAgICAgICAgaWYgKHRbMl0pIF8ub3BzLnBvcCgpO1xyXG4gICAgICAgICAgICAgICAgICAgIF8udHJ5cy5wb3AoKTsgY29udGludWU7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgb3AgPSBib2R5LmNhbGwodGhpc0FyZywgXyk7XHJcbiAgICAgICAgfSBjYXRjaCAoZSkgeyBvcCA9IFs2LCBlXTsgeSA9IDA7IH0gZmluYWxseSB7IGYgPSB0ID0gMDsgfVxyXG4gICAgICAgIGlmIChvcFswXSAmIDUpIHRocm93IG9wWzFdOyByZXR1cm4geyB2YWx1ZTogb3BbMF0gPyBvcFsxXSA6IHZvaWQgMCwgZG9uZTogdHJ1ZSB9O1xyXG4gICAgfVxyXG59XHJcblxyXG5leHBvcnQgZnVuY3Rpb24gX19leHBvcnRTdGFyKG0sIGV4cG9ydHMpIHtcclxuICAgIGZvciAodmFyIHAgaW4gbSkgaWYgKCFleHBvcnRzLmhhc093blByb3BlcnR5KHApKSBleHBvcnRzW3BdID0gbVtwXTtcclxufVxyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9fdmFsdWVzKG8pIHtcclxuICAgIHZhciBtID0gdHlwZW9mIFN5bWJvbCA9PT0gXCJmdW5jdGlvblwiICYmIG9bU3ltYm9sLml0ZXJhdG9yXSwgaSA9IDA7XHJcbiAgICBpZiAobSkgcmV0dXJuIG0uY2FsbChvKTtcclxuICAgIHJldHVybiB7XHJcbiAgICAgICAgbmV4dDogZnVuY3Rpb24gKCkge1xyXG4gICAgICAgICAgICBpZiAobyAmJiBpID49IG8ubGVuZ3RoKSBvID0gdm9pZCAwO1xyXG4gICAgICAgICAgICByZXR1cm4geyB2YWx1ZTogbyAmJiBvW2krK10sIGRvbmU6ICFvIH07XHJcbiAgICAgICAgfVxyXG4gICAgfTtcclxufVxyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9fcmVhZChvLCBuKSB7XHJcbiAgICB2YXIgbSA9IHR5cGVvZiBTeW1ib2wgPT09IFwiZnVuY3Rpb25cIiAmJiBvW1N5bWJvbC5pdGVyYXRvcl07XHJcbiAgICBpZiAoIW0pIHJldHVybiBvO1xyXG4gICAgdmFyIGkgPSBtLmNhbGwobyksIHIsIGFyID0gW10sIGU7XHJcbiAgICB0cnkge1xyXG4gICAgICAgIHdoaWxlICgobiA9PT0gdm9pZCAwIHx8IG4tLSA+IDApICYmICEociA9IGkubmV4dCgpKS5kb25lKSBhci5wdXNoKHIudmFsdWUpO1xyXG4gICAgfVxyXG4gICAgY2F0Y2ggKGVycm9yKSB7IGUgPSB7IGVycm9yOiBlcnJvciB9OyB9XHJcbiAgICBmaW5hbGx5IHtcclxuICAgICAgICB0cnkge1xyXG4gICAgICAgICAgICBpZiAociAmJiAhci5kb25lICYmIChtID0gaVtcInJldHVyblwiXSkpIG0uY2FsbChpKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgZmluYWxseSB7IGlmIChlKSB0aHJvdyBlLmVycm9yOyB9XHJcbiAgICB9XHJcbiAgICByZXR1cm4gYXI7XHJcbn1cclxuXHJcbmV4cG9ydCBmdW5jdGlvbiBfX3NwcmVhZCgpIHtcclxuICAgIGZvciAodmFyIGFyID0gW10sIGkgPSAwOyBpIDwgYXJndW1lbnRzLmxlbmd0aDsgaSsrKVxyXG4gICAgICAgIGFyID0gYXIuY29uY2F0KF9fcmVhZChhcmd1bWVudHNbaV0pKTtcclxuICAgIHJldHVybiBhcjtcclxufVxyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9fYXdhaXQodikge1xyXG4gICAgcmV0dXJuIHRoaXMgaW5zdGFuY2VvZiBfX2F3YWl0ID8gKHRoaXMudiA9IHYsIHRoaXMpIDogbmV3IF9fYXdhaXQodik7XHJcbn1cclxuXHJcbmV4cG9ydCBmdW5jdGlvbiBfX2FzeW5jR2VuZXJhdG9yKHRoaXNBcmcsIF9hcmd1bWVudHMsIGdlbmVyYXRvcikge1xyXG4gICAgaWYgKCFTeW1ib2wuYXN5bmNJdGVyYXRvcikgdGhyb3cgbmV3IFR5cGVFcnJvcihcIlN5bWJvbC5hc3luY0l0ZXJhdG9yIGlzIG5vdCBkZWZpbmVkLlwiKTtcclxuICAgIHZhciBnID0gZ2VuZXJhdG9yLmFwcGx5KHRoaXNBcmcsIF9hcmd1bWVudHMgfHwgW10pLCBpLCBxID0gW107XHJcbiAgICByZXR1cm4gaSA9IHt9LCB2ZXJiKFwibmV4dFwiKSwgdmVyYihcInRocm93XCIpLCB2ZXJiKFwicmV0dXJuXCIpLCBpW1N5bWJvbC5hc3luY0l0ZXJhdG9yXSA9IGZ1bmN0aW9uICgpIHsgcmV0dXJuIHRoaXM7IH0sIGk7XHJcbiAgICBmdW5jdGlvbiB2ZXJiKG4pIHsgaWYgKGdbbl0pIGlbbl0gPSBmdW5jdGlvbiAodikgeyByZXR1cm4gbmV3IFByb21pc2UoZnVuY3Rpb24gKGEsIGIpIHsgcS5wdXNoKFtuLCB2LCBhLCBiXSkgPiAxIHx8IHJlc3VtZShuLCB2KTsgfSk7IH07IH1cclxuICAgIGZ1bmN0aW9uIHJlc3VtZShuLCB2KSB7IHRyeSB7IHN0ZXAoZ1tuXSh2KSk7IH0gY2F0Y2ggKGUpIHsgc2V0dGxlKHFbMF1bM10sIGUpOyB9IH1cclxuICAgIGZ1bmN0aW9uIHN0ZXAocikgeyByLnZhbHVlIGluc3RhbmNlb2YgX19hd2FpdCA/IFByb21pc2UucmVzb2x2ZShyLnZhbHVlLnYpLnRoZW4oZnVsZmlsbCwgcmVqZWN0KSA6IHNldHRsZShxWzBdWzJdLCByKTsgfVxyXG4gICAgZnVuY3Rpb24gZnVsZmlsbCh2YWx1ZSkgeyByZXN1bWUoXCJuZXh0XCIsIHZhbHVlKTsgfVxyXG4gICAgZnVuY3Rpb24gcmVqZWN0KHZhbHVlKSB7IHJlc3VtZShcInRocm93XCIsIHZhbHVlKTsgfVxyXG4gICAgZnVuY3Rpb24gc2V0dGxlKGYsIHYpIHsgaWYgKGYodiksIHEuc2hpZnQoKSwgcS5sZW5ndGgpIHJlc3VtZShxWzBdWzBdLCBxWzBdWzFdKTsgfVxyXG59XHJcblxyXG5leHBvcnQgZnVuY3Rpb24gX19hc3luY0RlbGVnYXRvcihvKSB7XHJcbiAgICB2YXIgaSwgcDtcclxuICAgIHJldHVybiBpID0ge30sIHZlcmIoXCJuZXh0XCIpLCB2ZXJiKFwidGhyb3dcIiwgZnVuY3Rpb24gKGUpIHsgdGhyb3cgZTsgfSksIHZlcmIoXCJyZXR1cm5cIiksIGlbU3ltYm9sLml0ZXJhdG9yXSA9IGZ1bmN0aW9uICgpIHsgcmV0dXJuIHRoaXM7IH0sIGk7XHJcbiAgICBmdW5jdGlvbiB2ZXJiKG4sIGYpIHsgaVtuXSA9IG9bbl0gPyBmdW5jdGlvbiAodikgeyByZXR1cm4gKHAgPSAhcCkgPyB7IHZhbHVlOiBfX2F3YWl0KG9bbl0odikpLCBkb25lOiBuID09PSBcInJldHVyblwiIH0gOiBmID8gZih2KSA6IHY7IH0gOiBmOyB9XHJcbn1cclxuXHJcbmV4cG9ydCBmdW5jdGlvbiBfX2FzeW5jVmFsdWVzKG8pIHtcclxuICAgIGlmICghU3ltYm9sLmFzeW5jSXRlcmF0b3IpIHRocm93IG5ldyBUeXBlRXJyb3IoXCJTeW1ib2wuYXN5bmNJdGVyYXRvciBpcyBub3QgZGVmaW5lZC5cIik7XHJcbiAgICB2YXIgbSA9IG9bU3ltYm9sLmFzeW5jSXRlcmF0b3JdLCBpO1xyXG4gICAgcmV0dXJuIG0gPyBtLmNhbGwobykgOiAobyA9IHR5cGVvZiBfX3ZhbHVlcyA9PT0gXCJmdW5jdGlvblwiID8gX192YWx1ZXMobykgOiBvW1N5bWJvbC5pdGVyYXRvcl0oKSwgaSA9IHt9LCB2ZXJiKFwibmV4dFwiKSwgdmVyYihcInRocm93XCIpLCB2ZXJiKFwicmV0dXJuXCIpLCBpW1N5bWJvbC5hc3luY0l0ZXJhdG9yXSA9IGZ1bmN0aW9uICgpIHsgcmV0dXJuIHRoaXM7IH0sIGkpO1xyXG4gICAgZnVuY3Rpb24gdmVyYihuKSB7IGlbbl0gPSBvW25dICYmIGZ1bmN0aW9uICh2KSB7IHJldHVybiBuZXcgUHJvbWlzZShmdW5jdGlvbiAocmVzb2x2ZSwgcmVqZWN0KSB7IHYgPSBvW25dKHYpLCBzZXR0bGUocmVzb2x2ZSwgcmVqZWN0LCB2LmRvbmUsIHYudmFsdWUpOyB9KTsgfTsgfVxyXG4gICAgZnVuY3Rpb24gc2V0dGxlKHJlc29sdmUsIHJlamVjdCwgZCwgdikgeyBQcm9taXNlLnJlc29sdmUodikudGhlbihmdW5jdGlvbih2KSB7IHJlc29sdmUoeyB2YWx1ZTogdiwgZG9uZTogZCB9KTsgfSwgcmVqZWN0KTsgfVxyXG59XHJcblxyXG5leHBvcnQgZnVuY3Rpb24gX19tYWtlVGVtcGxhdGVPYmplY3QoY29va2VkLCByYXcpIHtcclxuICAgIGlmIChPYmplY3QuZGVmaW5lUHJvcGVydHkpIHsgT2JqZWN0LmRlZmluZVByb3BlcnR5KGNvb2tlZCwgXCJyYXdcIiwgeyB2YWx1ZTogcmF3IH0pOyB9IGVsc2UgeyBjb29rZWQucmF3ID0gcmF3OyB9XHJcbiAgICByZXR1cm4gY29va2VkO1xyXG59O1xyXG5cclxuZXhwb3J0IGZ1bmN0aW9uIF9faW1wb3J0U3Rhcihtb2QpIHtcclxuICAgIGlmIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpIHJldHVybiBtb2Q7XHJcbiAgICB2YXIgcmVzdWx0ID0ge307XHJcbiAgICBpZiAobW9kICE9IG51bGwpIGZvciAodmFyIGsgaW4gbW9kKSBpZiAoT2JqZWN0Lmhhc093blByb3BlcnR5LmNhbGwobW9kLCBrKSkgcmVzdWx0W2tdID0gbW9kW2tdO1xyXG4gICAgcmVzdWx0LmRlZmF1bHQgPSBtb2Q7XHJcbiAgICByZXR1cm4gcmVzdWx0O1xyXG59XHJcblxyXG5leHBvcnQgZnVuY3Rpb24gX19pbXBvcnREZWZhdWx0KG1vZCkge1xyXG4gICAgcmV0dXJuIChtb2QgJiYgbW9kLl9fZXNNb2R1bGUpID8gbW9kIDogeyBkZWZhdWx0OiBtb2QgfTtcclxufVxyXG4iLCIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgTWF1cmljaW8gR2VtZWxsaSBWaWdvbG8gYW5kIGNvbnRyaWJ1dG9ycy5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vZ2l0aHViLmNvbS9tYXVyaWNpb3ZpZ29sby9rZXljbG9hay1hbmd1bGFyL0xJQ0VOU0VcbiAqL1xuXG5pbXBvcnQgeyBDYW5BY3RpdmF0ZSwgUm91dGVyLCBBY3RpdmF0ZWRSb3V0ZVNuYXBzaG90LCBSb3V0ZXJTdGF0ZVNuYXBzaG90IH0gZnJvbSAnQGFuZ3VsYXIvcm91dGVyJztcblxuaW1wb3J0IHsgS2V5Y2xvYWtTZXJ2aWNlIH0gZnJvbSAnLi9rZXljbG9hay5zZXJ2aWNlJztcblxuLyoqXG4gKiBBIHNpbXBsZSBndWFyZCBpbXBsZW1lbnRhdGlvbiBvdXQgb2YgdGhlIGJveC4gVGhpcyBjbGFzcyBzaG91bGQgYmUgaW5oZXJpdGVkIGFuZFxuICogaW1wbGVtZW50ZWQgYnkgdGhlIGFwcGxpY2F0aW9uLiBUaGUgb25seSBtZXRob2QgdGhhdCBzaG91bGQgYmUgaW1wbGVtZW50ZWQgaXMgI2lzQWNjZXNzQWxsb3dlZC5cbiAqIFRoZSByZWFzb24gZm9yIHRoaXMgaXMgdGhhdCB0aGUgYXV0aG9yaXphdGlvbiBmbG93IGlzIHVzdWFsbHkgbm90IHVuaXF1ZSwgc28gaW4gdGhpcyB3YXkgeW91IHdpbGxcbiAqIGhhdmUgbW9yZSBmcmVlZG9tIHRvIGN1c3RvbWl6ZSB5b3VyIGF1dGhvcml6YXRpb24gZmxvdy5cbiAqL1xuZXhwb3J0IGFic3RyYWN0IGNsYXNzIEtleWNsb2FrQXV0aEd1YXJkIGltcGxlbWVudHMgQ2FuQWN0aXZhdGUge1xuICAvKipcbiAgICogSW5kaWNhdGVzIGlmIHRoZSB1c2VyIGlzIGF1dGhlbnRpY2F0ZWQgb3Igbm90LlxuICAgKi9cbiAgcHJvdGVjdGVkIGF1dGhlbnRpY2F0ZWQ6IGJvb2xlYW47XG4gIC8qKlxuICAgKiBSb2xlcyBvZiB0aGUgbG9nZ2VkIHVzZXIuIEl0IGNvbnRhaW5zIHRoZSBjbGllbnRJZCBhbmQgcmVhbG0gdXNlciByb2xlcy5cbiAgICovXG4gIHByb3RlY3RlZCByb2xlczogc3RyaW5nW107XG5cbiAgY29uc3RydWN0b3IocHJvdGVjdGVkIHJvdXRlcjogUm91dGVyLCBwcm90ZWN0ZWQga2V5Y2xvYWtBbmd1bGFyOiBLZXljbG9ha1NlcnZpY2UpIHt9XG5cbiAgLyoqXG4gICAqIENhbkFjdGl2YXRlIGNoZWNrcyBpZiB0aGUgdXNlciBpcyBsb2dnZWQgaW4gYW5kIGdldCB0aGUgZnVsbCBsaXN0IG9mIHJvbGVzIChSRUFMTSArIENMSUVOVClcbiAgICogb2YgdGhlIGxvZ2dlZCB1c2VyLiBUaGlzIHZhbHVlcyBhcmUgc2V0IHRvIGF1dGhlbnRpY2F0ZWQgYW5kIHJvbGVzIHBhcmFtcy5cbiAgICpcbiAgICogQHBhcmFtIHJvdXRlXG4gICAqIEBwYXJhbSBzdGF0ZVxuICAgKi9cbiAgY2FuQWN0aXZhdGUocm91dGU6IEFjdGl2YXRlZFJvdXRlU25hcHNob3QsIHN0YXRlOiBSb3V0ZXJTdGF0ZVNuYXBzaG90KTogUHJvbWlzZTxib29sZWFuPiB7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKGFzeW5jIChyZXNvbHZlLCByZWplY3QpID0+IHtcbiAgICAgIHRyeSB7XG4gICAgICAgIHRoaXMuYXV0aGVudGljYXRlZCA9IGF3YWl0IHRoaXMua2V5Y2xvYWtBbmd1bGFyLmlzTG9nZ2VkSW4oKTtcbiAgICAgICAgdGhpcy5yb2xlcyA9IGF3YWl0IHRoaXMua2V5Y2xvYWtBbmd1bGFyLmdldFVzZXJSb2xlcyh0cnVlKTtcblxuICAgICAgICBjb25zdCByZXN1bHQgPSBhd2FpdCB0aGlzLmlzQWNjZXNzQWxsb3dlZChyb3V0ZSwgc3RhdGUpO1xuICAgICAgICByZXNvbHZlKHJlc3VsdCk7XG4gICAgICB9IGNhdGNoIChlcnJvcikge1xuICAgICAgICByZWplY3QoJ0FuIGVycm9yIGhhcHBlbmVkIGR1cmluZyBhY2Nlc3MgdmFsaWRhdGlvbi4gRGV0YWlsczonICsgZXJyb3IpO1xuICAgICAgfVxuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIENyZWF0ZSB5b3VyIG93biBjdXN0b21pemVkIGF1dGhvcml6YXRpb24gZmxvdyBpbiB0aGlzIG1ldGhvZC4gRnJvbSBoZXJlIHlvdSBhbHJlYWR5IGtub3duXG4gICAqIGlmIHRoZSB1c2VyIGlzIGF1dGhlbnRpY2F0ZWQgKHRoaXMuYXV0aGVudGljYXRlZCkgYW5kIHRoZSB1c2VyIHJvbGVzICh0aGlzLnJvbGVzKS5cbiAgICpcbiAgICogQHBhcmFtIHJvdXRlXG4gICAqIEBwYXJhbSBzdGF0ZVxuICAgKi9cbiAgYWJzdHJhY3QgaXNBY2Nlc3NBbGxvd2VkKFxuICAgIHJvdXRlOiBBY3RpdmF0ZWRSb3V0ZVNuYXBzaG90LFxuICAgIHN0YXRlOiBSb3V0ZXJTdGF0ZVNuYXBzaG90XG4gICk6IFByb21pc2U8Ym9vbGVhbj47XG59XG4iLCIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgTWF1cmljaW8gR2VtZWxsaSBWaWdvbG8gYW5kIGNvbnRyaWJ1dG9ycy5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vZ2l0aHViLmNvbS9tYXVyaWNpb3ZpZ29sby9rZXljbG9hay1hbmd1bGFyL0xJQ0VOU0VcbiAqL1xuXG5pbXBvcnQgeyBJbmplY3RhYmxlIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5cbmltcG9ydCB7IEh0dHBIZWFkZXJzIH0gZnJvbSAnQGFuZ3VsYXIvY29tbW9uL2h0dHAnO1xuXG4vLyBXb3JrYXJvdW5kIGZvciByb2xsdXAgbGlicmFyeSBiZWhhdmlvdXIsIGFzIHBvaW50ZWQgb3V0IG9uIGlzc3VlICMxMjY3IChodHRwczovL2dpdGh1Yi5jb20vcm9sbHVwL3JvbGx1cC9pc3N1ZXMvMTI2NykuXG5pbXBvcnQgKiBhcyBLZXljbG9ha18gZnJvbSAna2V5Y2xvYWstanMnO1xuZXhwb3J0IGNvbnN0IEtleWNsb2FrID0gS2V5Y2xvYWtfO1xuXG5pbXBvcnQgeyBPYnNlcnZhYmxlLCBPYnNlcnZlciwgU3ViamVjdCB9IGZyb20gJ3J4anMnO1xuXG5pbXBvcnQgeyBLZXljbG9ha09wdGlvbnMgfSBmcm9tICcuLi9pbnRlcmZhY2VzL2tleWNsb2FrLW9wdGlvbnMnO1xuaW1wb3J0IHsgS2V5Y2xvYWtFdmVudCwgS2V5Y2xvYWtFdmVudFR5cGUgfSBmcm9tICcuLi9pbnRlcmZhY2VzL2tleWNsb2FrLWV2ZW50JztcblxuLyoqXG4gKiBTZXJ2aWNlIHRvIGV4cG9zZSBleGlzdGVudCBtZXRob2RzIGZyb20gdGhlIEtleWNsb2FrIEpTIGFkYXB0ZXIsIGFkZGluZyBuZXdcbiAqIGZ1bmN0aW9uYWxpdGllcyB0byBpbXByb3ZlIHRoZSB1c2Ugb2Yga2V5Y2xvYWsgaW4gQW5ndWxhciB2ID4gNC4zIGFwcGxpY2F0aW9ucy5cbiAqXG4gKiBUaGlzIGNsYXNzIHNob3VsZCBiZSBpbmplY3RlZCBpbiB0aGUgYXBwbGljYXRpb24gYm9vdHN0cmFwLCBzbyB0aGUgc2FtZSBpbnN0YW5jZSB3aWxsIGJlIHVzZWRcbiAqIGFsb25nIHRoZSB3ZWIgYXBwbGljYXRpb24uXG4gKi9cbkBJbmplY3RhYmxlKClcbmV4cG9ydCBjbGFzcyBLZXljbG9ha1NlcnZpY2Uge1xuICAvKipcbiAgICogS2V5Y2xvYWstanMgaW5zdGFuY2UuXG4gICAqL1xuICBwcml2YXRlIF9pbnN0YW5jZTogS2V5Y2xvYWsuS2V5Y2xvYWtJbnN0YW5jZTtcbiAgLyoqXG4gICAqIFVzZXIgcHJvZmlsZSBhcyBLZXljbG9ha1Byb2ZpbGUgaW50ZXJmYWNlLlxuICAgKi9cbiAgcHJpdmF0ZSBfdXNlclByb2ZpbGU6IEtleWNsb2FrLktleWNsb2FrUHJvZmlsZTtcbiAgLyoqXG4gICAqIEZsYWcgdG8gaW5kaWNhdGUgaWYgdGhlIGJlYXJlciB3aWxsIG5vdCBiZSBhZGRlZCB0byB0aGUgYXV0aG9yaXphdGlvbiBoZWFkZXIuXG4gICAqL1xuICBwcml2YXRlIF9lbmFibGVCZWFyZXJJbnRlcmNlcHRvcjogYm9vbGVhbjtcbiAgLyoqXG4gICAqIFdoZW4gdGhlIGltcGxpY2l0IGZsb3cgaXMgY2hvb3NlbiB0aGVyZSBtdXN0IGV4aXN0IGEgc2lsZW50UmVmcmVzaCwgYXMgdGhlcmUgaXNcbiAgICogbm8gcmVmcmVzaCB0b2tlbi5cbiAgICovXG4gIHByaXZhdGUgX3NpbGVudFJlZnJlc2g6IGJvb2xlYW47XG4gIC8qKlxuICAgKiBJbmRpY2F0ZXMgdGhhdCB0aGUgdXNlciBwcm9maWxlIHNob3VsZCBiZSBsb2FkZWQgYXQgdGhlIGtleWNsb2FrIGluaXRpYWxpemF0aW9uLFxuICAgKiBqdXN0IGFmdGVyIHRoZSBsb2dpbi5cbiAgICovXG4gIHByaXZhdGUgX2xvYWRVc2VyUHJvZmlsZUF0U3RhcnRVcDogYm9vbGVhbjtcbiAgLyoqXG4gICAqIFRoZSBiZWFyZXIgcHJlZml4IHRoYXQgd2lsbCBiZSBhcHBlbmRlZCB0byB0aGUgQXV0aG9yaXphdGlvbiBIZWFkZXIuXG4gICAqL1xuICBwcml2YXRlIF9iZWFyZXJQcmVmaXg6IHN0cmluZztcbiAgLyoqXG4gICAqIFZhbHVlIHRoYXQgd2lsbCBiZSB1c2VkIGFzIHRoZSBBdXRob3JpemF0aW9uIEh0dHAgSGVhZGVyIG5hbWUuXG4gICAqL1xuICBwcml2YXRlIF9hdXRob3JpemF0aW9uSGVhZGVyTmFtZTogc3RyaW5nO1xuICAvKipcbiAgICogVGhlIGV4Y2x1ZGVkIHVybHMgcGF0dGVybnMgdGhhdCBtdXN0IHNraXAgdGhlIEtleWNsb2FrQmVhcmVySW50ZXJjZXB0b3IuXG4gICAqL1xuICBwcml2YXRlIF9iZWFyZXJFeGNsdWRlZFVybHM6IHN0cmluZ1tdO1xuICAvKipcbiAgICogT2JzZXJ2ZXIgZm9yIHRoZSBrZXljbG9hayBldmVudHNcbiAgICovXG4gIHByaXZhdGUgX2tleWNsb2FrRXZlbnRzJDogU3ViamVjdDxLZXljbG9ha0V2ZW50PjtcblxuICBjb25zdHJ1Y3RvcigpIHtcbiAgICB0aGlzLl9rZXljbG9ha0V2ZW50cyQgPSBuZXcgU3ViamVjdDxLZXljbG9ha0V2ZW50PigpO1xuICB9XG5cbiAgLyoqXG4gICAqIFNhbml0aXplcyB0aGUgYmVhcmVyIHByZWZpeCwgcHJlcGFyaW5nIGl0IHRvIGJlIGFwcGVuZGVkIHRvXG4gICAqIHRoZSB0b2tlbi5cbiAgICpcbiAgICogQHBhcmFtIGJlYXJlclByZWZpeFxuICAgKiBQcmVmaXggdG8gYmUgYXBwZW5kZWQgdG8gdGhlIGF1dGhvcml6YXRpb24gaGVhZGVyIGFzXG4gICAqIEF1dGhvcml6YXRpb246IDxiZWFyZXItcHJlZml4PiA8dG9rZW4+LlxuICAgKiBAcmV0dXJuc1xuICAgKiBUaGUgYmVhcmVyIHByZWZpeCBzYW5pdGl6ZWQsIG1lYW5pbmcgdGhhdCBpdCB3aWxsIGZvbGxvdyB0aGUgYmVhcmVyUHJlZml4XG4gICAqIHBhcmFtIGFzIGRlc2NyaWJlZCBpbiB0aGUgbGlicmFyeSBpbml0aWxpemF0aW9uIG9yIHRoZSBkZWZhdWx0IHZhbHVlIGJlYXJlcixcbiAgICogd2l0aCBhIHNwYWNlIGFwcGVuZCBpbiB0aGUgZW5kIGZvciB0aGUgdG9rZW4gY29uY2F0ZW5hdGlvbi5cbiAgICovXG4gIHByaXZhdGUgc2FuaXRpemVCZWFyZXJQcmVmaXgoYmVhcmVyUHJlZml4OiBzdHJpbmcgfCB1bmRlZmluZWQpOiBzdHJpbmcge1xuICAgIGxldCBwcmVmaXg6IHN0cmluZyA9IChiZWFyZXJQcmVmaXggfHwgJ2JlYXJlcicpLnRyaW0oKTtcbiAgICByZXR1cm4gcHJlZml4LmNvbmNhdCgnICcpO1xuICB9XG5cbiAgLyoqXG4gICAqIFNldHMgZGVmYXVsdCB2YWx1ZSB0byB0cnVlIGlmIGl0IGlzIHVuZGVmaW5lZCBvciBudWxsLlxuICAgKlxuICAgKiBAcGFyYW0gdmFsdWUgLSBib29sZWFuIHZhbHVlIHRvIGJlIGNoZWNrZWRcbiAgICovXG4gIHByaXZhdGUgaWZVbmRlZmluZWRJc1RydWUodmFsdWU6IGJvb2xlYW4pOiBib29sZWFuIHtcbiAgICBsZXQgcmV0dXJuVmFsdWU6IGJvb2xlYW4gPSB2YWx1ZTtcbiAgICBpZiAocmV0dXJuVmFsdWUgPT09IHVuZGVmaW5lZCB8fCByZXR1cm5WYWx1ZSA9PT0gbnVsbCkge1xuICAgICAgcmV0dXJuVmFsdWUgPSB0cnVlO1xuICAgIH1cbiAgICByZXR1cm4gcmV0dXJuVmFsdWU7XG4gIH1cblxuICAvKipcbiAgICogQmluZHMgdGhlIGtleWNsb2FrLWpzIGV2ZW50cyB0byB0aGUga2V5Y2xvYWtFdmVudHMgU3ViamVjdFxuICAgKiB3aGljaCBpcyBhIGdvb2Qgd2F5IHRvIG1vbml0b3IgZm9yIGNoYW5nZXMsIGlmIG5lZWRlZC5cbiAgICpcbiAgICogVGhlIGtleWNsb2FrRXZlbnRzIHJldHVybnMgdGhlIGtleWNsb2FrLWpzIGV2ZW50IHR5cGUgYW5kIGFueVxuICAgKiBhcmd1bWVudCBpZiB0aGUgc291cmNlIGZ1bmN0aW9uIHByb3ZpZGVzIGFueS5cbiAgICovXG4gIHByaXZhdGUgYmluZHNLZXljbG9ha0V2ZW50cygpOiB2b2lkIHtcbiAgICBpZiAoIXRoaXMuX2luc3RhbmNlKSB7XG4gICAgICBjb25zb2xlLndhcm4oXG4gICAgICAgICdLZXljbG9hayBBbmd1bGFyIGV2ZW50cyBjb3VsZCBub3QgYmUgcmVnaXN0ZXJlZCBhcyB0aGUga2V5Y2xvYWsgaW5zdGFuY2UgaXMgdW5kZWZpbmVkLidcbiAgICAgICk7XG4gICAgICByZXR1cm47XG4gICAgfVxuXG4gICAgdGhpcy5faW5zdGFuY2Uub25BdXRoRXJyb3IgPSBlcnJvckRhdGEgPT4ge1xuICAgICAgdGhpcy5fa2V5Y2xvYWtFdmVudHMkLm5leHQoeyBhcmdzOiBlcnJvckRhdGEsIHR5cGU6IEtleWNsb2FrRXZlbnRUeXBlLk9uQXV0aEVycm9yIH0pO1xuICAgIH07XG5cbiAgICB0aGlzLl9pbnN0YW5jZS5vbkF1dGhMb2dvdXQgPSAoKSA9PiB7XG4gICAgICB0aGlzLl9rZXljbG9ha0V2ZW50cyQubmV4dCh7IHR5cGU6IEtleWNsb2FrRXZlbnRUeXBlLk9uQXV0aExvZ291dCB9KTtcbiAgICB9O1xuXG4gICAgdGhpcy5faW5zdGFuY2Uub25BdXRoUmVmcmVzaEVycm9yID0gKCkgPT4ge1xuICAgICAgdGhpcy5fa2V5Y2xvYWtFdmVudHMkLm5leHQoeyB0eXBlOiBLZXljbG9ha0V2ZW50VHlwZS5PbkF1dGhSZWZyZXNoRXJyb3IgfSk7XG4gICAgfTtcblxuICAgIHRoaXMuX2luc3RhbmNlLm9uQXV0aFN1Y2Nlc3MgPSAoKSA9PiB7XG4gICAgICB0aGlzLl9rZXljbG9ha0V2ZW50cyQubmV4dCh7IHR5cGU6IEtleWNsb2FrRXZlbnRUeXBlLk9uQXV0aFN1Y2Nlc3MgfSk7XG4gICAgfTtcblxuICAgIHRoaXMuX2luc3RhbmNlLm9uVG9rZW5FeHBpcmVkID0gKCkgPT4ge1xuICAgICAgdGhpcy5fa2V5Y2xvYWtFdmVudHMkLm5leHQoeyB0eXBlOiBLZXljbG9ha0V2ZW50VHlwZS5PblRva2VuRXhwaXJlZCB9KTtcbiAgICB9O1xuXG4gICAgdGhpcy5faW5zdGFuY2Uub25SZWFkeSA9IGF1dGhlbnRpY2F0ZWQgPT4ge1xuICAgICAgdGhpcy5fa2V5Y2xvYWtFdmVudHMkLm5leHQoeyBhcmdzOiBhdXRoZW50aWNhdGVkLCB0eXBlOiBLZXljbG9ha0V2ZW50VHlwZS5PblJlYWR5IH0pO1xuICAgIH07XG4gIH1cblxuICAvKipcbiAgICogS2V5Y2xvYWsgaW5pdGlhbGl6YXRpb24uIEl0IHNob3VsZCBiZSBjYWxsZWQgdG8gaW5pdGlhbGl6ZSB0aGUgYWRhcHRlci5cbiAgICogT3B0aW9ucyBpcyBhIG9iamVjdCB3aXRoIDIgbWFpbiBwYXJhbWV0ZXJzOiBjb25maWcgYW5kIGluaXRPcHRpb25zLiBUaGUgZmlyc3Qgb25lXG4gICAqIHdpbGwgYmUgdXNlZCB0byBjcmVhdGUgdGhlIEtleWNsb2FrIGluc3RhbmNlLiBUaGUgc2Vjb25kIG9uZSBhcmUgb3B0aW9ucyB0byBpbml0aWFsaXplIHRoZVxuICAgKiBrZXljbG9hayBpbnN0YW5jZS5cbiAgICpcbiAgICogQHBhcmFtIG9wdGlvbnNcbiAgICogQ29uZmlnOiBtYXkgYmUgYSBzdHJpbmcgcmVwcmVzZW50aW5nIHRoZSBrZXljbG9hayBVUkkgb3IgYW4gb2JqZWN0IHdpdGggdGhlXG4gICAqIGZvbGxvd2luZyBjb250ZW50OlxuICAgKiAtIHVybDogS2V5Y2xvYWsganNvbiBVUkxcbiAgICogLSByZWFsbTogcmVhbG0gbmFtZVxuICAgKiAtIGNsaWVudElkOiBjbGllbnQgaWRcbiAgICpcbiAgICogaW5pdE9wdGlvbnM6XG4gICAqIC0gb25Mb2FkOiBTcGVjaWZpZXMgYW4gYWN0aW9uIHRvIGRvIG9uIGxvYWQuIFN1cHBvcnRlZCB2YWx1ZXMgYXJlICdsb2dpbi1yZXF1aXJlZCcgb3JcbiAgICogJ2NoZWNrLXNzbycuXG4gICAqIC0gdG9rZW46IFNldCBhbiBpbml0aWFsIHZhbHVlIGZvciB0aGUgdG9rZW4uXG4gICAqIC0gcmVmcmVzaFRva2VuOiBTZXQgYW4gaW5pdGlhbCB2YWx1ZSBmb3IgdGhlIHJlZnJlc2ggdG9rZW4uXG4gICAqIC0gaWRUb2tlbjogU2V0IGFuIGluaXRpYWwgdmFsdWUgZm9yIHRoZSBpZCB0b2tlbiAob25seSB0b2dldGhlciB3aXRoIHRva2VuIG9yIHJlZnJlc2hUb2tlbikuXG4gICAqIC0gdGltZVNrZXc6IFNldCBhbiBpbml0aWFsIHZhbHVlIGZvciBza2V3IGJldHdlZW4gbG9jYWwgdGltZSBhbmQgS2V5Y2xvYWsgc2VydmVyIGluIHNlY29uZHNcbiAgICogKG9ubHkgdG9nZXRoZXIgd2l0aCB0b2tlbiBvciByZWZyZXNoVG9rZW4pLlxuICAgKiAtIGNoZWNrTG9naW5JZnJhbWU6IFNldCB0byBlbmFibGUvZGlzYWJsZSBtb25pdG9yaW5nIGxvZ2luIHN0YXRlIChkZWZhdWx0IGlzIHRydWUpLlxuICAgKiAtIGNoZWNrTG9naW5JZnJhbWVJbnRlcnZhbDogU2V0IHRoZSBpbnRlcnZhbCB0byBjaGVjayBsb2dpbiBzdGF0ZSAoZGVmYXVsdCBpcyA1IHNlY29uZHMpLlxuICAgKiAtIHJlc3BvbnNlTW9kZTogU2V0IHRoZSBPcGVuSUQgQ29ubmVjdCByZXNwb25zZSBtb2RlIHNlbmQgdG8gS2V5Y2xvYWsgc2VydmVyIGF0IGxvZ2luXG4gICAqIHJlcXVlc3QuIFZhbGlkIHZhbHVlcyBhcmUgcXVlcnkgb3IgZnJhZ21lbnQgLiBEZWZhdWx0IHZhbHVlIGlzIGZyYWdtZW50LCB3aGljaCBtZWFuc1xuICAgKiB0aGF0IGFmdGVyIHN1Y2Nlc3NmdWwgYXV0aGVudGljYXRpb24gd2lsbCBLZXljbG9hayByZWRpcmVjdCB0byBqYXZhc2NyaXB0IGFwcGxpY2F0aW9uXG4gICAqIHdpdGggT3BlbklEIENvbm5lY3QgcGFyYW1ldGVycyBhZGRlZCBpbiBVUkwgZnJhZ21lbnQuIFRoaXMgaXMgZ2VuZXJhbGx5IHNhZmVyIGFuZFxuICAgKiByZWNvbW1lbmRlZCBvdmVyIHF1ZXJ5LlxuICAgKiAtIGZsb3c6IFNldCB0aGUgT3BlbklEIENvbm5lY3QgZmxvdy4gVmFsaWQgdmFsdWVzIGFyZSBzdGFuZGFyZCwgaW1wbGljaXQgb3IgaHlicmlkLlxuICAgKlxuICAgKiBlbmFibGVCZWFyZXJJbnRlcmNlcHRvcjpcbiAgICogRmxhZyB0byBpbmRpY2F0ZSBpZiB0aGUgYmVhcmVyIHdpbGwgYWRkZWQgdG8gdGhlIGF1dGhvcml6YXRpb24gaGVhZGVyLlxuICAgKlxuICAgKiBsb2FkVXNlclByb2ZpbGVJblN0YXJ0VXA6XG4gICAqIEluZGljYXRlcyB0aGF0IHRoZSB1c2VyIHByb2ZpbGUgc2hvdWxkIGJlIGxvYWRlZCBhdCB0aGUga2V5Y2xvYWsgaW5pdGlhbGl6YXRpb24sXG4gICAqIGp1c3QgYWZ0ZXIgdGhlIGxvZ2luLlxuICAgKlxuICAgKiBiZWFyZXJFeGNsdWRlZFVybHM6XG4gICAqIFN0cmluZyBBcnJheSB0byBleGNsdWRlIHRoZSB1cmxzIHRoYXQgc2hvdWxkIG5vdCBoYXZlIHRoZSBBdXRob3JpemF0aW9uIEhlYWRlciBhdXRvbWF0aWNhbGx5XG4gICAqIGFkZGVkLlxuICAgKlxuICAgKiBhdXRob3JpemF0aW9uSGVhZGVyTmFtZTpcbiAgICogVGhpcyB2YWx1ZSB3aWxsIGJlIHVzZWQgYXMgdGhlIEF1dGhvcml6YXRpb24gSHR0cCBIZWFkZXIgbmFtZS5cbiAgICpcbiAgICogYmVhcmVyUHJlZml4OlxuICAgKiBUaGlzIHZhbHVlIHdpbGwgYmUgaW5jbHVkZWQgaW4gdGhlIEF1dGhvcml6YXRpb24gSHR0cCBIZWFkZXIgcGFyYW0uXG4gICAqXG4gICAqIEByZXR1cm5zXG4gICAqIEEgUHJvbWlzZSB3aXRoIGEgYm9vbGVhbiBpbmRpY2F0aW5nIGlmIHRoZSBpbml0aWFsaXphdGlvbiB3YXMgc3VjY2Vzc2Z1bC5cbiAgICovXG4gIGluaXQob3B0aW9uczogS2V5Y2xvYWtPcHRpb25zID0ge30pOiBQcm9taXNlPGJvb2xlYW4+IHtcbiAgICByZXR1cm4gbmV3IFByb21pc2UoKHJlc29sdmUsIHJlamVjdCkgPT4ge1xuICAgICAgdGhpcy5fZW5hYmxlQmVhcmVySW50ZXJjZXB0b3IgPSB0aGlzLmlmVW5kZWZpbmVkSXNUcnVlKG9wdGlvbnMuZW5hYmxlQmVhcmVySW50ZXJjZXB0b3IpO1xuICAgICAgdGhpcy5fbG9hZFVzZXJQcm9maWxlQXRTdGFydFVwID0gdGhpcy5pZlVuZGVmaW5lZElzVHJ1ZShvcHRpb25zLmxvYWRVc2VyUHJvZmlsZUF0U3RhcnRVcCk7XG4gICAgICB0aGlzLl9iZWFyZXJFeGNsdWRlZFVybHMgPSBvcHRpb25zLmJlYXJlckV4Y2x1ZGVkVXJscyB8fCBbXTtcbiAgICAgIHRoaXMuX2F1dGhvcml6YXRpb25IZWFkZXJOYW1lID0gb3B0aW9ucy5hdXRob3JpemF0aW9uSGVhZGVyTmFtZSB8fCAnQXV0aG9yaXphdGlvbic7XG4gICAgICB0aGlzLl9iZWFyZXJQcmVmaXggPSB0aGlzLnNhbml0aXplQmVhcmVyUHJlZml4KG9wdGlvbnMuYmVhcmVyUHJlZml4KTtcbiAgICAgIHRoaXMuX3NpbGVudFJlZnJlc2ggPSBvcHRpb25zLmluaXRPcHRpb25zID8gb3B0aW9ucy5pbml0T3B0aW9ucy5mbG93ID09PSAnaW1wbGljaXQnIDogZmFsc2U7XG4gICAgICB0aGlzLl9pbnN0YW5jZSA9IEtleWNsb2FrKG9wdGlvbnMuY29uZmlnKTtcbiAgICAgIHRoaXMuYmluZHNLZXljbG9ha0V2ZW50cygpO1xuICAgICAgdGhpcy5faW5zdGFuY2VcbiAgICAgICAgLmluaXQob3B0aW9ucy5pbml0T3B0aW9ucyEpXG4gICAgICAgIC5zdWNjZXNzKGFzeW5jIGF1dGhlbnRpY2F0ZWQgPT4ge1xuICAgICAgICAgIGlmIChhdXRoZW50aWNhdGVkICYmIHRoaXMuX2xvYWRVc2VyUHJvZmlsZUF0U3RhcnRVcCkge1xuICAgICAgICAgICAgYXdhaXQgdGhpcy5sb2FkVXNlclByb2ZpbGUoKTtcbiAgICAgICAgICB9XG4gICAgICAgICAgcmVzb2x2ZShhdXRoZW50aWNhdGVkKTtcbiAgICAgICAgfSlcbiAgICAgICAgLmVycm9yKGVycm9yID0+IHtcbiAgICAgICAgICByZWplY3QoJ0FuIGVycm9yIGhhcHBlbmVkIGR1cmluZyBLZXljbG9hayBpbml0aWFsaXphdGlvbi4nKTtcbiAgICAgICAgfSk7XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogUmVkaXJlY3RzIHRvIGxvZ2luIGZvcm0gb24gKG9wdGlvbnMgaXMgYW4gb3B0aW9uYWwgb2JqZWN0IHdpdGggcmVkaXJlY3RVcmkgYW5kL29yXG4gICAqIHByb21wdCBmaWVsZHMpLlxuICAgKlxuICAgKiBAcGFyYW0gb3B0aW9uc1xuICAgKiBPYmplY3QsIHdoZXJlOlxuICAgKiAgLSByZWRpcmVjdFVyaTogU3BlY2lmaWVzIHRoZSB1cmkgdG8gcmVkaXJlY3QgdG8gYWZ0ZXIgbG9naW4uXG4gICAqICAtIHByb21wdDpCeSBkZWZhdWx0IHRoZSBsb2dpbiBzY3JlZW4gaXMgZGlzcGxheWVkIGlmIHRoZSB1c2VyIGlzIG5vdCBsb2dnZWQtaW4gdG8gS2V5Y2xvYWsuXG4gICAqIFRvIG9ubHkgYXV0aGVudGljYXRlIHRvIHRoZSBhcHBsaWNhdGlvbiBpZiB0aGUgdXNlciBpcyBhbHJlYWR5IGxvZ2dlZC1pbiBhbmQgbm90IGRpc3BsYXkgdGhlXG4gICAqIGxvZ2luIHBhZ2UgaWYgdGhlIHVzZXIgaXMgbm90IGxvZ2dlZC1pbiwgc2V0IHRoaXMgb3B0aW9uIHRvIG5vbmUuIFRvIGFsd2F5cyByZXF1aXJlXG4gICAqIHJlLWF1dGhlbnRpY2F0aW9uIGFuZCBpZ25vcmUgU1NPLCBzZXQgdGhpcyBvcHRpb24gdG8gbG9naW4gLlxuICAgKiAgLSBtYXhBZ2U6IFVzZWQganVzdCBpZiB1c2VyIGlzIGFscmVhZHkgYXV0aGVudGljYXRlZC4gU3BlY2lmaWVzIG1heGltdW0gdGltZSBzaW5jZSB0aGVcbiAgICogYXV0aGVudGljYXRpb24gb2YgdXNlciBoYXBwZW5lZC4gSWYgdXNlciBpcyBhbHJlYWR5IGF1dGhlbnRpY2F0ZWQgZm9yIGxvbmdlciB0aW1lIHRoYW5cbiAgICogbWF4QWdlLCB0aGUgU1NPIGlzIGlnbm9yZWQgYW5kIGhlIHdpbGwgbmVlZCB0byByZS1hdXRoZW50aWNhdGUgYWdhaW4uXG4gICAqICAtIGxvZ2luSGludDogVXNlZCB0byBwcmUtZmlsbCB0aGUgdXNlcm5hbWUvZW1haWwgZmllbGQgb24gdGhlIGxvZ2luIGZvcm0uXG4gICAqICAtIGFjdGlvbjogSWYgdmFsdWUgaXMgJ3JlZ2lzdGVyJyB0aGVuIHVzZXIgaXMgcmVkaXJlY3RlZCB0byByZWdpc3RyYXRpb24gcGFnZSwgb3RoZXJ3aXNlIHRvXG4gICAqIGxvZ2luIHBhZ2UuXG4gICAqICAtIGxvY2FsZTogU3BlY2lmaWVzIHRoZSBkZXNpcmVkIGxvY2FsZSBmb3IgdGhlIFVJLlxuICAgKiBAcmV0dXJuc1xuICAgKiBBIHZvaWQgUHJvbWlzZSBpZiB0aGUgbG9naW4gaXMgc3VjY2Vzc2Z1bCBhbmQgYWZ0ZXIgdGhlIHVzZXIgcHJvZmlsZSBsb2FkaW5nLlxuICAgKi9cbiAgbG9naW4ob3B0aW9uczogS2V5Y2xvYWsuS2V5Y2xvYWtMb2dpbk9wdGlvbnMgPSB7fSk6IFByb21pc2U8dm9pZD4ge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZSgocmVzb2x2ZSwgcmVqZWN0KSA9PiB7XG4gICAgICB0aGlzLl9pbnN0YW5jZVxuICAgICAgICAubG9naW4ob3B0aW9ucylcbiAgICAgICAgLnN1Y2Nlc3MoYXN5bmMgKCkgPT4ge1xuICAgICAgICAgIGlmICh0aGlzLl9sb2FkVXNlclByb2ZpbGVBdFN0YXJ0VXApIHtcbiAgICAgICAgICAgIGF3YWl0IHRoaXMubG9hZFVzZXJQcm9maWxlKCk7XG4gICAgICAgICAgfVxuICAgICAgICAgIHJlc29sdmUoKTtcbiAgICAgICAgfSlcbiAgICAgICAgLmVycm9yKGVycm9yID0+IHtcbiAgICAgICAgICByZWplY3QoJ0FuIGVycm9yIGhhcHBlbmVkIGR1cmluZyB0aGUgbG9naW4uJyk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIFJlZGlyZWN0cyB0byBsb2dvdXQuXG4gICAqXG4gICAqIEBwYXJhbSByZWRpcmVjdFVyaVxuICAgKiBTcGVjaWZpZXMgdGhlIHVyaSB0byByZWRpcmVjdCB0byBhZnRlciBsb2dvdXQuXG4gICAqIEByZXR1cm5zXG4gICAqIEEgdm9pZCBQcm9taXNlIGlmIHRoZSBsb2dvdXQgd2FzIHN1Y2Nlc3NmdWwsIGNsZWFuaW5nIGFsc28gdGhlIHVzZXJQcm9maWxlLlxuICAgKi9cbiAgbG9nb3V0KHJlZGlyZWN0VXJpPzogc3RyaW5nKTogUHJvbWlzZTx2b2lkPiB7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKChyZXNvbHZlLCByZWplY3QpID0+IHtcbiAgICAgIGNvbnN0IG9wdGlvbnM6IGFueSA9IHtcbiAgICAgICAgcmVkaXJlY3RVcmlcbiAgICAgIH07XG5cbiAgICAgIHRoaXMuX2luc3RhbmNlXG4gICAgICAgIC5sb2dvdXQob3B0aW9ucylcbiAgICAgICAgLnN1Y2Nlc3MoKCkgPT4ge1xuICAgICAgICAgIHRoaXMuX3VzZXJQcm9maWxlID0gdW5kZWZpbmVkITtcbiAgICAgICAgICByZXNvbHZlKCk7XG4gICAgICAgIH0pXG4gICAgICAgIC5lcnJvcihlcnJvciA9PiB7XG4gICAgICAgICAgcmVqZWN0KCdBbiBlcnJvciBoYXBwZW5lZCBkdXJpbmcgbG9nb3V0LicpO1xuICAgICAgICB9KTtcbiAgICB9KTtcbiAgfVxuXG4gIC8qKlxuICAgKiBSZWRpcmVjdHMgdG8gcmVnaXN0cmF0aW9uIGZvcm0uIFNob3J0Y3V0IGZvciBsb2dpbiB3aXRoIG9wdGlvblxuICAgKiBhY3Rpb24gPSAncmVnaXN0ZXInLiBPcHRpb25zIGFyZSBzYW1lIGFzIGZvciB0aGUgbG9naW4gbWV0aG9kIGJ1dCAnYWN0aW9uJyBpcyBzZXQgdG9cbiAgICogJ3JlZ2lzdGVyJy5cbiAgICpcbiAgICogQHBhcmFtIG9wdGlvbnNcbiAgICogbG9naW4gb3B0aW9uc1xuICAgKiBAcmV0dXJuc1xuICAgKiBBIHZvaWQgUHJvbWlzZSBpZiB0aGUgcmVnaXN0ZXIgZmxvdyB3YXMgc3VjY2Vzc2Z1bC5cbiAgICovXG4gIHJlZ2lzdGVyKG9wdGlvbnM6IEtleWNsb2FrLktleWNsb2FrTG9naW5PcHRpb25zID0geyBhY3Rpb246ICdyZWdpc3RlcicgfSk6IFByb21pc2U8dm9pZD4ge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZSgocmVzb2x2ZSwgcmVqZWN0KSA9PiB7XG4gICAgICB0aGlzLl9pbnN0YW5jZVxuICAgICAgICAucmVnaXN0ZXIob3B0aW9ucylcbiAgICAgICAgLnN1Y2Nlc3MoKCkgPT4ge1xuICAgICAgICAgIHJlc29sdmUoKTtcbiAgICAgICAgfSlcbiAgICAgICAgLmVycm9yKCgpID0+IHtcbiAgICAgICAgICByZWplY3QoJ0FuIGVycm9yIGhhcHBlbmVkIGR1cmluZyB0aGUgcmVnaXN0ZXIgZXhlY3V0aW9uJyk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIENoZWNrIGlmIHRoZSB1c2VyIGhhcyBhY2Nlc3MgdG8gdGhlIHNwZWNpZmllZCByb2xlLiBJdCB3aWxsIGxvb2sgZm9yIHJvbGVzIGluXG4gICAqIHJlYWxtIGFuZCBjbGllbnRJZCwgYnV0IHdpbGwgbm90IGNoZWNrIGlmIHRoZSB1c2VyIGlzIGxvZ2dlZCBpbiBmb3IgYmV0dGVyIHBlcmZvcm1hbmNlLlxuICAgKlxuICAgKiBAcGFyYW0gcm9sZVxuICAgKiByb2xlIG5hbWVcbiAgICogQHJldHVybnNcbiAgICogQSBib29sZWFuIG1lYW5pbmcgaWYgdGhlIHVzZXIgaGFzIHRoZSBzcGVjaWZpZWQgUm9sZS5cbiAgICovXG4gIGlzVXNlckluUm9sZShyb2xlOiBzdHJpbmcpOiBib29sZWFuIHtcbiAgICBsZXQgaGFzUm9sZTogYm9vbGVhbjtcbiAgICBoYXNSb2xlID0gdGhpcy5faW5zdGFuY2UuaGFzUmVzb3VyY2VSb2xlKHJvbGUpO1xuICAgIGlmICghaGFzUm9sZSkge1xuICAgICAgaGFzUm9sZSA9IHRoaXMuX2luc3RhbmNlLmhhc1JlYWxtUm9sZShyb2xlKTtcbiAgICB9XG4gICAgcmV0dXJuIGhhc1JvbGU7XG4gIH1cblxuICAvKipcbiAgICogUmV0dXJuIHRoZSByb2xlcyBvZiB0aGUgbG9nZ2VkIHVzZXIuIFRoZSBhbGxSb2xlcyBwYXJhbWV0ZXIsIHdpdGggZGVmYXVsdCB2YWx1ZVxuICAgKiB0cnVlLCB3aWxsIHJldHVybiB0aGUgY2xpZW50SWQgYW5kIHJlYWxtIHJvbGVzIGFzc29jaWF0ZWQgd2l0aCB0aGUgbG9nZ2VkIHVzZXIuIElmIHNldCB0byBmYWxzZVxuICAgKiBpdCB3aWxsIG9ubHkgcmV0dXJuIHRoZSB1c2VyIHJvbGVzIGFzc29jaWF0ZWQgd2l0aCB0aGUgY2xpZW50SWQuXG4gICAqXG4gICAqIEBwYXJhbSBhbGxSb2xlc1xuICAgKiBGbGFnIHRvIHNldCBpZiBhbGwgcm9sZXMgc2hvdWxkIGJlIHJldHVybmVkLihPcHRpb25hbDogZGVmYXVsdCB2YWx1ZSBpcyB0cnVlKVxuICAgKiBAcmV0dXJuc1xuICAgKiBBcnJheSBvZiBSb2xlcyBhc3NvY2lhdGVkIHdpdGggdGhlIGxvZ2dlZCB1c2VyLlxuICAgKi9cbiAgZ2V0VXNlclJvbGVzKGFsbFJvbGVzOiBib29sZWFuID0gdHJ1ZSk6IHN0cmluZ1tdIHtcbiAgICBsZXQgcm9sZXM6IHN0cmluZ1tdID0gW107XG4gICAgaWYgKHRoaXMuX2luc3RhbmNlLnJlc291cmNlQWNjZXNzKSB7XG4gICAgICBmb3IgKGNvbnN0IGtleSBpbiB0aGlzLl9pbnN0YW5jZS5yZXNvdXJjZUFjY2Vzcykge1xuICAgICAgICBpZiAodGhpcy5faW5zdGFuY2UucmVzb3VyY2VBY2Nlc3MuaGFzT3duUHJvcGVydHkoa2V5KSkge1xuICAgICAgICAgIGNvbnN0IHJlc291cmNlQWNjZXNzOiBhbnkgPSB0aGlzLl9pbnN0YW5jZS5yZXNvdXJjZUFjY2Vzc1trZXldO1xuICAgICAgICAgIGNvbnN0IGNsaWVudFJvbGVzID0gcmVzb3VyY2VBY2Nlc3NbJ3JvbGVzJ10gfHwgW107XG4gICAgICAgICAgcm9sZXMgPSByb2xlcy5jb25jYXQoY2xpZW50Um9sZXMpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuICAgIGlmIChhbGxSb2xlcyAmJiB0aGlzLl9pbnN0YW5jZS5yZWFsbUFjY2Vzcykge1xuICAgICAgbGV0IHJlYWxtUm9sZXMgPSB0aGlzLl9pbnN0YW5jZS5yZWFsbUFjY2Vzc1sncm9sZXMnXSB8fCBbXTtcbiAgICAgIHJvbGVzLnB1c2goLi4ucmVhbG1Sb2xlcyk7XG4gICAgfVxuICAgIHJldHVybiByb2xlcztcbiAgfVxuXG4gIC8qKlxuICAgKiBDaGVjayBpZiB1c2VyIGlzIGxvZ2dlZCBpbi5cbiAgICpcbiAgICogQHJldHVybnNcbiAgICogQSBib29sZWFuIHRoYXQgaW5kaWNhdGVzIGlmIHRoZSB1c2VyIGlzIGxvZ2dlZCBpbi5cbiAgICovXG4gIGlzTG9nZ2VkSW4oKTogUHJvbWlzZTxib29sZWFuPiB7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKGFzeW5jIChyZXNvbHZlLCByZWplY3QpID0+IHtcbiAgICAgIHRyeSB7XG4gICAgICAgIGF3YWl0IHRoaXMudXBkYXRlVG9rZW4oMjApO1xuICAgICAgICByZXNvbHZlKHRydWUpO1xuICAgICAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICAgICAgcmVzb2x2ZShmYWxzZSk7XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogUmV0dXJucyB0cnVlIGlmIHRoZSB0b2tlbiBoYXMgbGVzcyB0aGFuIG1pblZhbGlkaXR5IHNlY29uZHMgbGVmdCBiZWZvcmVcbiAgICogaXQgZXhwaXJlcy5cbiAgICpcbiAgICogQHBhcmFtIG1pblZhbGlkaXR5XG4gICAqIFNlY29uZHMgbGVmdC4gKG1pblZhbGlkaXR5KSBpcyBvcHRpb25hbC4gRGVmYXVsdCB2YWx1ZSBpcyAwLlxuICAgKiBAcmV0dXJuc1xuICAgKiBCb29sZWFuIGluZGljYXRpbmcgaWYgdGhlIHRva2VuIGlzIGV4cGlyZWQuXG4gICAqL1xuICBpc1Rva2VuRXhwaXJlZChtaW5WYWxpZGl0eTogbnVtYmVyID0gMCk6IGJvb2xlYW4ge1xuICAgIHJldHVybiB0aGlzLl9pbnN0YW5jZS5pc1Rva2VuRXhwaXJlZChtaW5WYWxpZGl0eSk7XG4gIH1cblxuICAvKipcbiAgICogSWYgdGhlIHRva2VuIGV4cGlyZXMgd2l0aGluIG1pblZhbGlkaXR5IHNlY29uZHMgdGhlIHRva2VuIGlzIHJlZnJlc2hlZC4gSWYgdGhlXG4gICAqIHNlc3Npb24gc3RhdHVzIGlmcmFtZSBpcyBlbmFibGVkLCB0aGUgc2Vzc2lvbiBzdGF0dXMgaXMgYWxzbyBjaGVja2VkLlxuICAgKiBSZXR1cm5zIGEgcHJvbWlzZSB0ZWxsaW5nIGlmIHRoZSB0b2tlbiB3YXMgcmVmcmVzaGVkIG9yIG5vdC4gSWYgdGhlIHNlc3Npb24gaXMgbm90IGFjdGl2ZVxuICAgKiBhbnltb3JlLCB0aGUgcHJvbWlzZSBpcyByZWplY3RlZC5cbiAgICpcbiAgICogQHBhcmFtIG1pblZhbGlkaXR5XG4gICAqIFNlY29uZHMgbGVmdC4gKG1pblZhbGlkaXR5IGlzIG9wdGlvbmFsLCBpZiBub3Qgc3BlY2lmaWVkIDUgaXMgdXNlZClcbiAgICogQHJldHVybnNcbiAgICogUHJvbWlzZSB3aXRoIGEgYm9vbGVhbiBpbmRpY2F0aW5nIGlmIHRoZSB0b2tlbiB3YXMgc3VjY2VzZnVsbHkgdXBkYXRlZC5cbiAgICovXG4gIHVwZGF0ZVRva2VuKG1pblZhbGlkaXR5OiBudW1iZXIgPSA1KTogUHJvbWlzZTxib29sZWFuPiB7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKGFzeW5jIChyZXNvbHZlLCByZWplY3QpID0+IHtcbiAgICAgIC8vIFRPRE86IHRoaXMgaXMgYSB3b3JrYXJvdW5kIHVudGlsIHRoZSBzaWxlbnQgcmVmcmVzaCAoaXNzdWUgIzQzKVxuICAgICAgLy8gaXMgbm90IGltcGxlbWVudGVkLCBhdm9pZGluZyB0aGUgcmVkaXJlY3QgbG9vcC5cbiAgICAgIGlmICh0aGlzLl9zaWxlbnRSZWZyZXNoKSB7XG4gICAgICAgIGlmICh0aGlzLmlzVG9rZW5FeHBpcmVkKCkpIHtcbiAgICAgICAgICByZWplY3QoJ0ZhaWxlZCB0byByZWZyZXNoIHRoZSB0b2tlbiwgb3IgdGhlIHNlc3Npb24gaXMgZXhwaXJlZCcpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIHJlc29sdmUodHJ1ZSk7XG4gICAgICAgIH1cbiAgICAgICAgcmV0dXJuO1xuICAgICAgfVxuXG4gICAgICBpZiAoIXRoaXMuX2luc3RhbmNlKSB7XG4gICAgICAgIHJlamVjdCgpO1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG5cbiAgICAgIHRoaXMuX2luc3RhbmNlXG4gICAgICAgIC51cGRhdGVUb2tlbihtaW5WYWxpZGl0eSlcbiAgICAgICAgLnN1Y2Nlc3MocmVmcmVzaGVkID0+IHtcbiAgICAgICAgICByZXNvbHZlKHJlZnJlc2hlZCk7XG4gICAgICAgIH0pXG4gICAgICAgIC5lcnJvcihlcnJvciA9PiB7XG4gICAgICAgICAgcmVqZWN0KCdGYWlsZWQgdG8gcmVmcmVzaCB0aGUgdG9rZW4sIG9yIHRoZSBzZXNzaW9uIGlzIGV4cGlyZWQnKTtcbiAgICAgICAgfSk7XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogTG9hZHMgdGhlIHVzZXJzIHByb2ZpbGUuXG4gICAqIFJldHVybnMgcHJvbWlzZSB0byBzZXQgZnVuY3Rpb25zIHRvIGJlIGludm9rZWQgaWYgdGhlIHByb2ZpbGUgd2FzIGxvYWRlZCBzdWNjZXNzZnVsbHksIG9yIGlmXG4gICAqIHRoZSBwcm9maWxlIGNvdWxkIG5vdCBiZSBsb2FkZWQuXG4gICAqXG4gICAqIEBwYXJhbSBmb3JjZVJlbG9hZFxuICAgKiBJZiB0cnVlIHdpbGwgZm9yY2UgdGhlIGxvYWRVc2VyUHJvZmlsZSBldmVuIGlmIGl0cyBhbHJlYWR5IGxvYWRlZC5cbiAgICogQHJldHVybnNcbiAgICogQSBwcm9taXNlIHdpdGggdGhlIEtleWNsb2FrUHJvZmlsZSBkYXRhIGxvYWRlZC5cbiAgICovXG4gIGxvYWRVc2VyUHJvZmlsZShmb3JjZVJlbG9hZDogYm9vbGVhbiA9IGZhbHNlKTogUHJvbWlzZTxLZXljbG9hay5LZXljbG9ha1Byb2ZpbGU+IHtcbiAgICByZXR1cm4gbmV3IFByb21pc2UoYXN5bmMgKHJlc29sdmUsIHJlamVjdCkgPT4ge1xuICAgICAgaWYgKHRoaXMuX3VzZXJQcm9maWxlICYmICFmb3JjZVJlbG9hZCkge1xuICAgICAgICByZXNvbHZlKHRoaXMuX3VzZXJQcm9maWxlKTtcbiAgICAgICAgcmV0dXJuO1xuICAgICAgfVxuXG4gICAgICBpZiAoIShhd2FpdCB0aGlzLmlzTG9nZ2VkSW4oKSkpIHtcbiAgICAgICAgcmVqZWN0KCdUaGUgdXNlciBwcm9maWxlIHdhcyBub3QgbG9hZGVkIGFzIHRoZSB1c2VyIGlzIG5vdCBsb2dnZWQgaW4uJyk7XG4gICAgICAgIHJldHVybjtcbiAgICAgIH1cblxuICAgICAgdGhpcy5faW5zdGFuY2VcbiAgICAgICAgLmxvYWRVc2VyUHJvZmlsZSgpXG4gICAgICAgIC5zdWNjZXNzKHJlc3VsdCA9PiB7XG4gICAgICAgICAgdGhpcy5fdXNlclByb2ZpbGUgPSByZXN1bHQgYXMgS2V5Y2xvYWsuS2V5Y2xvYWtQcm9maWxlO1xuICAgICAgICAgIHJlc29sdmUodGhpcy5fdXNlclByb2ZpbGUpO1xuICAgICAgICB9KVxuICAgICAgICAuZXJyb3IoZXJyID0+IHtcbiAgICAgICAgICByZWplY3QoJ1RoZSB1c2VyIHByb2ZpbGUgY291bGQgbm90IGJlIGxvYWRlZC4nKTtcbiAgICAgICAgfSk7XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogUmV0dXJucyB0aGUgYXV0aGVudGljYXRlZCB0b2tlbiwgY2FsbGluZyB1cGRhdGVUb2tlbiB0byBnZXQgYSByZWZyZXNoZWQgb25lIGlmXG4gICAqIG5lY2Vzc2FyeS4gSWYgdGhlIHNlc3Npb24gaXMgZXhwaXJlZCB0aGlzIG1ldGhvZCBjYWxscyB0aGUgbG9naW4gbWV0aG9kIGZvciBhIG5ldyBsb2dpbi5cbiAgICpcbiAgICogQHJldHVybnNcbiAgICogUHJvbWlzZSB3aXRoIHRoZSBnZW5lcmF0ZWQgdG9rZW4uXG4gICAqL1xuICBnZXRUb2tlbigpOiBQcm9taXNlPHN0cmluZz4ge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZShhc3luYyAocmVzb2x2ZSwgcmVqZWN0KSA9PiB7XG4gICAgICB0cnkge1xuICAgICAgICBhd2FpdCB0aGlzLnVwZGF0ZVRva2VuKDEwKTtcbiAgICAgICAgcmVzb2x2ZSh0aGlzLl9pbnN0YW5jZS50b2tlbik7XG4gICAgICB9IGNhdGNoIChlcnJvcikge1xuICAgICAgICB0aGlzLmxvZ2luKCk7XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogUmV0dXJucyB0aGUgbG9nZ2VkIHVzZXJuYW1lLlxuICAgKlxuICAgKiBAcmV0dXJuc1xuICAgKiBUaGUgbG9nZ2VkIHVzZXJuYW1lLlxuICAgKi9cbiAgZ2V0VXNlcm5hbWUoKTogc3RyaW5nIHtcbiAgICBpZiAoIXRoaXMuX3VzZXJQcm9maWxlKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoJ1VzZXIgbm90IGxvZ2dlZCBpbiBvciB1c2VyIHByb2ZpbGUgd2FzIG5vdCBsb2FkZWQuJyk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMuX3VzZXJQcm9maWxlLnVzZXJuYW1lITtcbiAgfVxuXG4gIC8qKlxuICAgKiBDbGVhciBhdXRoZW50aWNhdGlvbiBzdGF0ZSwgaW5jbHVkaW5nIHRva2Vucy4gVGhpcyBjYW4gYmUgdXNlZnVsIGlmIGFwcGxpY2F0aW9uXG4gICAqIGhhcyBkZXRlY3RlZCB0aGUgc2Vzc2lvbiB3YXMgZXhwaXJlZCwgZm9yIGV4YW1wbGUgaWYgdXBkYXRpbmcgdG9rZW4gZmFpbHMuXG4gICAqIEludm9raW5nIHRoaXMgcmVzdWx0cyBpbiBvbkF1dGhMb2dvdXQgY2FsbGJhY2sgbGlzdGVuZXIgYmVpbmcgaW52b2tlZC5cbiAgICovXG4gIGNsZWFyVG9rZW4oKTogdm9pZCB7XG4gICAgdGhpcy5faW5zdGFuY2UuY2xlYXJUb2tlbigpO1xuICB9XG5cbiAgLyoqXG4gICAqIEFkZHMgYSB2YWxpZCB0b2tlbiBpbiBoZWFkZXIuIFRoZSBrZXkgJiB2YWx1ZSBmb3JtYXQgaXM6XG4gICAqIEF1dGhvcml6YXRpb24gQmVhcmVyIDx0b2tlbj4uXG4gICAqIElmIHRoZSBoZWFkZXJzIHBhcmFtIGlzIHVuZGVmaW5lZCBpdCB3aWxsIGNyZWF0ZSB0aGUgQW5ndWxhciBoZWFkZXJzIG9iamVjdC5cbiAgICpcbiAgICogQHBhcmFtIGhlYWRlcnNcbiAgICogVXBkYXRlZCBoZWFkZXIgd2l0aCBBdXRob3JpemF0aW9uIGFuZCBLZXljbG9hayB0b2tlbi5cbiAgICogQHJldHVybnNcbiAgICogQW4gb2JzZXJ2YWJsZSB3aXRoIHdpdGggdGhlIEhUVFAgQXV0aG9yaXphdGlvbiBoZWFkZXIgYW5kIHRoZSBjdXJyZW50IHRva2VuLlxuICAgKi9cbiAgYWRkVG9rZW5Ub0hlYWRlcihoZWFkZXJzQXJnPzogSHR0cEhlYWRlcnMpOiBPYnNlcnZhYmxlPEh0dHBIZWFkZXJzPiB7XG4gICAgcmV0dXJuIE9ic2VydmFibGUuY3JlYXRlKGFzeW5jIChvYnNlcnZlcjogT2JzZXJ2ZXI8YW55PikgPT4ge1xuICAgICAgbGV0IGhlYWRlcnMgPSBoZWFkZXJzQXJnO1xuICAgICAgaWYgKCFoZWFkZXJzKSB7XG4gICAgICAgIGhlYWRlcnMgPSBuZXcgSHR0cEhlYWRlcnMoKTtcbiAgICAgIH1cbiAgICAgIHRyeSB7XG4gICAgICAgIGNvbnN0IHRva2VuOiBzdHJpbmcgPSBhd2FpdCB0aGlzLmdldFRva2VuKCk7XG4gICAgICAgIGhlYWRlcnMgPSBoZWFkZXJzLnNldCh0aGlzLl9hdXRob3JpemF0aW9uSGVhZGVyTmFtZSwgdGhpcy5fYmVhcmVyUHJlZml4ICsgdG9rZW4pO1xuICAgICAgICBvYnNlcnZlci5uZXh0KGhlYWRlcnMpO1xuICAgICAgICBvYnNlcnZlci5jb21wbGV0ZSgpO1xuICAgICAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICAgICAgb2JzZXJ2ZXIuZXJyb3IoZXJyb3IpO1xuICAgICAgfVxuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybnMgdGhlIG9yaWdpbmFsIEtleWNsb2FrIGluc3RhbmNlLCBpZiB5b3UgbmVlZCBhbnkgY3VzdG9taXphdGlvbiB0aGF0XG4gICAqIHRoaXMgQW5ndWxhciBzZXJ2aWNlIGRvZXMgbm90IHN1cHBvcnQgeWV0LiBVc2Ugd2l0aCBjYXV0aW9uLlxuICAgKlxuICAgKiBAcmV0dXJuc1xuICAgKiBUaGUgS2V5Y2xvYWtJbnN0YW5jZSBmcm9tIGtleWNsb2FrLWpzLlxuICAgKi9cbiAgZ2V0S2V5Y2xvYWtJbnN0YW5jZSgpOiBLZXljbG9hay5LZXljbG9ha0luc3RhbmNlIHtcbiAgICByZXR1cm4gdGhpcy5faW5zdGFuY2U7XG4gIH1cblxuICAvKipcbiAgICogUmV0dXJucyB0aGUgZXhjbHVkZWQgVVJMcyB0aGF0IHNob3VsZCBub3QgYmUgY29uc2lkZXJlZCBieVxuICAgKiB0aGUgaHR0cCBpbnRlcmNlcHRvciB3aGljaCBhdXRvbWF0aWNhbGx5IGFkZHMgdGhlIGF1dGhvcml6YXRpb24gaGVhZGVyIGluIHRoZSBIdHRwIFJlcXVlc3QuXG4gICAqXG4gICAqIEByZXR1cm5zXG4gICAqIFRoZSBleGNsdWRlZCB1cmxzIHRoYXQgbXVzdCBub3QgYmUgaW50ZXJjZXB0ZWQgYnkgdGhlIEtleWNsb2FrQmVhcmVySW50ZXJjZXB0b3IuXG4gICAqL1xuICBnZXQgYmVhcmVyRXhjbHVkZWRVcmxzKCk6IHN0cmluZ1tdIHtcbiAgICByZXR1cm4gdGhpcy5fYmVhcmVyRXhjbHVkZWRVcmxzO1xuICB9XG5cbiAgLyoqXG4gICAqIEZsYWcgdG8gaW5kaWNhdGUgaWYgdGhlIGJlYXJlciB3aWxsIGJlIGFkZGVkIHRvIHRoZSBhdXRob3JpemF0aW9uIGhlYWRlci5cbiAgICpcbiAgICogQHJldHVybnNcbiAgICogUmV0dXJucyBpZiB0aGUgYmVhcmVyIGludGVyY2VwdG9yIHdhcyBzZXQgdG8gYmUgZGlzYWJsZWQuXG4gICAqL1xuICBnZXQgZW5hYmxlQmVhcmVySW50ZXJjZXB0b3IoKTogYm9vbGVhbiB7XG4gICAgcmV0dXJuIHRoaXMuX2VuYWJsZUJlYXJlckludGVyY2VwdG9yO1xuICB9XG5cbiAgLyoqXG4gICAqIEtleWNsb2FrIHN1YmplY3QgdG8gbW9uaXRvciB0aGUgZXZlbnRzIHRyaWdnZXJlZCBieSBrZXljbG9hay1qcy5cbiAgICogVGhlIGZvbGxvd2luZyBldmVudHMgYXMgYXZhaWxhYmxlIChhcyBkZXNjcmliZWQgYXQga2V5Y2xvYWsgZG9jcyAtXG4gICAqIGh0dHBzOi8vd3d3LmtleWNsb2FrLm9yZy9kb2NzL2xhdGVzdC9zZWN1cmluZ19hcHBzL2luZGV4Lmh0bWwjY2FsbGJhY2stZXZlbnRzKTpcbiAgICogLSBPbkF1dGhFcnJvclxuICAgKiAtIE9uQXV0aExvZ291dFxuICAgKiAtIE9uQXV0aFJlZnJlc2hFcnJvclxuICAgKiAtIE9uQXV0aFJlZnJlc2hTdWNjZXNzXG4gICAqIC0gT25BdXRoU3VjY2Vzc1xuICAgKiAtIE9uUmVhZHlcbiAgICogLSBPblRva2VuRXhwaXJlXG4gICAqIEluIGVhY2ggb2NjdXJyZW5jZSBvZiBhbnkgb2YgdGhlc2UsIHRoaXMgc3ViamVjdCB3aWxsIHJldHVybiB0aGUgZXZlbnQgdHlwZSxcbiAgICogZGVzY3JpYmVkIGF0IHtAbGluayBLZXljbG9ha0V2ZW50VHlwZX0gZW51bSBhbmQgdGhlIGZ1bmN0aW9uIGFyZ3MgZnJvbSB0aGUga2V5Y2xvYWstanNcbiAgICogaWYgcHJvdmlkZWQgYW55LlxuICAgKlxuICAgKiBAcmV0dXJuc1xuICAgKiBBIHN1YmplY3Qgd2l0aCB0aGUge0BsaW5rIEtleWNsb2FrRXZlbnR9IHdoaWNoIGRlc2NyaWJlcyB0aGUgZXZlbnQgdHlwZSBhbmQgYXR0YWNoZXMgdGhlXG4gICAqIGZ1bmN0aW9uIGFyZ3MuXG4gICAqL1xuICBnZXQga2V5Y2xvYWtFdmVudHMkKCk6IFN1YmplY3Q8S2V5Y2xvYWtFdmVudD4ge1xuICAgIHJldHVybiB0aGlzLl9rZXljbG9ha0V2ZW50cyQ7XG4gIH1cbn1cbiIsIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBNYXVyaWNpbyBHZW1lbGxpIFZpZ29sbyBhbmQgY29udHJpYnV0b3JzLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGEgTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9naXRodWIuY29tL21hdXJpY2lvdmlnb2xvL2tleWNsb2FrLWFuZ3VsYXIvTElDRU5TRVxuICovXG5cbmltcG9ydCB7IEluamVjdGFibGUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7XG4gIEh0dHBJbnRlcmNlcHRvcixcbiAgSHR0cFJlcXVlc3QsXG4gIEh0dHBIYW5kbGVyLFxuICBIdHRwRXZlbnQsXG4gIEh0dHBIZWFkZXJzXG59IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbi9odHRwJztcblxuaW1wb3J0IHsgT2JzZXJ2YWJsZSB9IGZyb20gJ3J4anMnO1xuaW1wb3J0IHsgbWVyZ2VNYXAgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbmltcG9ydCB7IEtleWNsb2FrU2VydmljZSB9IGZyb20gJy4uL3NlcnZpY2VzL2tleWNsb2FrLnNlcnZpY2UnO1xuXG4vKipcbiAqIFRoaXMgaW50ZXJjZXB0b3IgaW5jbHVkZXMgdGhlIGJlYXJlciBieSBkZWZhdWx0IGluIGFsbCBIdHRwQ2xpZW50IHJlcXVlc3RzLlxuICpcbiAqIElmIHlvdSBuZWVkIHRvIGV4Y2x1ZGUgc29tZSBVUkxzIGZyb20gYWRkaW5nIHRoZSBiZWFyZXIsIHBsZWFzZSwgdGFrZSBhIGxvb2tcbiAqIGF0IHRoZSB7QGxpbmsgS2V5Y2xvYWtPcHRpb25zfSBiZWFyZXJFeGNsdWRlZFVybHMgcHJvcGVydHkuXG4gKi9cbkBJbmplY3RhYmxlKClcbmV4cG9ydCBjbGFzcyBLZXljbG9ha0JlYXJlckludGVyY2VwdG9yIGltcGxlbWVudHMgSHR0cEludGVyY2VwdG9yIHtcbiAgcHJpdmF0ZSBleGNsdWRlZFVybHNSZWdleDogUmVnRXhwW107XG5cbiAgLyoqXG4gICAqIEtleWNsb2FrQmVhcmVySW50ZXJjZXB0b3IgY29uc3RydWN0b3IuXG4gICAqXG4gICAqIEBwYXJhbSBrZXljbG9hayAtIEluamVjdGVkIEtleWNsb2FrU2VydmljZSBpbnN0YW5jZS5cbiAgICovXG4gIGNvbnN0cnVjdG9yKHByaXZhdGUga2V5Y2xvYWs6IEtleWNsb2FrU2VydmljZSkge31cblxuICBwcml2YXRlIGxvYWRFeGNsdWRlZFVybHNSZWdleCgpIHtcbiAgICBjb25zdCBleGNsdWRlZFVybHM6IHN0cmluZ1tdID0gdGhpcy5rZXljbG9hay5iZWFyZXJFeGNsdWRlZFVybHM7XG4gICAgdGhpcy5leGNsdWRlZFVybHNSZWdleCA9IGV4Y2x1ZGVkVXJscy5tYXAodXJsUGF0dGVybiA9PiBuZXcgUmVnRXhwKHVybFBhdHRlcm4sICdpJykpIHx8IFtdO1xuICB9XG5cbiAgLyoqXG4gICAqIEludGVyY2VwdCBpbXBsZW1lbnRhdGlvbiB0aGF0IGNoZWNrcyBpZiB0aGUgcmVxdWVzdCB1cmwgbWF0Y2hlcyB0aGUgZXhjbHVkZWRVcmxzLlxuICAgKiBJZiBub3QsIGFkZHMgdGhlIEF1dGhvcml6YXRpb24gaGVhZGVyIHRvIHRoZSByZXF1ZXN0LlxuICAgKlxuICAgKiBAcGFyYW0gcmVxXG4gICAqIEBwYXJhbSBuZXh0XG4gICAqL1xuICBwdWJsaWMgaW50ZXJjZXB0KHJlcTogSHR0cFJlcXVlc3Q8YW55PiwgbmV4dDogSHR0cEhhbmRsZXIpOiBPYnNlcnZhYmxlPEh0dHBFdmVudDxhbnk+PiB7XG4gICAgLy8gSWYga2V5Y2xvYWsgc2VydmljZSBpcyBub3QgaW5pdGlhbGl6ZWQgeWV0LCBvciB0aGUgaW50ZXJjZXB0b3Igc2hvdWxkIG5vdCBiZSBleGVjdXRlXG4gICAgaWYgKCF0aGlzLmtleWNsb2FrIHx8ICF0aGlzLmtleWNsb2FrLmVuYWJsZUJlYXJlckludGVyY2VwdG9yKSB7XG4gICAgICByZXR1cm4gbmV4dC5oYW5kbGUocmVxKTtcbiAgICB9XG5cbiAgICBpZiAoIXRoaXMuZXhjbHVkZWRVcmxzUmVnZXgpIHtcbiAgICAgIHRoaXMubG9hZEV4Y2x1ZGVkVXJsc1JlZ2V4KCk7XG4gICAgfVxuXG4gICAgY29uc3QgdXJsUmVxdWVzdCA9IHJlcS51cmw7XG4gICAgY29uc3Qgc2hhbGxQYXNzOiBib29sZWFuID0gISF0aGlzLmV4Y2x1ZGVkVXJsc1JlZ2V4LmZpbmQocmVnZXggPT4gcmVnZXgudGVzdCh1cmxSZXF1ZXN0KSk7XG4gICAgaWYgKHNoYWxsUGFzcykge1xuICAgICAgcmV0dXJuIG5leHQuaGFuZGxlKHJlcSk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMua2V5Y2xvYWsuYWRkVG9rZW5Ub0hlYWRlcihyZXEuaGVhZGVycykucGlwZShcbiAgICAgIG1lcmdlTWFwKGhlYWRlcnNXaXRoQmVhcmVyID0+IHtcbiAgICAgICAgY29uc3Qga2NSZXEgPSByZXEuY2xvbmUoeyBoZWFkZXJzOiBoZWFkZXJzV2l0aEJlYXJlciB9KTtcbiAgICAgICAgcmV0dXJuIG5leHQuaGFuZGxlKGtjUmVxKTtcbiAgICAgIH0pXG4gICAgKTtcbiAgfVxufVxuIiwiLyoqXG4gKiBAbGljZW5zZVxuICogQ29weXJpZ2h0IE1hdXJpY2lvIEdlbWVsbGkgVmlnb2xvIGFuZCBjb250cmlidXRvcnMuXG4gKlxuICogVXNlIG9mIHRoaXMgc291cmNlIGNvZGUgaXMgZ292ZXJuZWQgYnkgYSBNSVQtc3R5bGUgbGljZW5zZSB0aGF0IGNhbiBiZVxuICogZm91bmQgaW4gdGhlIExJQ0VOU0UgZmlsZSBhdCBodHRwczovL2dpdGh1Yi5jb20vbWF1cmljaW92aWdvbG8va2V5Y2xvYWstYW5ndWxhci9MSUNFTlNFXG4gKi9cblxuaW1wb3J0IHsgTmdNb2R1bGUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7IENvbW1vbk1vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbic7XG5pbXBvcnQgeyBIVFRQX0lOVEVSQ0VQVE9SUyB9IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbi9odHRwJztcblxuaW1wb3J0IHsgS2V5Y2xvYWtTZXJ2aWNlIH0gZnJvbSAnLi9zZXJ2aWNlcy9rZXljbG9hay5zZXJ2aWNlJztcbmltcG9ydCB7IEtleWNsb2FrQmVhcmVySW50ZXJjZXB0b3IgfSBmcm9tICcuL2ludGVyY2VwdG9ycy9rZXljbG9hay1iZWFyZXIuaW50ZXJjZXB0b3InO1xuXG5ATmdNb2R1bGUoe1xuICBpbXBvcnRzOiBbQ29tbW9uTW9kdWxlXSxcbiAgcHJvdmlkZXJzOiBbXG4gICAgS2V5Y2xvYWtTZXJ2aWNlLFxuICAgIHtcbiAgICAgIHByb3ZpZGU6IEhUVFBfSU5URVJDRVBUT1JTLFxuICAgICAgdXNlQ2xhc3M6IEtleWNsb2FrQmVhcmVySW50ZXJjZXB0b3IsXG4gICAgICBtdWx0aTogdHJ1ZVxuICAgIH1cbiAgXVxufSlcbmV4cG9ydCBjbGFzcyBDb3JlTW9kdWxlIHt9XG4iLCIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgTWF1cmljaW8gR2VtZWxsaSBWaWdvbG8gYW5kIGNvbnRyaWJ1dG9ycy5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vZ2l0aHViLmNvbS9tYXVyaWNpb3ZpZ29sby9rZXljbG9hay1hbmd1bGFyL0xJQ0VOU0VcbiAqL1xuXG5pbXBvcnQgeyBOZ01vZHVsZSB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuXG5pbXBvcnQgeyBDb3JlTW9kdWxlIH0gZnJvbSAnLi9jb3JlL2NvcmUubW9kdWxlJztcblxuQE5nTW9kdWxlKHtcbiAgaW1wb3J0czogW0NvcmVNb2R1bGVdXG59KVxuZXhwb3J0IGNsYXNzIEtleWNsb2FrQW5ndWxhck1vZHVsZSB7fVxuIl0sIm5hbWVzIjpbIlN1YmplY3QiLCJPYnNlcnZhYmxlIiwiSHR0cEhlYWRlcnMiLCJJbmplY3RhYmxlIiwibWVyZ2VNYXAiLCJOZ01vZHVsZSIsIkNvbW1vbk1vZHVsZSIsIkhUVFBfSU5URVJDRVBUT1JTIl0sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7O1FBZ0JFLGNBQVc7Ozs7O1FBS1gsZUFBWTs7OztRQUlaLHFCQUFrQjs7OztRQUlsQix1QkFBb0I7Ozs7UUFJcEIsZ0JBQWE7Ozs7UUFJYixVQUFPOzs7Ozs7UUFNUCxpQkFBYzs7d0NBM0JkLFdBQVc7d0NBS1gsWUFBWTt3Q0FJWixrQkFBa0I7d0NBSWxCLG9CQUFvQjt3Q0FJcEIsYUFBYTt3Q0FJYixPQUFPO3dDQU1QLGNBQWM7O0lDM0NoQjs7Ozs7Ozs7Ozs7Ozs7QUFjQSx1QkFtRDBCLE9BQU8sRUFBRSxVQUFVLEVBQUUsQ0FBQyxFQUFFLFNBQVM7UUFDdkQsT0FBTyxLQUFLLENBQUMsS0FBSyxDQUFDLEdBQUcsT0FBTyxDQUFDLEVBQUUsVUFBVSxPQUFPLEVBQUUsTUFB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