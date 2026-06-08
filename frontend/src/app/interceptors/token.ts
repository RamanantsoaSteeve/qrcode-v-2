import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

export const AuthIntercepter: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  if (!authService.token) {
    return next(req);
  }

  const authReq = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${authService.token}`)
  });

  return next(authReq);
};
