import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';

export const tokenGuardGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);


  if (authService.isConnected()) {
    return true;
  }

  return router.navigateByUrl('/home');
};


export const authRouteGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isConnected()) {
    return router.navigateByUrl('/interaction');
  }

  return true;
}
