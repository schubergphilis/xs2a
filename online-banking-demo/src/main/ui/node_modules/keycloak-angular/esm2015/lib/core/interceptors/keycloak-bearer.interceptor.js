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
import { Injectable } from '@angular/core';
import { mergeMap } from 'rxjs/operators';
import { KeycloakService } from '../services/keycloak.service';
/**
 * This interceptor includes the bearer by default in all HttpClient requests.
 *
 * If you need to exclude some URLs from adding the bearer, please, take a look
 * at the {\@link KeycloakOptions} bearerExcludedUrls property.
 */
export class KeycloakBearerInterceptor {
    /**
     * KeycloakBearerInterceptor constructor.
     *
     * @param {?} keycloak - Injected KeycloakService instance.
     */
    constructor(keycloak) {
        this.keycloak = keycloak;
    }
    /**
     * @return {?}
     */
    loadExcludedUrlsRegex() {
        /** @type {?} */
        const excludedUrls = this.keycloak.bearerExcludedUrls;
        this.excludedUrlsRegex = excludedUrls.map(urlPattern => new RegExp(urlPattern, 'i')) || [];
    }
    /**
     * Intercept implementation that checks if the request url matches the excludedUrls.
     * If not, adds the Authorization header to the request.
     *
     * @param {?} req
     * @param {?} next
     * @return {?}
     */
    intercept(req, next) {
        // If keycloak service is not initialized yet, or the interceptor should not be execute
        if (!this.keycloak || !this.keycloak.enableBearerInterceptor) {
            return next.handle(req);
        }
        if (!this.excludedUrlsRegex) {
            this.loadExcludedUrlsRegex();
        }
        /** @type {?} */
        const urlRequest = req.url;
        /** @type {?} */
        const shallPass = !!this.excludedUrlsRegex.find(regex => regex.test(urlRequest));
        if (shallPass) {
            return next.handle(req);
        }
        return this.keycloak.addTokenToHeader(req.headers).pipe(mergeMap(headersWithBearer => {
            /** @type {?} */
            const kcReq = req.clone({ headers: headersWithBearer });
            return next.handle(kcReq);
        }));
    }
}
KeycloakBearerInterceptor.decorators = [
    { type: Injectable },
];
/** @nocollapse */
KeycloakBearerInterceptor.ctorParameters = () => [
    { type: KeycloakService }
];
function KeycloakBearerInterceptor_tsickle_Closure_declarations() {
    /** @type {?} */
    KeycloakBearerInterceptor.prototype.excludedUrlsRegex;
    /** @type {?} */
    KeycloakBearerInterceptor.prototype.keycloak;
}

