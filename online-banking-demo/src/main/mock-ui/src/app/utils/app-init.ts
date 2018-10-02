import { KeycloakService } from 'keycloak-angular';

import { ConfigService } from '../service/config.service';
import { Config } from '../model/Config';

export function initializer(keycloakService: KeycloakService, configService: ConfigService): () => Promise<any> {
  return (): Promise<any> => configService.loadConfig().then((config: Config) => {
    configService.setConfig(config);
    console.log('awi configService getConfig', configService.getConfig());
    return keycloakInit(keycloakService, configService);
  });
}

export function keycloakInit(keycloak: KeycloakService, configService: ConfigService): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        await keycloak.init({
          config: configService.getConfig().keycloakConfig,
          initOptions: {
            onLoad: 'login-required',
            checkLoginIframe: false,
            flow: 'implicit',
          },
          enableBearerInterceptor: true,
          bearerExcludedUrls: ['http://localhost:28081/configuration/properties']
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
  };
}
