
import { ApplicationConfig, isDevMode } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideFirebaseApp, initializeApp } from '@angular/fire/app';
import { provideAuth, getAuth } from '@angular/fire/auth';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideTransloco } from '@ngneat/transloco';

import { routes } from './app.routes';
import { TranslocoHttpLoader } from './transloco-loader';
import { AuthIntercepter } from './interceptors/token';
import { firebaseConfig } from '../environments/environment';

export const appConfig: ApplicationConfig = {
  providers: [
    provideFirebaseApp(() => initializeApp(firebaseConfig)),
    provideAuth(() => getAuth()),
    provideHttpClient(
      withInterceptors([AuthIntercepter])
    ),
    provideRouter(routes),
    provideHttpClient(), provideTransloco({
      config: {
        availableLangs: ['en', 'fr', 'mg'],
        defaultLang: 'en',
        reRenderOnLangChange: true,
        prodMode: !isDevMode(),
      },
      loader: TranslocoHttpLoader
    })
  ]
};
