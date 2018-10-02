import { KeycloakService } from 'keycloak-angular';

import { ConfigService } from '../service/config.service';
import { Config } from '../model/Config';

export function initializer(keycloakService: KeycloakService, configService: ConfigService): () => Promise<any> {
  return (): Promise<any> => configService.loadConfig().then((config: Config) => {
    configService.setConfig(config);
    console.log('awi configService getConfig', configService.getConfig());
    keycloakInit(keycloakService, configService).then();
  });


  // return (): Promise<any> => {
  //   return new Promise( async (resolve, reject) => {
  //     configService.loadConfig().then((config: Config) => {
  //         configService.setConfig(config);
  //         console.log('awi configService getConfig', configService.getConfig());
  //         keycloakInit(keycloakService, configService);
  //       });
  //   });
  // };
}

export function keycloakInit(keycloak: KeycloakService, configService: ConfigService): Promise<any> {
    return new Promise(async (resolve, reject) => {
      try {
        console.log('awi keycloakInit');
        await keycloak.init({
          config: configService.getConfig().keycloakConfig,
          initOptions: {
            onLoad: 'login-required',
            checkLoginIframe: false,
            flow: 'implicit',
          },
          enableBearerInterceptor: true,
          bearerExcludedUrls: []
        });
        resolve();
      } catch (error) {
        reject(error);
      }
    });
}
