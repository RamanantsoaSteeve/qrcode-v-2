import { Routes } from '@angular/router';
import { tokenGuardGuard, authRouteGuard } from './guards/token.guard';

export const routes: Routes = [
  {
    path: 'home',
    loadComponent: () => import('./components/home/home').then((m) => m.Home),
    canActivate: [authRouteGuard],
  },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then((m) => m.Login),
    canActivate: [authRouteGuard],
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./components/forgotpassword/forgot/forgot').then((m) => m.ForgotComponent),
    canActivate: [authRouteGuard],
  },
  {
    path: 'reset-password',
    loadComponent: () => import('./components/resetpassword/reset/reset').then((m) => m.ResetComponent),
    canActivate: [tokenGuardGuard],
  },
  {
    path: 'register',
    loadComponent: () => import('./components/register/register').then((m) => m.Register),
    canActivate: [authRouteGuard],
  },
  {
    path: 'interaction',
    loadComponent: () => import('./components/interaction/interaction').then((m) => m.Interaction),
    canActivate: [tokenGuardGuard],
  },
  {
    path: 'qrcode',
    loadComponent: () => import('./components/qrcode/qrcode').then((m) => m.Qrcode),
    canActivate: [tokenGuardGuard],
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./components/dashboard/dashboard').then((m) => m.DashboardComponent),
    canActivate: [tokenGuardGuard],
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
  },
];
