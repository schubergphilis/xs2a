import { HttpHeaders } from '@angular/common/http';
import * as Keycloak_ from 'keycloak-js';
export declare const Keycloak: typeof Keycloak_;
import { Observable, Subject } from 'rxjs';
import { KeycloakOptions } from '../interfaces/keycloak-options';
import { KeycloakEvent } from '../interfaces/keycloak-event';
/**
 * Service to expose existent methods from the Keycloak JS adapter, adding new
 * functionalities to improve the use of keycloak in Angular v > 4.3 applications.
 *
 * This class should be injected in the application bootstrap, so the same instance will be used
 * along the web application.
 */
export declare class KeycloakService {
    /**
     * Keycloak-js instance.
     */
    private _instance;
    /**
     * User profile as KeycloakProfile interface.
     */
    private _userProfile;
    /**
     * Flag to indicate if the bearer will not be added to the authorization header.
     */
    private _enableBearerInterceptor;
    /**
     * When the implicit flow is choosen there must exist a silentRefresh, as there is
     * no refresh token.
     */
    private _silentRefresh;
    /**
     * Indicates that the user profile should be loaded at the keycloak initialization,
     * just after the login.
     */
    private _loadUserProfileAtStartUp;
    /**
     * The bearer prefix that will be appended to the Authorization Header.
     */
    private _bearerPrefix;
    /**
     * Value that will be used as the Authorization Http Header name.
     */
    private _authorizationHeaderName;
    /**
     * The excluded urls patterns that must skip the KeycloakBearerInterceptor.
     */
    private _bearerExcludedUrls;
    /**
     * Observer for the keycloak events
     */
    private _keycloakEvents$;
    constructor();
    /**
     * Sanitizes the bearer prefix, preparing it to be appended to
     * the token.
     *
     * @param bearerPrefix
     * Prefix to be appended to the authorization header as
     * Authorization: <bearer-prefix> <token>.
     * @returns
     * The bearer prefix sanitized, meaning that it will follow the bearerPrefix
     * param as described in the library initilization or the default value bearer,
     * with a space append in the end for the token concatenation.
     */
    private sanitizeBearerPrefix(bearerPrefix);
    /**
     * Sets default value to true if it is undefined or null.
     *
     * @param value - boolean value to be checked
     */
    private ifUndefinedIsTrue(value);
    /**
     * Binds the keycloak-js events to the keycloakEvents Subject
     * which is a good way to monitor for changes, if needed.
     *
     * The keycloakEvents returns the keycloak-js event type and any
     * argument if the source function provides any.
     */
    private bindsKeycloakEvents();
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
    init(options?: KeycloakOptions): Promise<boolean>;
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
    login(options?: Keycloak.KeycloakLoginOptions): Promise<void>;
    /**
     * Redirects to logout.
     *
     * @param redirectUri
     * Specifies the uri to redirect to after logout.
     * @returns
     * A void Promise if the logout was successful, cleaning also the userProfile.
     */
    logout(redirectUri?: string): Promise<void>;
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
    register(options?: Keycloak.KeycloakLoginOptions): Promise<void>;
    /**
     * Check if the user has access to the specified role. It will look for roles in
     * realm and clientId, but will not check if the user is logged in for better performance.
     *
     * @param role
     * role name
     * @returns
     * A boolean meaning if the user has the specified Role.
     */
    isUserInRole(role: string): boolean;
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
    getUserRoles(allRoles?: boolean): string[];
    /**
     * Check if user is logged in.
     *
     * @returns
     * A boolean that indicates if the user is logged in.
     */
    isLoggedIn(): Promise<boolean>;
    /**
     * Returns true if the token has less than minValidity seconds left before
     * it expires.
     *
     * @param minValidity
     * Seconds left. (minValidity) is optional. Default value is 0.
     * @returns
     * Boolean indicating if the token is expired.
     */
    isTokenExpired(minValidity?: number): boolean;
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
    updateToken(minValidity?: number): Promise<boolean>;
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
    loadUserProfile(forceReload?: boolean): Promise<Keycloak.KeycloakProfile>;
    /**
     * Returns the authenticated token, calling updateToken to get a refreshed one if
     * necessary. If the session is expired this method calls the login method for a new login.
     *
     * @returns
     * Promise with the generated token.
     */
    getToken(): Promise<string>;
    /**
     * Returns the logged username.
     *
     * @returns
     * The logged username.
     */
    getUsername(): string;
    /**
     * Clear authentication state, including tokens. This can be useful if application
     * has detected the session was expired, for example if updating token fails.
     * Invoking this results in onAuthLogout callback listener being invoked.
     */
    clearToken(): void;
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
    addTokenToHeader(headersArg?: HttpHeaders): Observable<HttpHeaders>;
    /**
     * Returns the original Keycloak instance, if you need any customization that
     * this Angular service does not support yet. Use with caution.
     *
     * @returns
     * The KeycloakInstance from keycloak-js.
     */
    getKeycloakInstance(): Keycloak.KeycloakInstance;
    /**
     * Returns the excluded URLs that should not be considered by
     * the http interceptor which automatically adds the authorization header in the Http Request.
     *
     * @returns
     * The excluded urls that must not be intercepted by the KeycloakBearerInterceptor.
     */
    readonly bearerExcludedUrls: string[];
    /**
     * Flag to indicate if the bearer will be added to the authorization header.
     *
     * @returns
     * Returns if the bearer interceptor was set to be disabled.
     */
    readonly enableBearerInterceptor: boolean;
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
    readonly keycloakEvents$: Subject<KeycloakEvent>;
}
