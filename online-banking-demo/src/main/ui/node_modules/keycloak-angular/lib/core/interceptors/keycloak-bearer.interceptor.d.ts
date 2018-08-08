import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { KeycloakService } from '../services/keycloak.service';
/**
 * This interceptor includes the bearer by default in all HttpClient requests.
 *
 * If you need to exclude some URLs from adding the bearer, please, take a look
 * at the {@link KeycloakOptions} bearerExcludedUrls property.
 */
export declare class KeycloakBearerInterceptor implements HttpInterceptor {
    private keycloak;
    private excludedUrlsRegex;
    /**
     * KeycloakBearerInterceptor constructor.
     *
     * @param keycloak - Injected KeycloakService instance.
     */
    constructor(keycloak: KeycloakService);
    private loadExcludedUrlsRegex();
    /**
     * Intercept implementation that checks if the request url matches the excludedUrls.
     * If not, adds the Authorization header to the request.
     *
     * @param req
     * @param next
     */
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>;
}