//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoia2V5Y2xvYWstYmVhcmVyLmludGVyY2VwdG9yLmpzIiwic291cmNlUm9vdCI6Im5nOi8va2V5Y2xvYWstYW5ndWxhci8iLCJzb3VyY2VzIjpbImxpYi9jb3JlL2ludGVyY2VwdG9ycy9rZXljbG9hay1iZWFyZXIuaW50ZXJjZXB0b3IudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7QUFRQSxPQUFPLEVBQUUsVUFBVSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBVTNDLE9BQU8sRUFBRSxRQUFRLEVBQUUsTUFBTSxnQkFBZ0IsQ0FBQztBQUUxQyxPQUFPLEVBQUUsZUFBZSxFQUFFLE1BQU0sOEJBQThCLENBQUM7Ozs7Ozs7QUFTL0QsTUFBTTs7Ozs7O0lBUUosWUFBb0IsUUFBeUI7UUFBekIsYUFBUSxHQUFSLFFBQVEsQ0FBaUI7S0FBSTs7OztJQUV6QyxxQkFBcUI7O1FBQzNCLE1BQU0sWUFBWSxHQUFhLElBQUksQ0FBQyxRQUFRLENBQUMsa0JBQWtCLENBQUM7UUFDaEUsSUFBSSxDQUFDLGlCQUFpQixHQUFHLFlBQVksQ0FBQyxHQUFHLENBQUMsVUFBVSxDQUFDLEVBQUUsQ0FBQyxJQUFJLE1BQU0sQ0FBQyxVQUFVLEVBQUUsR0FBRyxDQUFDLENBQUMsSUFBSSxFQUFFLENBQUM7Ozs7Ozs7Ozs7SUFVdEYsU0FBUyxDQUFDLEdBQXFCLEVBQUUsSUFBaUI7O1FBRXZELEVBQUUsQ0FBQyxDQUFDLENBQUMsSUFBSSxDQUFDLFFBQVEsSUFBSSxDQUFDLElBQUksQ0FBQyxRQUFRLENBQUMsdUJBQXVCLENBQUMsQ0FBQyxDQUFDO1lBQzdELE1BQU0sQ0FBQyxJQUFJLENBQUMsTUFBTSxDQUFDLEdBQUcsQ0FBQyxDQUFDO1NBQ3pCO1FBRUQsRUFBRSxDQUFDLENBQUMsQ0FBQyxJQUFJLENBQUMsaUJBQWlCLENBQUMsQ0FBQyxDQUFDO1lBQzVCLElBQUksQ0FBQyxxQkFBcUIsRUFBRSxDQUFDO1NBQzlCOztRQUVELE1BQU0sVUFBVSxHQUFHLEdBQUcsQ0FBQyxHQUFHLENBQUM7O1FBQzNCLE1BQU0sU0FBUyxHQUFZLENBQUMsQ0FBQyxJQUFJLENBQUMsaUJBQWlCLENBQUMsSUFBSSxDQUFDLEtBQUssQ0FBQyxFQUFFLENBQUMsS0FBSyxDQUFDLElBQUksQ0FBQyxVQUFVLENBQUMsQ0FBQyxDQUFDO1FBQzFGLEVBQUUsQ0FBQyxDQUFDLFNBQVMsQ0FBQyxDQUFDLENBQUM7WUFDZCxNQUFNLENBQUMsSUFBSSxDQUFDLE1BQU0sQ0FBQyxHQUFHLENBQUMsQ0FBQztTQUN6QjtRQUVELE1BQU0sQ0FBQyxJQUFJLENBQUMsUUFBUSxDQUFDLGdCQUFnQixDQUFDLEdBQUcsQ0FBQyxPQUFPLENBQUMsQ0FBQyxJQUFJLENBQ3JELFFBQVEsQ0FBQyxpQkFBaUIsQ0FBQyxFQUFFOztZQUMzQixNQUFNLEtBQUssR0FBRyxHQUFHLENBQUMsS0FBSyxDQUFDLEVBQUUsT0FBTyxFQUFFLGlCQUFpQixFQUFFLENBQUMsQ0FBQztZQUN4RCxNQUFNLENBQUMsSUFBSSxDQUFDLE1BQU0sQ0FBQyxLQUFLLENBQUMsQ0FBQztTQUMzQixDQUFDLENBQ0gsQ0FBQzs7OztZQTVDTCxVQUFVOzs7O1lBUkYsZUFBZSIsInNvdXJjZXNDb250ZW50IjpbIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBNYXVyaWNpbyBHZW1lbGxpIFZpZ29sbyBhbmQgY29udHJpYnV0b3JzLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGEgTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9naXRodWIuY29tL21hdXJpY2lvdmlnb2xvL2tleWNsb2FrLWFuZ3VsYXIvTElDRU5TRVxuICovXG5cbmltcG9ydCB7IEluamVjdGFibGUgfSBmcm9tICdAYW5ndWxhci9jb3JlJztcbmltcG9ydCB7XG4gIEh0dHBJbnRlcmNlcHRvcixcbiAgSHR0cFJlcXVlc3QsXG4gIEh0dHBIYW5kbGVyLFxuICBIdHRwRXZlbnQsXG4gIEh0dHBIZWFkZXJzXG59IGZyb20gJ0Bhbmd1bGFyL2NvbW1vbi9odHRwJztcblxuaW1wb3J0IHsgT2JzZXJ2YWJsZSB9IGZyb20gJ3J4anMnO1xuaW1wb3J0IHsgbWVyZ2VNYXAgfSBmcm9tICdyeGpzL29wZXJhdG9ycyc7XG5cbmltcG9ydCB7IEtleWNsb2FrU2VydmljZSB9IGZyb20gJy4uL3NlcnZpY2VzL2tleWNsb2FrLnNlcnZpY2UnO1xuXG4vKipcbiAqIFRoaXMgaW50ZXJjZXB0b3IgaW5jbHVkZXMgdGhlIGJlYXJlciBieSBkZWZhdWx0IGluIGFsbCBIdHRwQ2xpZW50IHJlcXVlc3RzLlxuICpcbiAqIElmIHlvdSBuZWVkIHRvIGV4Y2x1ZGUgc29tZSBVUkxzIGZyb20gYWRkaW5nIHRoZSBiZWFyZXIsIHBsZWFzZSwgdGFrZSBhIGxvb2tcbiAqIGF0IHRoZSB7QGxpbmsgS2V5Y2xvYWtPcHRpb25zfSBiZWFyZXJFeGNsdWRlZFVybHMgcHJvcGVydHkuXG4gKi9cbkBJbmplY3RhYmxlKClcbmV4cG9ydCBjbGFzcyBLZXljbG9ha0JlYXJlckludGVyY2VwdG9yIGltcGxlbWVudHMgSHR0cEludGVyY2VwdG9yIHtcbiAgcHJpdmF0ZSBleGNsdWRlZFVybHNSZWdleDogUmVnRXhwW107XG5cbiAgLyoqXG4gICAqIEtleWNsb2FrQmVhcmVySW50ZXJjZXB0b3IgY29uc3RydWN0b3IuXG4gICAqXG4gICAqIEBwYXJhbSBrZXljbG9hayAtIEluamVjdGVkIEtleWNsb2FrU2VydmljZSBpbnN0YW5jZS5cbiAgICovXG4gIGNvbnN0cnVjdG9yKHByaXZhdGUga2V5Y2xvYWs6IEtleWNsb2FrU2VydmljZSkge31cblxuICBwcml2YXRlIGxvYWRFeGNsdWRlZFVybHNSZWdleCgpIHtcbiAgICBjb25zdCBleGNsdWRlZFVybHM6IHN0cmluZ1tdID0gdGhpcy5rZXljbG9hay5iZWFyZXJFeGNsdWRlZFVybHM7XG4gICAgdGhpcy5leGNsdWRlZFVybHNSZWdleCA9IGV4Y2x1ZGVkVXJscy5tYXAodXJsUGF0dGVybiA9PiBuZXcgUmVnRXhwKHVybFBhdHRlcm4sICdpJykpIHx8IFtdO1xuICB9XG5cbiAgLyoqXG4gICAqIEludGVyY2VwdCBpbXBsZW1lbnRhdGlvbiB0aGF0IGNoZWNrcyBpZiB0aGUgcmVxdWVzdCB1cmwgbWF0Y2hlcyB0aGUgZXhjbHVkZWRVcmxzLlxuICAgKiBJZiBub3QsIGFkZHMgdGhlIEF1dGhvcml6YXRpb24gaGVhZGVyIHRvIHRoZSByZXF1ZXN0LlxuICAgKlxuICAgKiBAcGFyYW0gcmVxXG4gICAqIEBwYXJhbSBuZXh0XG4gICAqL1xuICBwdWJsaWMgaW50ZXJjZXB0KHJlcTogSHR0cFJlcXVlc3Q8YW55PiwgbmV4dDogSHR0cEhhbmRsZXIpOiBPYnNlcnZhYmxlPEh0dHBFdmVudDxhbnk+PiB7XG4gICAgLy8gSWYga2V5Y2xvYWsgc2VydmljZSBpcyBub3QgaW5pdGlhbGl6ZWQgeWV0LCBvciB0aGUgaW50ZXJjZXB0b3Igc2hvdWxkIG5vdCBiZSBleGVjdXRlXG4gICAgaWYgKCF0aGlzLmtleWNsb2FrIHx8ICF0aGlzLmtleWNsb2FrLmVuYWJsZUJlYXJlckludGVyY2VwdG9yKSB7XG4gICAgICByZXR1cm4gbmV4dC5oYW5kbGUocmVxKTtcbiAgICB9XG5cbiAgICBpZiAoIXRoaXMuZXhjbHVkZWRVcmxzUmVnZXgpIHtcbiAgICAgIHRoaXMubG9hZEV4Y2x1ZGVkVXJsc1JlZ2V4KCk7XG4gICAgfVxuXG4gICAgY29uc3QgdXJsUmVxdWVzdCA9IHJlcS51cmw7XG4gICAgY29uc3Qgc2hhbGxQYXNzOiBib29sZWFuID0gISF0aGlzLmV4Y2x1ZGVkVXJsc1JlZ2V4LmZpbmQocmVnZXggPT4gcmVnZXgudGVzdCh1cmxSZXF1ZXN0KSk7XG4gICAgaWYgKHNoYWxsUGFzcykge1xuICAgICAgcmV0dXJuIG5leHQuaGFuZGxlKHJlcSk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMua2V5Y2xvYWsuYWRkVG9rZW5Ub0hlYWRlcihyZXEuaGVhZGVycykucGlwZShcbiAgICAgIG1lcmdlTWFwKGhlYWRlcnNXaXRoQmVhcmVyID0+IHtcbiAgICAgICAgY29uc3Qga2NSZXEgPSByZXEuY2xvbmUoeyBoZWFkZXJzOiBoZWFkZXJzV2l0aEJlYXJlciB9KTtcbiAgICAgICAgcmV0dXJuIG5leHQuaGFuZGxlKGtjUmVxKTtcbiAgICAgIH0pXG4gICAgKTtcbiAgfVxufVxuIl19