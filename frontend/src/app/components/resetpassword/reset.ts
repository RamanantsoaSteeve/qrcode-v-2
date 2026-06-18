import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { TranslocoDirective } from "@ngneat/transloco";
import { AuthService } from '../../services/auth';
import { Router, RouterLink } from '@angular/router';

const customCheckPassword = (controlName: string, matchingControlName: string): ValidatorFn => {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    const control = formGroup.get(controlName);
    const matchingControl = formGroup.get(matchingControlName);
    if (!control || !matchingControl) return null;

    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({ mustMatch: true });

      return { mustMatch: true };
    } else {
      if (matchingControl.hasError('mustMatch')) {
        matchingControl.setErrors(null);
      }

      return null;
    }
  };
};

@Component({
  selector: 'app-reset',
  imports: [TranslocoDirective, ReactiveFormsModule, RouterLink],
  templateUrl: './reset.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ResetComponent {
  public fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private routes = inject(Router);


  readonly formPassword = this.fb.group({
    password: ['', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/)
    ]],
    checkpassword: ['', [Validators.required]],
  },
    {
      validators: customCheckPassword('password', 'checkpassword')
    });

  recover() {
    const RawValue = this.formPassword.getRawValue();
    const id = localStorage.getItem('id');
    this.authService.sendPassword(id!, RawValue.password!).subscribe((response) => {
      if (response.success) {
        this.routes.navigate(['/interaction']);
      }
    });
  }
}
