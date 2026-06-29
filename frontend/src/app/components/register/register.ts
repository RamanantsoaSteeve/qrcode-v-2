import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslocoModule } from '@ngneat/transloco';
import { AuthService } from '../../services/auth';

import { finalize, Subject, takeUntil, timer } from 'rxjs';

const customCheckPassword = (controlName: string, matchingControlName: string): ValidatorFn => {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    const control = formGroup.get(controlName);
    const matchingControl = formGroup.get(matchingControlName);

    if (!control || !matchingControl) {
      return null;
    }

    if (matchingControl.errors && !matchingControl.errors['mustMatch']) {
      return null;
    }

    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({ mustMatch: true });
      return { mustMatch: true };
    }

    matchingControl.setErrors(null);
    return null;
  };
};

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, CommonModule, TranslocoModule],
  templateUrl: './register.html',
})


export class Register {
  private routes = inject(Router);
  public authService = inject(AuthService);
  private fb = inject(FormBuilder);
  public isLoading = signal<boolean>(false);
  public isRegister = signal<boolean>(false);
  private destroy$ = new Subject<void>();

  readonly formCheck = this.fb.group({
    value1: ['', [Validators.required, Validators.pattern('[0-9]')]],
    value2: ['', [Validators.required, Validators.pattern('[0-9]')]],
    value3: ['', [Validators.required, Validators.pattern('[0-9]')]],
    value4: ['', [Validators.required, Validators.pattern('[0-9]')]]

  });

  readonly formRegister = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(4)]],
    email: ['', [Validators.required, Validators.email]],
    password: [
      '',
      [
        Validators.required,
        Validators.minLength(5),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/),
      ],
    ],
    checkpassword: ['', [Validators.required]],
  }, { validators: customCheckPassword('password', 'checkpassword') });


  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  sendEmail() {
    const formRegisterRaw = this.formRegister.getRawValue();
    this.isLoading.set(true);

    this.authService
      .sendEmail('', formRegisterRaw.email!, 'Verifier votre email')
      .subscribe({
        next: () => {
          this.isRegister.set(true);
        },
        error: ({ error }) => {
          this.isLoading.set(false);
          console.log(error.message);
        }
      });
  }

  recoverCode() {
    const rawValuCeckCode = this.formCheck.getRawValue();
    const code = Number(rawValuCeckCode.value1! + rawValuCeckCode.value2! + rawValuCeckCode.value3! + rawValuCeckCode.value4!);
    this.authService
      .sendCode(this.formRegister.value.username!, this.formRegister.value.email!, this.formRegister.value.password!, code)
      .subscribe({
        next: (response) => {
          if (response.success) {
            localStorage.setItem('id', String(response.id));
            localStorage.setItem('user', this.formRegister.value.username!);
            this.authService.showToast.set(true);
            timer(10000)
              .pipe(takeUntil(this.destroy$))
              .subscribe(() => {
                this.authService.showToast.set(false);
              });

            this.authService.saveUser(this.formRegister.value.username!);
            this.authService.saveToken(response.token);
            this.routes.navigate(['/interaction']);
          }
        },

        error: ({ error }) => {
          console.log(error.success);

          if (!error.success) {
            this.authService.showToast.set(true);
            timer(10000)
              .pipe(takeUntil(this.destroy$))
              .subscribe(() => {
                this.authService.showToast.set(false);
              });
            this.isRegister.set(false);

            this.routes.navigate(['/home']);
          }
        }
      });
  }
}
