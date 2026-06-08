import { Component, inject, signal } from '@angular/core';
import {
  FormBuilder, ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslocoModule } from '@ngneat/transloco';
import { Auth, GoogleAuthProvider, signInWithPopup, user } from '@angular/fire/auth';


import { AuthService } from '../../services/auth';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, CommonModule, TranslocoModule],
  templateUrl: './login.html',
})
export class Login {
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(Auth);
  readonly foundNamemail = signal<boolean>(false);
  public readonly isClick = signal(false);


  user$ = user(this.auth);
  isLoading = signal<boolean>(false);

  readonly formLogin = this.fb.group({
    email: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  login() {
    this.isLoading.set(true);
    const formraw = this.formLogin.getRawValue();


    this.authService
      .login(formraw.email!, formraw.password!)
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (response) => {
          this.authService.saveToken(response.token!);
          this.authService.saveUser(response.user?.username!);
          localStorage.setItem('id', String(response.user?.id));
          localStorage.setItem('user', String(response.user?.username));

          return this.router.navigate(['/interaction']);
        },
        error: ({ error }) => {
          console.log(error.error.message);
          alert('Something went wrong');
        },
      });
  }


  async googleSignIn() {
    try {
      this.isClick.set(true);
      const result = await signInWithPopup(this.auth, new GoogleAuthProvider());
      if (result.user.email) {
        this.authService.sendEmailFirebase(result.user.email).subscribe(res => {
          if (res.success) {
            this.authService.saveToken(res.token);
            localStorage.setItem('id', String(res.id));
            localStorage.setItem('user', res.username);

            return this.router.navigate(['/interaction']);
          }

          this.isClick.set(false);

          return this.router.navigate(['/home']);
        });
      }

      this.isClick.set(false);
      this.router.navigate(['/register']);
    } catch (error) {
      console.error('Erreur de connexion :', error);
    }
  }


}
