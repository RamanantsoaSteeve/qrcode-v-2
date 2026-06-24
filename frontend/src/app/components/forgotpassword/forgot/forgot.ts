import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from "@angular/router";
import { TranslocoDirective } from "@ngneat/transloco";
import { AuthService } from '../../../services/auth';

@Component({
  selector: 'app-forgot',
  standalone: true,
  imports: [RouterLink, TranslocoDirective, ReactiveFormsModule],
  templateUrl: './forgot.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ForgotComponent {
  private authService = inject(AuthService);
  private routes = inject(Router);
  private fb = inject(FormBuilder);
  readonly sendEmail = signal(false);

  readonly formEmail = this.fb.group({
    email: ['', [Validators.email, Validators.required]]
  });

  readonly formCheck = this.fb.group({
    value1: ['', [Validators.required, Validators.pattern('[0-9]')]],
    value2: ['', [Validators.required, Validators.pattern('[0-9]')]],
    value3: ['', [Validators.required, Validators.pattern('[0-9]')]],
    value4: ['', [Validators.required, Validators.pattern('[0-9]')]]

  });


  reset() {
    this.sendEmail.set(true);
    const valueRaw = this.formEmail.getRawValue();
    this.authService
      .sendEmail('', valueRaw.email!, 'Verifier votre email')
      .subscribe({
        next: (response) => {
          console.log(response.message);
        }
      });
  }


  recoverCode() {
    const RawFormCheck = this.formCheck.getRawValue();
    const code = Number(RawFormCheck.value1! + RawFormCheck.value2! + RawFormCheck.value3! + RawFormCheck.value4!);

    this.authService.getId(this.formEmail.value.email!).subscribe({
      next: (response) => {

        if (response.success) {
          this.authService.sendCodeForgot(code, this.formEmail.value.email!).subscribe({
            next: (response2) => {
              localStorage.setItem('id', String(response.id));
              this.authService.saveToken(response2.token);
              this.routes.navigate(['/reset-password']);

            },
            error: ({ error }) => {
              if (!error.success) {
                this.sendEmail.set(false);
                this.routes.navigate(['/login']);
              }

            }
          });
        }
      }
    });
  }
}
