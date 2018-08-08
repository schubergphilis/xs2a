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
import * as tslib_1 from "tslib";
import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import * as Keycloak_ from 'keycloak-js';
/** @type {?} */
export var Keycloak = Keycloak_;
import { Observable, Subject } from 'rxjs';
import { KeycloakEventType } from '../interfaces/keycloak-event';
/**
 * Service to expose existent methods from the Keycloak JS adapter, adding new
 * functionalities to improve the use of keycloak in Angular v > 4.3 applications.
 *
 * This class should be injected in the application bootstrap, so the same instance will be used
 * along the web application.
 */
var KeycloakService = /** @class */ (function () {
    function KeycloakService() {
        this._keycloakEvents$ = new Subject();
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
        if (options === void 0) { options = {}; }
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
                .success(function (authenticated) { return tslib_1.__awaiter(_this, void 0, void 0, function () {
                return tslib_1.__generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (!(authenticated && this._loadUserProfileAtStartUp)) return [3 /*break*/, 2];
                            return [4 /*yield*/, this.loadUserProfile()];
                        case 1:
                            _a.sent();
                            _a.label = 2;
                        case 2:
                            resolve(authenticated);
                            return [2 /*return*/];
                    }
                });
            }); })
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
        if (options === void 0) { options = {}; }
        return new Promise(function (resolve, reject) {
            _this._instance
                .login(options)
                .success(function () { return tslib_1.__awaiter(_this, void 0, void 0, function () {
                return tslib_1.__generator(this, function (_a) {
                    switch (_a.label) {
                        case 0:
                            if (!this._loadUserProfileAtStartUp) return [3 /*break*/, 2];
                            return [4 /*yield*/, this.loadUserProfile()];
                        case 1:
                            _a.sent();
                            _a.label = 2;
                        case 2:
                            resolve();
                            return [2 /*return*/];
                    }
                });
            }); })
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
        if (options === void 0) { options = { action: 'register' }; }
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
        if (allRoles === void 0) { allRoles = true; }
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
            roles.push.apply(roles, tslib_1.__spread(realmRoles));
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
        return new Promise(function (resolve, reject) { return tslib_1.__awaiter(_this, void 0, void 0, function () {
            var error_1;
            return tslib_1.__generator(this, function (_a) {
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
        }); });
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
        if (minValidity === void 0) { minValidity = 0; }
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
        if (minValidity === void 0) { minValidity = 5; }
        return new Promise(function (resolve, reject) { return tslib_1.__awaiter(_this, void 0, void 0, function () {
            return tslib_1.__generator(this, function (_a) {
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
        }); });
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
        if (forceReload === void 0) { forceReload = false; }
        return new Promise(function (resolve, reject) { return tslib_1.__awaiter(_this, void 0, void 0, function () {
            var _this = this;
            return tslib_1.__generator(this, function (_a) {
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
        }); });
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
        return new Promise(function (resolve, reject) { return tslib_1.__awaiter(_this, void 0, void 0, function () {
            var error_2;
            return tslib_1.__generator(this, function (_a) {
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
        }); });
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
        return Observable.create(function (observer) { return tslib_1.__awaiter(_this, void 0, void 0, function () {
            var headers, token, error_3;
            return tslib_1.__generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        headers = headersArg;
                        if (!headers) {
                            headers = new HttpHeaders();
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
        }); });
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
         */
        function () {
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
         */
        function () {
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
         */
        function () {
            return this._keycloakEvents$;
        },
        enumerable: true,
        configurable: true
    });
    KeycloakService.decorators = [
        { type: Injectable },
    ];
    /** @nocollapse */
    KeycloakService.ctorParameters = function () { return []; };
    return KeycloakService;
}());
export { KeycloakService };
function KeycloakService_tsickle_Closure_declarations() {
    /**
     * Keycloak-js instance.
     * @type {?}
     */
    KeycloakService.prototype._instance;
    /**
     * User profile as KeycloakProfile interface.
     * @type {?}
     */
    KeycloakService.prototype._userProfile;
    /**
     * Flag to indicate if the bearer will not be added to the authorization header.
     * @type {?}
     */
    KeycloakService.prototype._enableBearerInterceptor;
    /**
     * When the implicit flow is choosen there must exist a silentRefresh, as there is
     * no refresh token.
     * @type {?}
     */
    KeycloakService.prototype._silentRefresh;
    /**
     * Indicates that the user profile should be loaded at the keycloak initialization,
     * just after the login.
     * @type {?}
     */
    KeycloakService.prototype._loadUserProfileAtStartUp;
    /**
     * The bearer prefix that will be appended to the Authorization Header.
     * @type {?}
     */
    KeycloakService.prototype._bearerPrefix;
    /**
     * Value that will be used as the Authorization Http Header name.
     * @type {?}
     */
    KeycloakService.prototype._authorizationHeaderName;
    /**
     * The excluded urls patterns that must skip the KeycloakBearerInterceptor.
     * @type {?}
     */
    KeycloakService.prototype._bearerExcludedUrls;
    /**
     * Observer for the keycloak events
     * @type {?}
     */
    KeycloakService.prototype._keycloakEvents$;
}

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWsuc2VydmljZS5qcyIsInNvdXJjZVJvb3QiOiJuZzovL2tleWNsb2FrLWFuZ3VsYXIvIiwic291cmNlcyI6WyJsaWIvY29yZS9zZXJ2aWNlcy9rZXljbG9hay5zZXJ2aWNlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7OztBQVFBLE9BQU8sRUFBRSxVQUFVLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFFM0MsT0FBTyxFQUFFLFdBQVcsRUFBRSxNQUFNLHNCQUFzQixDQUFDO0FBR25ELE9BQU8sS0FBSyxTQUFTLE1BQU0sYUFBYSxDQUFDOztBQUN6QyxXQUFhLFFBQVEsR0FBRyxTQUFTLENBQUM7QUFFbEMsT0FBTyxFQUFFLFVBQVUsRUFBWSxPQUFPLEVBQUUsTUFBTSxNQUFNLENBQUM7QUFHckQsT0FBTyxFQUFpQixpQkFBaUIsRUFBRSxNQUFNLDhCQUE4QixDQUFDOzs7Ozs7Ozs7SUFrRDlFO1FBQ0UsSUFBSSxDQUFDLGdCQUFnQixHQUFHLElBQUksT0FBTyxFQUFpQixDQUFDO0tBQ3REOzs7Ozs7Ozs7Ozs7O0lBY08sOENBQW9COzs7Ozs7Ozs7Ozs7Y0FBQyxZQUFnQzs7UUFDM0QsSUFBSSxNQUFNLEdBQVcsQ0FBQyxZQUFZLElBQUksUUFBUSxDQUFDLENBQUMsSUFBSSxFQUFFLENBQUM7UUFDdkQsTUFBTSxDQUFDLE1BQU0sQ0FBQyxNQUFNLENBQUMsR0FBRyxDQUFDLENBQUM7Ozs7Ozs7O0lBUXBCLDJDQUFpQjs7Ozs7O2NBQUMsS0FBYzs7UUFDdEMsSUFBSSxXQUFXLEdBQVksS0FBSyxDQUFDO1FBQ2pDLEVBQUUsQ0FBQyxDQUFDLFdBQVcsS0FBSyxTQUFTLElBQUksV0FBVyxLQUFLLElBQUksQ0FBQyxDQUFDLENBQUM7WUFDdEQsV0FBVyxHQUFHLElBQUksQ0FBQztTQUNwQjtRQUNELE1BQU0sQ0FBQyxXQUFXLENBQUM7Ozs7Ozs7Ozs7SUFVYiw2Q0FBbUI7Ozs7Ozs7Ozs7UUFDekIsRUFBRSxDQUFDLENBQUMsQ0FBQyxJ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QUNqRixRQUFRLENBQUMsSUFBSSxDQUFDLE9BQU8sQ0FBQyxDQUFDO3dCQUN2QixRQUFRLENBQUMsUUFBUSxFQUFFLENBQUM7Ozs7d0JBRXBCLFFBQVEsQ0FBQyxLQUFLLENBQUMsT0FBSyxDQUFDLENBQUM7Ozs7O2FBRXpCLENBQUMsQ0FBQztLQUNKO0lBRUQ7Ozs7OztPQU1HOzs7Ozs7OztJQUNILDZDQUFtQjs7Ozs7OztJQUFuQjtRQUNFLE1BQU0sQ0FBQyxJQUFJLENBQUMsU0FBUyxDQUFDO0tBQ3ZCO0lBU0Qsc0JBQUksK0NBQWtCO1FBUHRCOzs7Ozs7V0FNRzs7Ozs7Ozs7UUFDSDtZQUNFLE1BQU0sQ0FBQyxJQUFJLENBQUMsbUJBQW1CLENBQUM7U0FDakM7OztPQUFBO0lBUUQsc0JBQUksb0RBQXVCO1FBTjNCOzs7OztXQUtHOzs7Ozs7O1FBQ0g7WUFDRSxNQUFNLENBQUMsSUFBSSxDQUFDLHdCQUF3QixDQUFDO1NBQ3RDOzs7T0FBQTtJQXFCRCxzQkFBSSw0Q0FBZTtRQW5CbkI7Ozs7Ozs7Ozs7Ozs7Ozs7OztXQWtCRzs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7UUFDSDtZQUNFLE1BQU0sQ0FBQyxJQUFJLENBQUMsZ0JBQWdCLENBQUM7U0FDOUI7OztPQUFBOztnQkFsaUJGLFVBQVU7Ozs7MEJBNUJYOztTQTZCYSxlQUFlIiwic291cmNlc0NvbnRlbnQiOlsiLyoqXG4gKiBAbGljZW5zZVxuICogQ29weXJpZ2h0IE1hdXJpY2lvIEdlbWVsbGkgVmlnb2xvIGFuZCBjb250cmlidXRvcnMuXG4gKlxuICogVXNlIG9mIHRoaXMgc291cmNlIGNvZGUgaXMgZ292ZXJuZWQgYnkgYSBNSVQtc3R5bGUgbGljZW5zZSB0aGF0IGNhbiBiZVxuICogZm91bmQgaW4gdGhlIExJQ0VOU0UgZmlsZSBhdCBodHRwczovL2dpdGh1Yi5jb20vbWF1cmljaW92aWdvbG8va2V5Y2xvYWstYW5ndWxhci9MSUNFTlNFXG4gKi9cblxuaW1wb3J0IHsgSW5qZWN0YWJsZSB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuXG5pbXBvcnQgeyBIdHRwSGVhZGVycyB9IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbi9odHRwJztcblxuLy8gV29ya2Fyb3VuZCBmb3Igcm9sbHVwIGxpYnJhcnkgYmVoYXZpb3VyLCBhcyBwb2ludGVkIG91dCBvbiBpc3N1ZSAjMTI2NyAoaHR0cHM6Ly9naXRodWIuY29tL3JvbGx1cC9yb2xsdXAvaXNzdWVzLzEyNjcpLlxuaW1wb3J0ICogYXMgS2V5Y2xvYWtfIGZyb20gJ2tleWNsb2FrLWpzJztcbmV4cG9ydCBjb25zdCBLZXljbG9hayA9IEtleWNsb2FrXztcblxuaW1wb3J0IHsgT2JzZXJ2YWJsZSwgT2JzZXJ2ZXIsIFN1YmplY3QgfSBmcm9tICdyeGpzJztcblxuaW1wb3J0IHsgS2V5Y2xvYWtPcHRpb25zIH0gZnJvbSAnLi4vaW50ZXJmYWNlcy9rZXljbG9hay1vcHRpb25zJztcbmltcG9ydCB7IEtleWNsb2FrRXZlbnQsIEtleWNsb2FrRXZlbnRUeXBlIH0gZnJvbSAnLi4vaW50ZXJmYWNlcy9rZXljbG9hay1ldmVudCc7XG5cbi8qKlxuICogU2VydmljZSB0byBleHBvc2UgZXhpc3RlbnQgbWV0aG9kcyBmcm9tIHRoZSBLZXljbG9hayBKUyBhZGFwdGVyLCBhZGRpbmcgbmV3XG4gKiBmdW5jdGlvbmFsaXRpZXMgdG8gaW1wcm92ZSB0aGUgdXNlIG9mIGtleWNsb2FrIGluIEFuZ3VsYXIgdiA+IDQuMyBhcHBsaWNhdGlvbnMuXG4gKlxuICogVGhpcyBjbGFzcyBzaG91bGQgYmUgaW5qZWN0ZWQgaW4gdGhlIGFwcGxpY2F0aW9uIGJvb3RzdHJhcCwgc28gdGhlIHNhbWUgaW5zdGFuY2Ugd2lsbCBiZSB1c2VkXG4gKiBhbG9uZyB0aGUgd2ViIGFwcGxpY2F0aW9uLlxuICovXG5ASW5qZWN0YWJsZSgpXG5leHBvcnQgY2xhc3MgS2V5Y2xvYWtTZXJ2aWNlIHtcbiAgLyoqXG4gICAqIEtleWNsb2FrLWpzIGluc3RhbmNlLlxuICAgKi9cbiAgcHJpdmF0ZSBfaW5zdGFuY2U6IEtleWNsb2FrLktleWNsb2FrSW5zdGFuY2U7XG4gIC8qKlxuICAgKiBVc2VyIHByb2ZpbGUgYXMgS2V5Y2xvYWtQcm9maWxlIGludGVyZmFjZS5cbiAgICovXG4gIHByaXZhdGUgX3VzZXJQcm9maWxlOiBLZXljbG9hay5LZXljbG9ha1Byb2ZpbGU7XG4gIC8qKlxuICAgKiBGbGFnIHRvIGluZGljYXRlIGlmIHRoZSBiZWFyZXIgd2lsbCBub3QgYmUgYWRkZWQgdG8gdGhlIGF1dGhvcml6YXRpb24gaGVhZGVyLlxuICAgKi9cbiAgcHJpdmF0ZSBfZW5hYmxlQmVhcmVySW50ZXJjZXB0b3I6IGJvb2xlYW47XG4gIC8qKlxuICAgKiBXaGVuIHRoZSBpbXBsaWNpdCBmbG93IGlzIGNob29zZW4gdGhlcmUgbXVzdCBleGlzdCBhIHNpbGVudFJlZnJlc2gsIGFzIHRoZXJlIGlzXG4gICAqIG5vIHJlZnJlc2ggdG9rZW4uXG4gICAqL1xuICBwcml2YXRlIF9zaWxlbnRSZWZyZXNoOiBib29sZWFuO1xuICAvKipcbiAgICogSW5kaWNhdGVzIHRoYXQgdGhlIHVzZXIgcHJvZmlsZSBzaG91bGQgYmUgbG9hZGVkIGF0IHRoZSBrZXljbG9hayBpbml0aWFsaXphdGlvbixcbiAgICoganVzdCBhZnRlciB0aGUgbG9naW4uXG4gICAqL1xuICBwcml2YXRlIF9sb2FkVXNlclByb2ZpbGVBdFN0YXJ0VXA6IGJvb2xlYW47XG4gIC8qKlxuICAgKiBUaGUgYmVhcmVyIHByZWZpeCB0aGF0IHdpbGwgYmUgYXBwZW5kZWQgdG8gdGhlIEF1dGhvcml6YXRpb24gSGVhZGVyLlxuICAgKi9cbiAgcHJpdmF0ZSBfYmVhcmVyUHJlZml4OiBzdHJpbmc7XG4gIC8qKlxuICAgKiBWYWx1ZSB0aGF0IHdpbGwgYmUgdXNlZCBhcyB0aGUgQXV0aG9yaXphdGlvbiBIdHRwIEhlYWRlciBuYW1lLlxuICAgKi9cbiAgcHJpdmF0ZSBfYXV0aG9yaXphdGlvbkhlYWRlck5hbWU6IHN0cmluZztcbiAgLyoqXG4gICAqIFRoZSBleGNsdWRlZCB1cmxzIHBhdHRlcm5zIHRoYXQgbXVzdCBza2lwIHRoZSBLZXljbG9ha0JlYXJlckludGVyY2VwdG9yLlxuICAgKi9cbiAgcHJpdmF0ZSBfYmVhcmVyRXhjbHVkZWRVcmxzOiBzdHJpbmdbXTtcbiAgLyoqXG4gICAqIE9ic2VydmVyIGZvciB0aGUga2V5Y2xvYWsgZXZlbnRzXG4gICAqL1xuICBwcml2YXRlIF9rZXljbG9ha0V2ZW50cyQ6IFN1YmplY3Q8S2V5Y2xvYWtFdmVudD47XG5cbiAgY29uc3RydWN0b3IoKSB7XG4gICAgdGhpcy5fa2V5Y2xvYWtFdmVudHMkID0gbmV3IFN1YmplY3Q8S2V5Y2xvYWtFdmVudD4oKTtcbiAgfVxuXG4gIC8qKlxuICAgKiBTYW5pdGl6ZXMgdGhlIGJlYXJlciBwcmVmaXgsIHByZXBhcmluZyBpdCB0byBiZSBhcHBlbmRlZCB0b1xuICAgKiB0aGUgdG9rZW4uXG4gICAqXG4gICAqIEBwYXJhbSBiZWFyZXJQcmVmaXhcbiAgICogUHJlZml4IHRvIGJlIGFwcGVuZGVkIHRvIHRoZSBhdXRob3JpemF0aW9uIGhlYWRlciBhc1xuICAgKiBBdXRob3JpemF0aW9uOiA8YmVhcmVyLXByZWZpeD4gPHRva2VuPi5cbiAgICogQHJldHVybnNcbiAgICogVGhlIGJlYXJlciBwcmVmaXggc2FuaXRpemVkLCBtZWFuaW5nIHRoYXQgaXQgd2lsbCBmb2xsb3cgdGhlIGJlYXJlclByZWZpeFxuICAgKiBwYXJhbSBhcyBkZXNjcmliZWQgaW4gdGhlIGxpYnJhcnkgaW5pdGlsaXphdGlvbiBvciB0aGUgZGVmYXVsdCB2YWx1ZSBiZWFyZXIsXG4gICAqIHdpdGggYSBzcGFjZSBhcHBlbmQgaW4gdGhlIGVuZCBmb3IgdGhlIHRva2VuIGNvbmNhdGVuYXRpb24uXG4gICAqL1xuICBwcml2YXRlIHNhbml0aXplQmVhcmVyUHJlZml4KGJlYXJlclByZWZpeDogc3RyaW5nIHwgdW5kZWZpbmVkKTogc3RyaW5nIHtcbiAgICBsZXQgcHJlZml4OiBzdHJpbmcgPSAoYmVhcmVyUHJlZml4IHx8ICdiZWFyZXInKS50cmltKCk7XG4gICAgcmV0dXJuIHByZWZpeC5jb25jYXQoJyAnKTtcbiAgfVxuXG4gIC8qKlxuICAgKiBTZXRzIGRlZmF1bHQgdmFsdWUgdG8gdHJ1ZSBpZiBpdCBpcyB1bmRlZmluZWQgb3IgbnVsbC5cbiAgICpcbiAgICogQHBhcmFtIHZhbHVlIC0gYm9vbGVhbiB2YWx1ZSB0byBiZSBjaGVja2VkXG4gICAqL1xuICBwcml2YXRlIGlmVW5kZWZpbmVkSXNUcnVlKHZhbHVlOiBib29sZWFuKTogYm9vbGVhbiB7XG4gICAgbGV0IHJldHVyblZhbHVlOiBib29sZWFuID0gdmFsdWU7XG4gICAgaWYgKHJldHVyblZhbHVlID09PSB1bmRlZmluZWQgfHwgcmV0dXJuVmFsdWUgPT09IG51bGwpIHtcbiAgICAgIHJldHVyblZhbHVlID0gdHJ1ZTtcbiAgICB9XG4gICAgcmV0dXJuIHJldHVyblZhbHVlO1xuICB9XG5cbiAgLyoqXG4gICAqIEJpbmRzIHRoZSBrZXljbG9hay1qcyBldmVudHMgdG8gdGhlIGtleWNsb2FrRXZlbnRzIFN1YmplY3RcbiAgICogd2hpY2ggaXMgYSBnb29kIHdheSB0byBtb25pdG9yIGZvciBjaGFuZ2VzLCBpZiBuZWVkZWQuXG4gICAqXG4gICAqIFRoZSBrZXljbG9ha0V2ZW50cyByZXR1cm5zIHRoZSBrZXljbG9hay1qcyBldmVudCB0eXBlIGFuZCBhbnlcbiAgICogYXJndW1lbnQgaWYgdGhlIHNvdXJjZSBmdW5jdGlvbiBwcm92aWRlcyBhbnkuXG4gICAqL1xuICBwcml2YXRlIGJpbmRzS2V5Y2xvYWtFdmVudHMoKTogdm9pZCB7XG4gICAgaWYgKCF0aGlzLl9pbnN0YW5jZSkge1xuICAgICAgY29uc29sZS53YXJuKFxuICAgICAgICAnS2V5Y2xvYWsgQW5ndWxhciBldmVudHMgY291bGQgbm90IGJlIHJlZ2lzdGVyZWQgYXMgdGhlIGtleWNsb2FrIGluc3RhbmNlIGlzIHVuZGVmaW5lZC4nXG4gICAgICApO1xuICAgICAgcmV0dXJuO1xuICAgIH1cblxuICAgIHRoaXMuX2luc3RhbmNlLm9uQXV0aEVycm9yID0gZXJyb3JEYXRhID0+IHtcbiAgICAgIHRoaXMuX2tleWNsb2FrRXZlbnRzJC5uZXh0KHsgYXJnczogZXJyb3JEYXRhLCB0eXBlOiBLZXljbG9ha0V2ZW50VHlwZS5PbkF1dGhFcnJvciB9KTtcbiAgICB9O1xuXG4gICAgdGhpcy5faW5zdGFuY2Uub25BdXRoTG9nb3V0ID0gKCkgPT4ge1xuICAgICAgdGhpcy5fa2V5Y2xvYWtFdmVudHMkLm5leHQoeyB0eXBlOiBLZXljbG9ha0V2ZW50VHlwZS5PbkF1dGhMb2dvdXQgfSk7XG4gICAgfTtcblxuICAgIHRoaXMuX2luc3RhbmNlLm9uQXV0aFJlZnJlc2hFcnJvciA9ICgpID0+IHtcbiAgICAgIHRoaXMuX2tleWNsb2FrRXZlbnRzJC5uZXh0KHsgdHlwZTogS2V5Y2xvYWtFdmVudFR5cGUuT25BdXRoUmVmcmVzaEVycm9yIH0pO1xuICAgIH07XG5cbiAgICB0aGlzLl9pbnN0YW5jZS5vbkF1dGhTdWNjZXNzID0gKCkgPT4ge1xuICAgICAgdGhpcy5fa2V5Y2xvYWtFdmVudHMkLm5leHQoeyB0eXBlOiBLZXljbG9ha0V2ZW50VHlwZS5PbkF1dGhTdWNjZXNzIH0pO1xuICAgIH07XG5cbiAgICB0aGlzLl9pbnN0YW5jZS5vblRva2VuRXhwaXJlZCA9ICgpID0+IHtcbiAgICAgIHRoaXMuX2tleWNsb2FrRXZlbnRzJC5uZXh0KHsgdHlwZTogS2V5Y2xvYWtFdmVudFR5cGUuT25Ub2tlbkV4cGlyZWQgfSk7XG4gICAgfTtcblxuICAgIHRoaXMuX2luc3RhbmNlLm9uUmVhZHkgPSBhdXRoZW50aWNhdGVkID0+IHtcbiAgICAgIHRoaXMuX2tleWNsb2FrRXZlbnRzJC5uZXh0KHsgYXJnczogYXV0aGVudGljYXRlZCwgdHlwZTogS2V5Y2xvYWtFdmVudFR5cGUuT25SZWFkeSB9KTtcbiAgICB9O1xuICB9XG5cbiAgLyoqXG4gICAqIEtleWNsb2FrIGluaXRpYWxpemF0aW9uLiBJdCBzaG91bGQgYmUgY2FsbGVkIHRvIGluaXRpYWxpemUgdGhlIGFkYXB0ZXIuXG4gICAqIE9wdGlvbnMgaXMgYSBvYmplY3Qgd2l0aCAyIG1haW4gcGFyYW1ldGVyczogY29uZmlnIGFuZCBpbml0T3B0aW9ucy4gVGhlIGZpcnN0IG9uZVxuICAgKiB3aWxsIGJlIHVzZWQgdG8gY3JlYXRlIHRoZSBLZXljbG9hayBpbnN0YW5jZS4gVGhlIHNlY29uZCBvbmUgYXJlIG9wdGlvbnMgdG8gaW5pdGlhbGl6ZSB0aGVcbiAgICoga2V5Y2xvYWsgaW5zdGFuY2UuXG4gICAqXG4gICAqIEBwYXJhbSBvcHRpb25zXG4gICAqIENvbmZpZzogbWF5IGJlIGEgc3RyaW5nIHJlcHJlc2VudGluZyB0aGUga2V5Y2xvYWsgVVJJIG9yIGFuIG9iamVjdCB3aXRoIHRoZVxuICAgKiBmb2xsb3dpbmcgY29udGVudDpcbiAgICogLSB1cmw6IEtleWNsb2FrIGpzb24gVVJMXG4gICAqIC0gcmVhbG06IHJlYWxtIG5hbWVcbiAgICogLSBjbGllbnRJZDogY2xpZW50IGlkXG4gICAqXG4gICAqIGluaXRPcHRpb25zOlxuICAgKiAtIG9uTG9hZDogU3BlY2lmaWVzIGFuIGFjdGlvbiB0byBkbyBvbiBsb2FkLiBTdXBwb3J0ZWQgdmFsdWVzIGFyZSAnbG9naW4tcmVxdWlyZWQnIG9yXG4gICAqICdjaGVjay1zc28nLlxuICAgKiAtIHRva2VuOiBTZXQgYW4gaW5pdGlhbCB2YWx1ZSBmb3IgdGhlIHRva2VuLlxuICAgKiAtIHJlZnJlc2hUb2tlbjogU2V0IGFuIGluaXRpYWwgdmFsdWUgZm9yIHRoZSByZWZyZXNoIHRva2VuLlxuICAgKiAtIGlkVG9rZW46IFNldCBhbiBpbml0aWFsIHZhbHVlIGZvciB0aGUgaWQgdG9rZW4gKG9ubHkgdG9nZXRoZXIgd2l0aCB0b2tlbiBvciByZWZyZXNoVG9rZW4pLlxuICAgKiAtIHRpbWVTa2V3OiBTZXQgYW4gaW5pdGlhbCB2YWx1ZSBmb3Igc2tldyBiZXR3ZWVuIGxvY2FsIHRpbWUgYW5kIEtleWNsb2FrIHNlcnZlciBpbiBzZWNvbmRzXG4gICAqIChvbmx5IHRvZ2V0aGVyIHdpdGggdG9rZW4gb3IgcmVmcmVzaFRva2VuKS5cbiAgICogLSBjaGVja0xvZ2luSWZyYW1lOiBTZXQgdG8gZW5hYmxlL2Rpc2FibGUgbW9uaXRvcmluZyBsb2dpbiBzdGF0ZSAoZGVmYXVsdCBpcyB0cnVlKS5cbiAgICogLSBjaGVja0xvZ2luSWZyYW1lSW50ZXJ2YWw6IFNldCB0aGUgaW50ZXJ2YWwgdG8gY2hlY2sgbG9naW4gc3RhdGUgKGRlZmF1bHQgaXMgNSBzZWNvbmRzKS5cbiAgICogLSByZXNwb25zZU1vZGU6IFNldCB0aGUgT3BlbklEIENvbm5lY3QgcmVzcG9uc2UgbW9kZSBzZW5kIHRvIEtleWNsb2FrIHNlcnZlciBhdCBsb2dpblxuICAgKiByZXF1ZXN0LiBWYWxpZCB2YWx1ZXMgYXJlIHF1ZXJ5IG9yIGZyYWdtZW50IC4gRGVmYXVsdCB2YWx1ZSBpcyBmcmFnbWVudCwgd2hpY2ggbWVhbnNcbiAgICogdGhhdCBhZnRlciBzdWNjZXNzZnVsIGF1dGhlbnRpY2F0aW9uIHdpbGwgS2V5Y2xvYWsgcmVkaXJlY3QgdG8gamF2YXNjcmlwdCBhcHBsaWNhdGlvblxuICAgKiB3aXRoIE9wZW5JRCBDb25uZWN0IHBhcmFtZXRlcnMgYWRkZWQgaW4gVVJMIGZyYWdtZW50LiBUaGlzIGlzIGdlbmVyYWxseSBzYWZlciBhbmRcbiAgICogcmVjb21tZW5kZWQgb3ZlciBxdWVyeS5cbiAgICogLSBmbG93OiBTZXQgdGhlIE9wZW5JRCBDb25uZWN0IGZsb3cuIFZhbGlkIHZhbHVlcyBhcmUgc3RhbmRhcmQsIGltcGxpY2l0IG9yIGh5YnJpZC5cbiAgICpcbiAgICogZW5hYmxlQmVhcmVySW50ZXJjZXB0b3I6XG4gICAqIEZsYWcgdG8gaW5kaWNhdGUgaWYgdGhlIGJlYXJlciB3aWxsIGFkZGVkIHRvIHRoZSBhdXRob3JpemF0aW9uIGhlYWRlci5cbiAgICpcbiAgICogbG9hZFVzZXJQcm9maWxlSW5TdGFydFVwOlxuICAgKiBJbmRpY2F0ZXMgdGhhdCB0aGUgdXNlciBwcm9maWxlIHNob3VsZCBiZSBsb2FkZWQgYXQgdGhlIGtleWNsb2FrIGluaXRpYWxpemF0aW9uLFxuICAgKiBqdXN0IGFmdGVyIHRoZSBsb2dpbi5cbiAgICpcbiAgICogYmVhcmVyRXhjbHVkZWRVcmxzOlxuICAgKiBTdHJpbmcgQXJyYXkgdG8gZXhjbHVkZSB0aGUgdXJscyB0aGF0IHNob3VsZCBub3QgaGF2ZSB0aGUgQXV0aG9yaXphdGlvbiBIZWFkZXIgYXV0b21hdGljYWxseVxuICAgKiBhZGRlZC5cbiAgICpcbiAgICogYXV0aG9yaXphdGlvbkhlYWRlck5hbWU6XG4gICAqIFRoaXMgdmFsdWUgd2lsbCBiZSB1c2VkIGFzIHRoZSBBdXRob3JpemF0aW9uIEh0dHAgSGVhZGVyIG5hbWUuXG4gICAqXG4gICAqIGJlYXJlclByZWZpeDpcbiAgICogVGhpcyB2YWx1ZSB3aWxsIGJlIGluY2x1ZGVkIGluIHRoZSBBdXRob3JpemF0aW9uIEh0dHAgSGVhZGVyIHBhcmFtLlxuICAgKlxuICAgKiBAcmV0dXJuc1xuICAgKiBBIFByb21pc2Ugd2l0aCBhIGJvb2xlYW4gaW5kaWNhdGluZyBpZiB0aGUgaW5pdGlhbGl6YXRpb24gd2FzIHN1Y2Nlc3NmdWwuXG4gICAqL1xuICBpbml0KG9wdGlvbnM6IEtleWNsb2FrT3B0aW9ucyA9IHt9KTogUHJvbWlzZTxib29sZWFuPiB7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKChyZXNvbHZlLCByZWplY3QpID0+IHtcbiAgICAgIHRoaXMuX2VuYWJsZUJlYXJlckludGVyY2VwdG9yID0gdGhpcy5pZlVuZGVmaW5lZElzVHJ1ZShvcHRpb25zLmVuYWJsZUJlYXJlckludGVyY2VwdG9yKTtcbiAgICAgIHRoaXMuX2xvYWRVc2VyUHJvZmlsZUF0U3RhcnRVcCA9IHRoaXMuaWZVbmRlZmluZWRJc1RydWUob3B0aW9ucy5sb2FkVXNlclByb2ZpbGVBdFN0YXJ0VXApO1xuICAgICAgdGhpcy5fYmVhcmVyRXhjbHVkZWRVcmxzID0gb3B0aW9ucy5iZWFyZXJFeGNsdWRlZFVybHMgfHwgW107XG4gICAgICB0aGlzLl9hdXRob3JpemF0aW9uSGVhZGVyTmFtZSA9IG9wdGlvbnMuYXV0aG9yaXphdGlvbkhlYWRlck5hbWUgfHwgJ0F1dGhvcml6YXRpb24nO1xuICAgICAgdGhpcy5fYmVhcmVyUHJlZml4ID0gdGhpcy5zYW5pdGl6ZUJlYXJlclByZWZpeChvcHRpb25zLmJlYXJlclByZWZpeCk7XG4gICAgICB0aGlzLl9zaWxlbnRSZWZyZXNoID0gb3B0aW9ucy5pbml0T3B0aW9ucyA/IG9wdGlvbnMuaW5pdE9wdGlvbnMuZmxvdyA9PT0gJ2ltcGxpY2l0JyA6IGZhbHNlO1xuICAgICAgdGhpcy5faW5zdGFuY2UgPSBLZXljbG9hayhvcHRpb25zLmNvbmZpZyk7XG4gICAgICB0aGlzLmJpbmRzS2V5Y2xvYWtFdmVudHMoKTtcbiAgICAgIHRoaXMuX2luc3RhbmNlXG4gICAgICAgIC5pbml0KG9wdGlvbnMuaW5pdE9wdGlvbnMhKVxuICAgICAgICAuc3VjY2Vzcyhhc3luYyBhdXRoZW50aWNhdGVkID0+IHtcbiAgICAgICAgICBpZiAoYXV0aGVudGljYXRlZCAmJiB0aGlzLl9sb2FkVXNlclByb2ZpbGVBdFN0YXJ0VXApIHtcbiAgICAgICAgICAgIGF3YWl0IHRoaXMubG9hZFVzZXJQcm9maWxlKCk7XG4gICAgICAgICAgfVxuICAgICAgICAgIHJlc29sdmUoYXV0aGVudGljYXRlZCk7XG4gICAgICAgIH0pXG4gICAgICAgIC5lcnJvcihlcnJvciA9PiB7XG4gICAgICAgICAgcmVqZWN0KCdBbiBlcnJvciBoYXBwZW5lZCBkdXJpbmcgS2V5Y2xvYWsgaW5pdGlhbGl6YXRpb24uJyk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIFJlZGlyZWN0cyB0byBsb2dpbiBmb3JtIG9uIChvcHRpb25zIGlzIGFuIG9wdGlvbmFsIG9iamVjdCB3aXRoIHJlZGlyZWN0VXJpIGFuZC9vclxuICAgKiBwcm9tcHQgZmllbGRzKS5cbiAgICpcbiAgICogQHBhcmFtIG9wdGlvbnNcbiAgICogT2JqZWN0LCB3aGVyZTpcbiAgICogIC0gcmVkaXJlY3RVcmk6IFNwZWNpZmllcyB0aGUgdXJpIHRvIHJlZGlyZWN0IHRvIGFmdGVyIGxvZ2luLlxuICAgKiAgLSBwcm9tcHQ6QnkgZGVmYXVsdCB0aGUgbG9naW4gc2NyZWVuIGlzIGRpc3BsYXllZCBpZiB0aGUgdXNlciBpcyBub3QgbG9nZ2VkLWluIHRvIEtleWNsb2FrLlxuICAgKiBUbyBvbmx5IGF1dGhlbnRpY2F0ZSB0byB0aGUgYXBwbGljYXRpb24gaWYgdGhlIHVzZXIgaXMgYWxyZWFkeSBsb2dnZWQtaW4gYW5kIG5vdCBkaXNwbGF5IHRoZVxuICAgKiBsb2dpbiBwYWdlIGlmIHRoZSB1c2VyIGlzIG5vdCBsb2dnZWQtaW4sIHNldCB0aGlzIG9wdGlvbiB0byBub25lLiBUbyBhbHdheXMgcmVxdWlyZVxuICAgKiByZS1hdXRoZW50aWNhdGlvbiBhbmQgaWdub3JlIFNTTywgc2V0IHRoaXMgb3B0aW9uIHRvIGxvZ2luIC5cbiAgICogIC0gbWF4QWdlOiBVc2VkIGp1c3QgaWYgdXNlciBpcyBhbHJlYWR5IGF1dGhlbnRpY2F0ZWQuIFNwZWNpZmllcyBtYXhpbXVtIHRpbWUgc2luY2UgdGhlXG4gICAqIGF1dGhlbnRpY2F0aW9uIG9mIHVzZXIgaGFwcGVuZWQuIElmIHVzZXIgaXMgYWxyZWFkeSBhdXRoZW50aWNhdGVkIGZvciBsb25nZXIgdGltZSB0aGFuXG4gICAqIG1heEFnZSwgdGhlIFNTTyBpcyBpZ25vcmVkIGFuZCBoZSB3aWxsIG5lZWQgdG8gcmUtYXV0aGVudGljYXRlIGFnYWluLlxuICAgKiAgLSBsb2dpbkhpbnQ6IFVzZWQgdG8gcHJlLWZpbGwgdGhlIHVzZXJuYW1lL2VtYWlsIGZpZWxkIG9uIHRoZSBsb2dpbiBmb3JtLlxuICAgKiAgLSBhY3Rpb246IElmIHZhbHVlIGlzICdyZWdpc3RlcicgdGhlbiB1c2VyIGlzIHJlZGlyZWN0ZWQgdG8gcmVnaXN0cmF0aW9uIHBhZ2UsIG90aGVyd2lzZSB0b1xuICAgKiBsb2dpbiBwYWdlLlxuICAgKiAgLSBsb2NhbGU6IFNwZWNpZmllcyB0aGUgZGVzaXJlZCBsb2NhbGUgZm9yIHRoZSBVSS5cbiAgICogQHJldHVybnNcbiAgICogQSB2b2lkIFByb21pc2UgaWYgdGhlIGxvZ2luIGlzIHN1Y2Nlc3NmdWwgYW5kIGFmdGVyIHRoZSB1c2VyIHByb2ZpbGUgbG9hZGluZy5cbiAgICovXG4gIGxvZ2luKG9wdGlvbnM6IEtleWNsb2FrLktleWNsb2FrTG9naW5PcHRpb25zID0ge30pOiBQcm9taXNlPHZvaWQ+IHtcbiAgICByZXR1cm4gbmV3IFByb21pc2UoKHJlc29sdmUsIHJlamVjdCkgPT4ge1xuICAgICAgdGhpcy5faW5zdGFuY2VcbiAgICAgICAgLmxvZ2luKG9wdGlvbnMpXG4gICAgICAgIC5zdWNjZXNzKGFzeW5jICgpID0+IHtcbiAgICAgICAgICBpZiAodGhpcy5fbG9hZFVzZXJQcm9maWxlQXRTdGFydFVwKSB7XG4gICAgICAgICAgICBhd2FpdCB0aGlzLmxvYWRVc2VyUHJvZmlsZSgpO1xuICAgICAgICAgIH1cbiAgICAgICAgICByZXNvbHZlKCk7XG4gICAgICAgIH0pXG4gICAgICAgIC5lcnJvcihlcnJvciA9PiB7XG4gICAgICAgICAgcmVqZWN0KCdBbiBlcnJvciBoYXBwZW5lZCBkdXJpbmcgdGhlIGxvZ2luLicpO1xuICAgICAgICB9KTtcbiAgICB9KTtcbiAgfVxuXG4gIC8qKlxuICAgKiBSZWRpcmVjdHMgdG8gbG9nb3V0LlxuICAgKlxuICAgKiBAcGFyYW0gcmVkaXJlY3RVcmlcbiAgICogU3BlY2lmaWVzIHRoZSB1cmkgdG8gcmVkaXJlY3QgdG8gYWZ0ZXIgbG9nb3V0LlxuICAgKiBAcmV0dXJuc1xuICAgKiBBIHZvaWQgUHJvbWlzZSBpZiB0aGUgbG9nb3V0IHdhcyBzdWNjZXNzZnVsLCBjbGVhbmluZyBhbHNvIHRoZSB1c2VyUHJvZmlsZS5cbiAgICovXG4gIGxvZ291dChyZWRpcmVjdFVyaT86IHN0cmluZyk6IFByb21pc2U8dm9pZD4ge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZSgocmVzb2x2ZSwgcmVqZWN0KSA9PiB7XG4gICAgICBjb25zdCBvcHRpb25zOiBhbnkgPSB7XG4gICAgICAgIHJlZGlyZWN0VXJpXG4gICAgICB9O1xuXG4gICAgICB0aGlzLl9pbnN0YW5jZVxuICAgICAgICAubG9nb3V0KG9wdGlvbnMpXG4gICAgICAgIC5zdWNjZXNzKCgpID0+IHtcbiAgICAgICAgICB0aGlzLl91c2VyUHJvZmlsZSA9IHVuZGVmaW5lZCE7XG4gICAgICAgICAgcmVzb2x2ZSgpO1xuICAgICAgICB9KVxuICAgICAgICAuZXJyb3IoZXJyb3IgPT4ge1xuICAgICAgICAgIHJlamVjdCgnQW4gZXJyb3IgaGFwcGVuZWQgZHVyaW5nIGxvZ291dC4nKTtcbiAgICAgICAgfSk7XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogUmVkaXJlY3RzIHRvIHJlZ2lzdHJhdGlvbiBmb3JtLiBTaG9ydGN1dCBmb3IgbG9naW4gd2l0aCBvcHRpb25cbiAgICogYWN0aW9uID0gJ3JlZ2lzdGVyJy4gT3B0aW9ucyBhcmUgc2FtZSBhcyBmb3IgdGhlIGxvZ2luIG1ldGhvZCBidXQgJ2FjdGlvbicgaXMgc2V0IHRvXG4gICAqICdyZWdpc3RlcicuXG4gICAqXG4gICAqIEBwYXJhbSBvcHRpb25zXG4gICAqIGxvZ2luIG9wdGlvbnNcbiAgICogQHJldHVybnNcbiAgICogQSB2b2lkIFByb21pc2UgaWYgdGhlIHJlZ2lzdGVyIGZsb3cgd2FzIHN1Y2Nlc3NmdWwuXG4gICAqL1xuICByZWdpc3RlcihvcHRpb25zOiBLZXljbG9hay5LZXljbG9ha0xvZ2luT3B0aW9ucyA9IHsgYWN0aW9uOiAncmVnaXN0ZXInIH0pOiBQcm9taXNlPHZvaWQ+IHtcbiAgICByZXR1cm4gbmV3IFByb21pc2UoKHJlc29sdmUsIHJlamVjdCkgPT4ge1xuICAgICAgdGhpcy5faW5zdGFuY2VcbiAgICAgICAgLnJlZ2lzdGVyKG9wdGlvbnMpXG4gICAgICAgIC5zdWNjZXNzKCgpID0+IHtcbiAgICAgICAgICByZXNvbHZlKCk7XG4gICAgICAgIH0pXG4gICAgICAgIC5lcnJvcigoKSA9PiB7XG4gICAgICAgICAgcmVqZWN0KCdBbiBlcnJvciBoYXBwZW5lZCBkdXJpbmcgdGhlIHJlZ2lzdGVyIGV4ZWN1dGlvbicpO1xuICAgICAgICB9KTtcbiAgICB9KTtcbiAgfVxuXG4gIC8qKlxuICAgKiBDaGVjayBpZiB0aGUgdXNlciBoYXMgYWNjZXNzIHRvIHRoZSBzcGVjaWZpZWQgcm9sZS4gSXQgd2lsbCBsb29rIGZvciByb2xlcyBpblxuICAgKiByZWFsbSBhbmQgY2xpZW50SWQsIGJ1dCB3aWxsIG5vdCBjaGVjayBpZiB0aGUgdXNlciBpcyBsb2dnZWQgaW4gZm9yIGJldHRlciBwZXJmb3JtYW5jZS5cbiAgICpcbiAgICogQHBhcmFtIHJvbGVcbiAgICogcm9sZSBuYW1lXG4gICAqIEByZXR1cm5zXG4gICAqIEEgYm9vbGVhbiBtZWFuaW5nIGlmIHRoZSB1c2VyIGhhcyB0aGUgc3BlY2lmaWVkIFJvbGUuXG4gICAqL1xuICBpc1VzZXJJblJvbGUocm9sZTogc3RyaW5nKTogYm9vbGVhbiB7XG4gICAgbGV0IGhhc1JvbGU6IGJvb2xlYW47XG4gICAgaGFzUm9sZSA9IHRoaXMuX2luc3RhbmNlLmhhc1Jlc291cmNlUm9sZShyb2xlKTtcbiAgICBpZiAoIWhhc1JvbGUpIHtcbiAgICAgIGhhc1JvbGUgPSB0aGlzLl9pbnN0YW5jZS5oYXNSZWFsbVJvbGUocm9sZSk7XG4gICAgfVxuICAgIHJldHVybiBoYXNSb2xlO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybiB0aGUgcm9sZXMgb2YgdGhlIGxvZ2dlZCB1c2VyLiBUaGUgYWxsUm9sZXMgcGFyYW1ldGVyLCB3aXRoIGRlZmF1bHQgdmFsdWVcbiAgICogdHJ1ZSwgd2lsbCByZXR1cm4gdGhlIGNsaWVudElkIGFuZCByZWFsbSByb2xlcyBhc3NvY2lhdGVkIHdpdGggdGhlIGxvZ2dlZCB1c2VyLiBJZiBzZXQgdG8gZmFsc2VcbiAgICogaXQgd2lsbCBvbmx5IHJldHVybiB0aGUgdXNlciByb2xlcyBhc3NvY2lhdGVkIHdpdGggdGhlIGNsaWVudElkLlxuICAgKlxuICAgKiBAcGFyYW0gYWxsUm9sZXNcbiAgICogRmxhZyB0byBzZXQgaWYgYWxsIHJvbGVzIHNob3VsZCBiZSByZXR1cm5lZC4oT3B0aW9uYWw6IGRlZmF1bHQgdmFsdWUgaXMgdHJ1ZSlcbiAgICogQHJldHVybnNcbiAgICogQXJyYXkgb2YgUm9sZXMgYXNzb2NpYXRlZCB3aXRoIHRoZSBsb2dnZWQgdXNlci5cbiAgICovXG4gIGdldFVzZXJSb2xlcyhhbGxSb2xlczogYm9vbGVhbiA9IHRydWUpOiBzdHJpbmdbXSB7XG4gICAgbGV0IHJvbGVzOiBzdHJpbmdbXSA9IFtdO1xuICAgIGlmICh0aGlzLl9pbnN0YW5jZS5yZXNvdXJjZUFjY2Vzcykge1xuICAgICAgZm9yIChjb25zdCBrZXkgaW4gdGhpcy5faW5zdGFuY2UucmVzb3VyY2VBY2Nlc3MpIHtcbiAgICAgICAgaWYgKHRoaXMuX2luc3RhbmNlLnJlc291cmNlQWNjZXNzLmhhc093blByb3BlcnR5KGtleSkpIHtcbiAgICAgICAgICBjb25zdCByZXNvdXJjZUFjY2VzczogYW55ID0gdGhpcy5faW5zdGFuY2UucmVzb3VyY2VBY2Nlc3Nba2V5XTtcbiAgICAgICAgICBjb25zdCBjbGllbnRSb2xlcyA9IHJlc291cmNlQWNjZXNzWydyb2xlcyddIHx8IFtdO1xuICAgICAgICAgIHJvbGVzID0gcm9sZXMuY29uY2F0KGNsaWVudFJvbGVzKTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cbiAgICBpZiAoYWxsUm9sZXMgJiYgdGhpcy5faW5zdGFuY2UucmVhbG1BY2Nlc3MpIHtcbiAgICAgIGxldCByZWFsbVJvbGVzID0gdGhpcy5faW5zdGFuY2UucmVhbG1BY2Nlc3NbJ3JvbGVzJ10gfHwgW107XG4gICAgICByb2xlcy5wdXNoKC4uLnJlYWxtUm9sZXMpO1xuICAgIH1cbiAgICByZXR1cm4gcm9sZXM7XG4gIH1cblxuICAvKipcbiAgICogQ2hlY2sgaWYgdXNlciBpcyBsb2dnZWQgaW4uXG4gICAqXG4gICAqIEByZXR1cm5zXG4gICAqIEEgYm9vbGVhbiB0aGF0IGluZGljYXRlcyBpZiB0aGUgdXNlciBpcyBsb2dnZWQgaW4uXG4gICAqL1xuICBpc0xvZ2dlZEluKCk6IFByb21pc2U8Ym9vbGVhbj4ge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZShhc3luYyAocmVzb2x2ZSwgcmVqZWN0KSA9PiB7XG4gICAgICB0cnkge1xuICAgICAgICBhd2FpdCB0aGlzLnVwZGF0ZVRva2VuKDIwKTtcbiAgICAgICAgcmVzb2x2ZSh0cnVlKTtcbiAgICAgIH0gY2F0Y2ggKGVycm9yKSB7XG4gICAgICAgIHJlc29sdmUoZmFsc2UpO1xuICAgICAgfVxuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybnMgdHJ1ZSBpZiB0aGUgdG9rZW4gaGFzIGxlc3MgdGhhbiBtaW5WYWxpZGl0eSBzZWNvbmRzIGxlZnQgYmVmb3JlXG4gICAqIGl0IGV4cGlyZXMuXG4gICAqXG4gICAqIEBwYXJhbSBtaW5WYWxpZGl0eVxuICAgKiBTZWNvbmRzIGxlZnQuIChtaW5WYWxpZGl0eSkgaXMgb3B0aW9uYWwuIERlZmF1bHQgdmFsdWUgaXMgMC5cbiAgICogQHJldHVybnNcbiAgICogQm9vbGVhbiBpbmRpY2F0aW5nIGlmIHRoZSB0b2tlbiBpcyBleHBpcmVkLlxuICAgKi9cbiAgaXNUb2tlbkV4cGlyZWQobWluVmFsaWRpdHk6IG51bWJlciA9IDApOiBib29sZWFuIHtcbiAgICByZXR1cm4gdGhpcy5faW5zdGFuY2UuaXNUb2tlbkV4cGlyZWQobWluVmFsaWRpdHkpO1xuICB9XG5cbiAgLyoqXG4gICAqIElmIHRoZSB0b2tlbiBleHBpcmVzIHdpdGhpbiBtaW5WYWxpZGl0eSBzZWNvbmRzIHRoZSB0b2tlbiBpcyByZWZyZXNoZWQuIElmIHRoZVxuICAgKiBzZXNzaW9uIHN0YXR1cyBpZnJhbWUgaXMgZW5hYmxlZCwgdGhlIHNlc3Npb24gc3RhdHVzIGlzIGFsc28gY2hlY2tlZC5cbiAgICogUmV0dXJucyBhIHByb21pc2UgdGVsbGluZyBpZiB0aGUgdG9rZW4gd2FzIHJlZnJlc2hlZCBvciBub3QuIElmIHRoZSBzZXNzaW9uIGlzIG5vdCBhY3RpdmVcbiAgICogYW55bW9yZSwgdGhlIHByb21pc2UgaXMgcmVqZWN0ZWQuXG4gICAqXG4gICAqIEBwYXJhbSBtaW5WYWxpZGl0eVxuICAgKiBTZWNvbmRzIGxlZnQuIChtaW5WYWxpZGl0eSBpcyBvcHRpb25hbCwgaWYgbm90IHNwZWNpZmllZCA1IGlzIHVzZWQpXG4gICAqIEByZXR1cm5zXG4gICAqIFByb21pc2Ugd2l0aCBhIGJvb2xlYW4gaW5kaWNhdGluZyBpZiB0aGUgdG9rZW4gd2FzIHN1Y2Nlc2Z1bGx5IHVwZGF0ZWQuXG4gICAqL1xuICB1cGRhdGVUb2tlbihtaW5WYWxpZGl0eTogbnVtYmVyID0gNSk6IFByb21pc2U8Ym9vbGVhbj4ge1xuICAgIHJldHVybiBuZXcgUHJvbWlzZShhc3luYyAocmVzb2x2ZSwgcmVqZWN0KSA9PiB7XG4gICAgICAvLyBUT0RPOiB0aGlzIGlzIGEgd29ya2Fyb3VuZCB1bnRpbCB0aGUgc2lsZW50IHJlZnJlc2ggKGlzc3VlICM0MylcbiAgICAgIC8vIGlzIG5vdCBpbXBsZW1lbnRlZCwgYXZvaWRpbmcgdGhlIHJlZGlyZWN0IGxvb3AuXG4gICAgICBpZiAodGhpcy5fc2lsZW50UmVmcmVzaCkge1xuICAgICAgICBpZiAodGhpcy5pc1Rva2VuRXhwaXJlZCgpKSB7XG4gICAgICAgICAgcmVqZWN0KCdGYWlsZWQgdG8gcmVmcmVzaCB0aGUgdG9rZW4sIG9yIHRoZSBzZXNzaW9uIGlzIGV4cGlyZWQnKTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICByZXNvbHZlKHRydWUpO1xuICAgICAgICB9XG4gICAgICAgIHJldHVybjtcbiAgICAgIH1cblxuICAgICAgaWYgKCF0aGlzLl9pbnN0YW5jZSkge1xuICAgICAgICByZWplY3QoKTtcbiAgICAgICAgcmV0dXJuO1xuICAgICAgfVxuXG4gICAgICB0aGlzLl9pbnN0YW5jZVxuICAgICAgICAudXBkYXRlVG9rZW4obWluVmFsaWRpdHkpXG4gICAgICAgIC5zdWNjZXNzKHJlZnJlc2hlZCA9PiB7XG4gICAgICAgICAgcmVzb2x2ZShyZWZyZXNoZWQpO1xuICAgICAgICB9KVxuICAgICAgICAuZXJyb3IoZXJyb3IgPT4ge1xuICAgICAgICAgIHJlamVjdCgnRmFpbGVkIHRvIHJlZnJlc2ggdGhlIHRva2VuLCBvciB0aGUgc2Vzc2lvbiBpcyBleHBpcmVkJyk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIExvYWRzIHRoZSB1c2VycyBwcm9maWxlLlxuICAgKiBSZXR1cm5zIHByb21pc2UgdG8gc2V0IGZ1bmN0aW9ucyB0byBiZSBpbnZva2VkIGlmIHRoZSBwcm9maWxlIHdhcyBsb2FkZWQgc3VjY2Vzc2Z1bGx5LCBvciBpZlxuICAgKiB0aGUgcHJvZmlsZSBjb3VsZCBub3QgYmUgbG9hZGVkLlxuICAgKlxuICAgKiBAcGFyYW0gZm9yY2VSZWxvYWRcbiAgICogSWYgdHJ1ZSB3aWxsIGZvcmNlIHRoZSBsb2FkVXNlclByb2ZpbGUgZXZlbiBpZiBpdHMgYWxyZWFkeSBsb2FkZWQuXG4gICAqIEByZXR1cm5zXG4gICAqIEEgcHJvbWlzZSB3aXRoIHRoZSBLZXljbG9ha1Byb2ZpbGUgZGF0YSBsb2FkZWQuXG4gICAqL1xuICBsb2FkVXNlclByb2ZpbGUoZm9yY2VSZWxvYWQ6IGJvb2xlYW4gPSBmYWxzZSk6IFByb21pc2U8S2V5Y2xvYWsuS2V5Y2xvYWtQcm9maWxlPiB7XG4gICAgcmV0dXJuIG5ldyBQcm9taXNlKGFzeW5jIChyZXNvbHZlLCByZWplY3QpID0+IHtcbiAgICAgIGlmICh0aGlzLl91c2VyUHJvZmlsZSAmJiAhZm9yY2VSZWxvYWQpIHtcbiAgICAgICAgcmVzb2x2ZSh0aGlzLl91c2VyUHJvZmlsZSk7XG4gICAgICAgIHJldHVybjtcbiAgICAgIH1cblxuICAgICAgaWYgKCEoYXdhaXQgdGhpcy5pc0xvZ2dlZEluKCkpKSB7XG4gICAgICAgIHJlamVjdCgnVGhlIHVzZXIgcHJvZmlsZSB3YXMgbm90IGxvYWRlZCBhcyB0aGUgdXNlciBpcyBub3QgbG9nZ2VkIGluLicpO1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG5cbiAgICAgIHRoaXMuX2luc3RhbmNlXG4gICAgICAgIC5sb2FkVXNlclByb2ZpbGUoKVxuICAgICAgICAuc3VjY2VzcyhyZXN1bHQgPT4ge1xuICAgICAgICAgIHRoaXMuX3VzZXJQcm9maWxlID0gcmVzdWx0IGFzIEtleWNsb2FrLktleWNsb2FrUHJvZmlsZTtcbiAgICAgICAgICByZXNvbHZlKHRoaXMuX3VzZXJQcm9maWxlKTtcbiAgICAgICAgfSlcbiAgICAgICAgLmVycm9yKGVyciA9PiB7XG4gICAgICAgICAgcmVqZWN0KCdUaGUgdXNlciBwcm9maWxlIGNvdWxkIG5vdCBiZSBsb2FkZWQuJyk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybnMgdGhlIGF1dGhlbnRpY2F0ZWQgdG9rZW4sIGNhbGxpbmcgdXBkYXRlVG9rZW4gdG8gZ2V0IGEgcmVmcmVzaGVkIG9uZSBpZlxuICAgKiBuZWNlc3NhcnkuIElmIHRoZSBzZXNzaW9uIGlzIGV4cGlyZWQgdGhpcyBtZXRob2QgY2FsbHMgdGhlIGxvZ2luIG1ldGhvZCBmb3IgYSBuZXcgbG9naW4uXG4gICAqXG4gICAqIEByZXR1cm5zXG4gICAqIFByb21pc2Ugd2l0aCB0aGUgZ2VuZXJhdGVkIHRva2VuLlxuICAgKi9cbiAgZ2V0VG9rZW4oKTogUHJvbWlzZTxzdHJpbmc+IHtcbiAgICByZXR1cm4gbmV3IFByb21pc2UoYXN5bmMgKHJlc29sdmUsIHJlamVjdCkgPT4ge1xuICAgICAgdHJ5IHtcbiAgICAgICAgYXdhaXQgdGhpcy51cGRhdGVUb2tlbigxMCk7XG4gICAgICAgIHJlc29sdmUodGhpcy5faW5zdGFuY2UudG9rZW4pO1xuICAgICAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICAgICAgdGhpcy5sb2dpbigpO1xuICAgICAgfVxuICAgIH0pO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybnMgdGhlIGxvZ2dlZCB1c2VybmFtZS5cbiAgICpcbiAgICogQHJldHVybnNcbiAgICogVGhlIGxvZ2dlZCB1c2VybmFtZS5cbiAgICovXG4gIGdldFVzZXJuYW1lKCk6IHN0cmluZyB7XG4gICAgaWYgKCF0aGlzLl91c2VyUHJvZmlsZSkge1xuICAgICAgdGhyb3cgbmV3IEVycm9yKCdVc2VyIG5vdCBsb2dnZWQgaW4gb3IgdXNlciBwcm9maWxlIHdhcyBub3QgbG9hZGVkLicpO1xuICAgIH1cblxuICAgIHJldHVybiB0aGlzLl91c2VyUHJvZmlsZS51c2VybmFtZSE7XG4gIH1cblxuICAvKipcbiAgICogQ2xlYXIgYXV0aGVudGljYXRpb24gc3RhdGUsIGluY2x1ZGluZyB0b2tlbnMuIFRoaXMgY2FuIGJlIHVzZWZ1bCBpZiBhcHBsaWNhdGlvblxuICAgKiBoYXMgZGV0ZWN0ZWQgdGhlIHNlc3Npb24gd2FzIGV4cGlyZWQsIGZvciBleGFtcGxlIGlmIHVwZGF0aW5nIHRva2VuIGZhaWxzLlxuICAgKiBJbnZva2luZyB0aGlzIHJlc3VsdHMgaW4gb25BdXRoTG9nb3V0IGNhbGxiYWNrIGxpc3RlbmVyIGJlaW5nIGludm9rZWQuXG4gICAqL1xuICBjbGVhclRva2VuKCk6IHZvaWQge1xuICAgIHRoaXMuX2luc3RhbmNlLmNsZWFyVG9rZW4oKTtcbiAgfVxuXG4gIC8qKlxuICAgKiBBZGRzIGEgdmFsaWQgdG9rZW4gaW4gaGVhZGVyLiBUaGUga2V5ICYgdmFsdWUgZm9ybWF0IGlzOlxuICAgKiBBdXRob3JpemF0aW9uIEJlYXJlciA8dG9rZW4+LlxuICAgKiBJZiB0aGUgaGVhZGVycyBwYXJhbSBpcyB1bmRlZmluZWQgaXQgd2lsbCBjcmVhdGUgdGhlIEFuZ3VsYXIgaGVhZGVycyBvYmplY3QuXG4gICAqXG4gICAqIEBwYXJhbSBoZWFkZXJzXG4gICAqIFVwZGF0ZWQgaGVhZGVyIHdpdGggQXV0aG9yaXphdGlvbiBhbmQgS2V5Y2xvYWsgdG9rZW4uXG4gICAqIEByZXR1cm5zXG4gICAqIEFuIG9ic2VydmFibGUgd2l0aCB3aXRoIHRoZSBIVFRQIEF1dGhvcml6YXRpb24gaGVhZGVyIGFuZCB0aGUgY3VycmVudCB0b2tlbi5cbiAgICovXG4gIGFkZFRva2VuVG9IZWFkZXIoaGVhZGVyc0FyZz86IEh0dHBIZWFkZXJzKTogT2JzZXJ2YWJsZTxIdHRwSGVhZGVycz4ge1xuICAgIHJldHVybiBPYnNlcnZhYmxlLmNyZWF0ZShhc3luYyAob2JzZXJ2ZXI6IE9ic2VydmVyPGFueT4pID0+IHtcbiAgICAgIGxldCBoZWFkZXJzID0gaGVhZGVyc0FyZztcbiAgICAgIGlmICghaGVhZGVycykge1xuICAgICAgICBoZWFkZXJzID0gbmV3IEh0dHBIZWFkZXJzKCk7XG4gICAgICB9XG4gICAgICB0cnkge1xuICAgICAgICBjb25zdCB0b2tlbjogc3RyaW5nID0gYXdhaXQgdGhpcy5nZXRUb2tlbigpO1xuICAgICAgICBoZWFkZXJzID0gaGVhZGVycy5zZXQodGhpcy5fYXV0aG9yaXphdGlvbkhlYWRlck5hbWUsIHRoaXMuX2JlYXJlclByZWZpeCArIHRva2VuKTtcbiAgICAgICAgb2JzZXJ2ZXIubmV4dChoZWFkZXJzKTtcbiAgICAgICAgb2JzZXJ2ZXIuY29tcGxldGUoKTtcbiAgICAgIH0gY2F0Y2ggKGVycm9yKSB7XG4gICAgICAgIG9ic2VydmVyLmVycm9yKGVycm9yKTtcbiAgICAgIH1cbiAgICB9KTtcbiAgfVxuXG4gIC8qKlxuICAgKiBSZXR1cm5zIHRoZSBvcmlnaW5hbCBLZXljbG9hayBpbnN0YW5jZSwgaWYgeW91IG5lZWQgYW55IGN1c3RvbWl6YXRpb24gdGhhdFxuICAgKiB0aGlzIEFuZ3VsYXIgc2VydmljZSBkb2VzIG5vdCBzdXBwb3J0IHlldC4gVXNlIHdpdGggY2F1dGlvbi5cbiAgICpcbiAgICogQHJldHVybnNcbiAgICogVGhlIEtleWNsb2FrSW5zdGFuY2UgZnJvbSBrZXljbG9hay1qcy5cbiAgICovXG4gIGdldEtleWNsb2FrSW5zdGFuY2UoKTogS2V5Y2xvYWsuS2V5Y2xvYWtJbnN0YW5jZSB7XG4gICAgcmV0dXJuIHRoaXMuX2luc3RhbmNlO1xuICB9XG5cbiAgLyoqXG4gICAqIFJldHVybnMgdGhlIGV4Y2x1ZGVkIFVSTHMgdGhhdCBzaG91bGQgbm90IGJlIGNvbnNpZGVyZWQgYnlcbiAgICogdGhlIGh0dHAgaW50ZXJjZXB0b3Igd2hpY2ggYXV0b21hdGljYWxseSBhZGRzIHRoZSBhdXRob3JpemF0aW9uIGhlYWRlciBpbiB0aGUgSHR0cCBSZXF1ZXN0LlxuICAgKlxuICAgKiBAcmV0dXJuc1xuICAgKiBUaGUgZXhjbHVkZWQgdXJscyB0aGF0IG11c3Qgbm90IGJlIGludGVyY2VwdGVkIGJ5IHRoZSBLZXljbG9ha0JlYXJlckludGVyY2VwdG9yLlxuICAgKi9cbiAgZ2V0IGJlYXJlckV4Y2x1ZGVkVXJscygpOiBzdHJpbmdbXSB7XG4gICAgcmV0dXJuIHRoaXMuX2JlYXJlckV4Y2x1ZGVkVXJscztcbiAgfVxuXG4gIC8qKlxuICAgKiBGbGFnIHRvIGluZGljYXRlIGlmIHRoZSBiZWFyZXIgd2lsbCBiZSBhZGRlZCB0byB0aGUgYXV0aG9yaXphdGlvbiBoZWFkZXIuXG4gICAqXG4gICAqIEByZXR1cm5zXG4gICAqIFJldHVybnMgaWYgdGhlIGJlYXJlciBpbnRlcmNlcHRvciB3YXMgc2V0IHRvIGJlIGRpc2FibGVkLlxuICAgKi9cbiAgZ2V0IGVuYWJsZUJlYXJlckludGVyY2VwdG9yKCk6IGJvb2xlYW4ge1xuICAgIHJldHVybiB0aGlzLl9lbmFibGVCZWFyZXJJbnRlcmNlcHRvcjtcbiAgfVxuXG4gIC8qKlxuICAgKiBLZXljbG9hayBzdWJqZWN0IHRvIG1vbml0b3IgdGhlIGV2ZW50cyB0cmlnZ2VyZWQgYnkga2V5Y2xvYWstanMuXG4gICAqIFRoZSBmb2xsb3dpbmcgZXZlbnRzIGFzIGF2YWlsYWJsZSAoYXMgZGVzY3JpYmVkIGF0IGtleWNsb2FrIGRvY3MgLVxuICAgKiBodHRwczovL3d3dy5rZXljbG9hay5vcmcvZG9jcy9sYXRlc3Qvc2VjdXJpbmdfYXBwcy9pbmRleC5odG1sI2NhbGxiYWNrLWV2ZW50cyk6XG4gICAqIC0gT25BdXRoRXJyb3JcbiAgICogLSBPbkF1dGhMb2dvdXRcbiAgICogLSBPbkF1dGhSZWZyZXNoRXJyb3JcbiAgICogLSBPbkF1dGhSZWZyZXNoU3VjY2Vzc1xuICAgKiAtIE9uQXV0aFN1Y2Nlc3NcbiAgICogLSBPblJlYWR5XG4gICAqIC0gT25Ub2tlbkV4cGlyZVxuICAgKiBJbiBlYWNoIG9jY3VycmVuY2Ugb2YgYW55IG9mIHRoZXNlLCB0aGlzIHN1YmplY3Qgd2lsbCByZXR1cm4gdGhlIGV2ZW50IHR5cGUsXG4gICAqIGRlc2NyaWJlZCBhdCB7QGxpbmsgS2V5Y2xvYWtFdmVudFR5cGV9IGVudW0gYW5kIHRoZSBmdW5jdGlvbiBhcmdzIGZyb20gdGhlIGtleWNsb2FrLWpzXG4gICAqIGlmIHByb3ZpZGVkIGFueS5cbiAgICpcbiAgICogQHJldHVybnNcbiAgICogQSBzdWJqZWN0IHdpdGggdGhlIHtAbGluayBLZXljbG9ha0V2ZW50fSB3aGljaCBkZXNjcmliZXMgdGhlIGV2ZW50IHR5cGUgYW5kIGF0dGFjaGVzIHRoZVxuICAgKiBmdW5jdGlvbiBhcmdzLlxuICAgKi9cbiAgZ2V0IGtleWNsb2FrRXZlbnRzJCgpOiBTdWJqZWN0PEtleWNsb2FrRXZlbnQ+IHtcbiAgICByZXR1cm4gdGhpcy5fa2V5Y2xvYWtFdmVudHMkO1xuICB9XG59XG4iXX0=