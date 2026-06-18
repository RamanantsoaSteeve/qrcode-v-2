import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthResponse, AuthResponseLogin, AuthResponseLoginFireBase, AuthResponsePassword, AuthResponseRegister, AuthResponseToken } from '../models/response';

import { config } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})

export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private readonly API = `${config.url}/auth`;
  public showToast = signal<boolean>(false);

  get token(): string | null {
    return localStorage.getItem('token');
  }

  createUser(username: string, email: string, password: string): Observable<AuthResponseRegister> {
    return this.http.post<AuthResponseRegister>(
      `${this.API}/register`,
      { username, email, password }
    );
  }

  login(email: string, password: string): Observable<AuthResponseLogin> {
    return this.http.post<AuthResponseLogin>(`${this.API}/login`, { email, password });
  }

  checkToken(): Observable<AuthResponseToken> {
    return this.http.get<AuthResponseToken>(`${this.API}/check/token`);
  }

  removeToken(): Observable<AuthResponseToken> {
    return this.http.get<AuthResponseToken>(`${this.API}/remove/token`);
  }

  getId(email: string): Observable<AuthResponseRegister> {
    return this.http.post<AuthResponseRegister>(`${this.API}/check-user`, { email });
  }

  isConnected() {
    return Boolean(localStorage.getItem('token'));
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  saveUser(user: string) {
    localStorage.setItem('user', user);
  }

  getUser() {
    return JSON.parse(localStorage.getItem('user') || '{}');
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  sendEmail(text: string, to: string, subject: string): Observable<AuthResponseToken> {
    return this.http.post<AuthResponseToken>(`${this.API}/send-email`, { text, to, subject });
  }

  sendCode(email: string, code: number, id: number): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API}/send-code`, { email, code, id });
  }

  sendCodeForgot(email: string, code: number, id: number): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API}/send-code-forgot`, { email, code, id });
  }

  sendPassword(id: string, password: string): Observable<AuthResponsePassword> {
    return this.http.post<AuthResponsePassword>(`${this.API}/change-password-user`, { password, id: Number(id) });
  }

  sendEmailFirebase(email: string): Observable<AuthResponseLoginFireBase> {
    return this.http.post<AuthResponseLoginFireBase>(`${this.API}/check-email-firebase`, { email });
  }
}
