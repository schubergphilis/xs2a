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
/**
 * A simple guard implementation out of the box. This class should be inherited and
 * implemented by the application. The only method that should be implemented is #isAccessAllowed.
 * The reason for this is that the authorization flow is usually not unique, so in this way you will
 * have more freedom to customize your authorization flow.
 * @abstract
 */
export class KeycloakAuthGuard {
    /**
     * @param {?} router
     * @param {?} keycloakAngular
     */
    constructor(router, keycloakAngular) {
        this.router = router;
        this.keycloakAngular = keycloakAngular;
    }
    /**
     * CanActivate checks if the user is logged in and get the full list of roles (REALM + CLIENT)
     * of the logged user. This values are set to authenticated and roles params.
     *
     * @param {?} route
     * @param {?} state
     * @return {?}
     */
    canActivate(route, state) {
        return new Promise((resolve, reject) => tslib_1.__awaiter(this, void 0, void 0, function* () {
            try {
                this.authenticated = yield this.keycloakAngular.isLoggedIn();
                this.roles = yield this.keycloakAngular.getUserRoles(true);
                /** @type {?} */
                const result = yield this.isAccessAllowed(route, state);
                resolve(result);
            }
            catch (error) {
                reject('An error happened during access validation. Details:' + error);
            }
        }));
    }
}
function KeycloakAuthGuard_tsickle_Closure_declarations() {
    /**
     * Indicates if the user is authenticated or not.
     * @type {?}
     */
    KeycloakAuthGuard.prototype.authenticated;
    /**
     * Roles of the logged user. It contains the clientId and realm user roles.
     * @type {?}
     */
    KeycloakAuthGuard.prototype.roles;
    /** @type {?} */
    KeycloakAuthGuard.prototype.router;
    /** @type {?} */
    KeycloakAuthGuard.prototype.keycloakAngular;
    /**
     * Create your own customized authorization flow in this method. From here you already known
     * if the user is authenticated (this.authenticated) and the user roles (this.roles).
     *
     * @abstract
     * @param {?} route
     * @param {?} state
     * @return {?}
     */
    KeycloakAuthGuard.prototype.isAccessAllowed = function (route, state) { };
}

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWstYXV0aC1ndWFyZC5qcyIsInNvdXJjZVJvb3QiOiJuZzovL2tleWNsb2FrLWFuZ3VsYXIvIiwic291cmNlcyI6WyJsaWIvY29yZS9zZXJ2aWNlcy9rZXljbG9hay1hdXRoLWd1YXJkLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7QUFrQkEsTUFBTTs7Ozs7SUFVSixZQUFzQixNQUFjLEVBQVksZUFBZ0M7UUFBMUQsV0FBTSxHQUFOLE1BQU0sQ0FBUTtRQUFZLG9CQUFlLEdBQWYsZUFBZSxDQUFpQjtLQUFJOzs7Ozs7Ozs7SUFTcEYsV0FBVyxDQUFDLEtBQTZCLEVBQUUsS0FBMEI7UUFDbkUsTUFBTSxDQUFDLElBQUksT0FBTyxDQUFDLENBQU8sT0FBTyxFQUFFLE1BQU0sRUFBRSxFQUFFO1lBQzNDLElBQUksQ0FBQztnQkFDSCxJQUFJLENBQUMsYUFBYSxHQUFHLE1BQU0sSUFBSSxDQUFDLGVBQWUsQ0FBQyxVQUFVLEVBQUUsQ0FBQztnQkFDN0QsSUFBSSxDQUFDLEtBQUssR0FBRyxNQUFNLElBQUksQ0FBQyxlQUFlLENBQUMsWUFBWSxDQUFDLElBQUksQ0FBQyxDQUFDOztnQkFFM0QsTUFBTSxNQUFNLEdBQUcsTUFBTSxJQUFJLENBQUMsZUFBZSxDQUFDLEtBQUssRUFBRSxLQUFLLENBQUMsQ0FBQztnQkFDeEQsT0FBTyxDQUFDLE1BQU0sQ0FBQyxDQUFDO2FBQ2pCO1lBQUMsS0FBSyxDQUFDLENBQUMsS0FBSyxFQUFFLENBQUM7Z0JBQ2YsTUFBTSxDQUFDLHNEQUFzRCxHQUFHLEtBQUssQ0FBQyxDQUFDO2FBQ3hFO1VBQ0YsQ0FBQyxDQUFDO0tBQ0o7Q0FhRiIsInNvdXJjZXNDb250ZW50IjpbIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBNYXVyaWNpbyBHZW1lbGxpIFZpZ29sbyBhbmQgY29udHJpYnV0b3JzLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGEgTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9naXRodWIuY29tL21hdXJpY2lvdmlnb2xvL2tleWNsb2FrLWFuZ3VsYXIvTElDRU5TRVxuICovXG5cbmltcG9ydCB7IENhbkFjdGl2YXRlLCBSb3V0ZXIsIEFjdGl2YXRlZFJvdXRlU25hcHNob3QsIFJvdXRlclN0YXRlU25hcHNob3QgfSBmcm9tICdAYW5ndWxhci9yb3V0ZXInO1xuXG5pbXBvcnQgeyBLZXljbG9ha1NlcnZpY2UgfSBmcm9tICcuL2tleWNsb2FrLnNlcnZpY2UnO1xuXG4vKipcbiAqIEEgc2ltcGxlIGd1YXJkIGltcGxlbWVudGF0aW9uIG91dCBvZiB0aGUgYm94LiBUaGlzIGNsYXNzIHNob3VsZCBiZSBpbmhlcml0ZWQgYW5kXG4gKiBpbXBsZW1lbnRlZCBieSB0aGUgYXBwbGljYXRpb24uIFRoZSBvbmx5IG1ldGhvZCB0aGF0IHNob3VsZCBiZSBpbXBsZW1lbnRlZCBpcyAjaXNBY2Nlc3NBbGxvd2VkLlxuICogVGhlIHJlYXNvbiBmb3IgdGhpcyBpcyB0aGF0IHRoZSBhdXRob3JpemF0aW9uIGZsb3cgaXMgdXN1YWxseSBub3QgdW5pcXVlLCBzbyBpbiB0aGlzIHdheSB5b3Ugd2lsbFxuICogaGF2ZSBtb3JlIGZyZWVkb20gdG8gY3VzdG9taXplIHlvdXIgYXV0aG9yaXphdGlvbiBmbG93LlxuICovXG5leHBvcnQgYWJzdHJhY3QgY2xhc3MgS2V5Y2xvYWtBdXRoR3VhcmQgaW1wbGVtZW50cyBDYW5BY3RpdmF0ZSB7XG4gIC8qKlxuICAgKiBJbmRpY2F0ZXMgaWYgdGhlIHVzZXIgaXMgYXV0aGVudGljYXRlZCBvciBub3QuXG4gICAqL1xuICBwcm90ZWN0ZWQgYXV0aGVudGljYXRlZDogYm9vbGVhbjtcbiAgLyoqXG4gICAqIFJvbGVzIG9mIHRoZSBsb2dnZWQgdXNlci4gSXQgY29udGFpbnMgdGhlIGNsaWVudElkIGFuZCByZWFsbSB1c2VyIHJvbGVzLlxuICAgKi9cbiAgcHJvdGVjdGVkIHJvbGVzOiBzdHJpbmdbXTtcblxuICBjb25zdHJ1Y3Rvcihwcm90ZWN0ZWQgcm91dGVyOiBSb3V0ZXIsIHByb3RlY3RlZCBrZXljbG9ha0FuZ3VsYXI6IEtleWNsb2FrU2VydmljZSkge31cblxuICAvKipcbiAgICogQ2FuQWN0aXZhdGUgY2hlY2tzIGlmIHRoZSB1c2VyIGlzIGxvZ2dlZCBpbiBhbmQgZ2V0IHRoZSBmdWxsIGxpc3Qgb2Ygcm9sZXMgKFJFQUxNICsgQ0xJRU5UKVxuICAgKiBvZiB0aGUgbG9nZ2VkIHVzZXIuIFRoaXMgdmFsdWVzIGFyZSBzZXQgdG8gYXV0aGVudGljYXRlZCBhbmQgcm9sZXMgcGFyYW1zLlxuICAgKlxuICAgKiBAcGFyYW0gcm91dGVcbiAgICogQHBhcmFtIHN0YXRlXG4gICAqL1xuICBjYW5BY3RpdmF0ZShyb3V0ZTogQWN0aXZhdGVkUm91dGVTbmFwc2hvdCwgc3RhdGU6IFJvdXRlclN0YXRlU25hcHNob3QpOiBQcm9taXNlPGJvb2xlYW4+IHtcbiAgICByZXR1cm4gbmV3IFByb21pc2UoYXN5bmMgKHJlc29sdmUsIHJlamVjdCkgPT4ge1xuICAgICAgdHJ5IHtcbiAgICAgICAgdGhpcy5hdXRoZW50aWNhdGVkID0gYXdhaXQgdGhpcy5rZXljbG9ha0FuZ3VsYXIuaXNMb2dnZWRJbigpO1xuICAgICAgICB0aGlzLnJvbGVzID0gYXdhaXQgdGhpcy5rZXljbG9ha0FuZ3VsYXIuZ2V0VXNlclJvbGVzKHRydWUpO1xuXG4gICAgICAgIGNvbnN0IHJlc3VsdCA9IGF3YWl0IHRoaXMuaXNBY2Nlc3NBbGxvd2VkKHJvdXRlLCBzdGF0ZSk7XG4gICAgICAgIHJlc29sdmUocmVzdWx0KTtcbiAgICAgIH0gY2F0Y2ggKGVycm9yKSB7XG4gICAgICAgIHJlamVjdCgnQW4gZXJyb3IgaGFwcGVuZWQgZHVyaW5nIGFjY2VzcyB2YWxpZGF0aW9uLiBEZXRhaWxzOicgKyBlcnJvcik7XG4gICAgICB9XG4gICAgfSk7XG4gIH1cblxuICAvKipcbiAgICogQ3JlYXRlIHlvdXIgb3duIGN1c3RvbWl6ZWQgYXV0aG9yaXphdGlvbiBmbG93IGluIHRoaXMgbWV0aG9kLiBGcm9tIGhlcmUgeW91IGFscmVhZHkga25vd25cbiAgICogaWYgdGhlIHVzZXIgaXMgYXV0aGVudGljYXRlZCAodGhpcy5hdXRoZW50aWNhdGVkKSBhbmQgdGhlIHVzZXIgcm9sZXMgKHRoaXMucm9sZXMpLlxuICAgKlxuICAgKiBAcGFyYW0gcm91dGVcbiAgICogQHBhcmFtIHN0YXRlXG4gICAqL1xuICBhYnN0cmFjdCBpc0FjY2Vzc0FsbG93ZWQoXG4gICAgcm91dGU6IEFjdGl2YXRlZFJvdXRlU25hcHNob3QsXG4gICAgc3RhdGU6IFJvdXRlclN0YXRlU25hcHNob3RcbiAgKTogUHJvbWlzZTxib29sZWFuPjtcbn1cbiJdfQ==