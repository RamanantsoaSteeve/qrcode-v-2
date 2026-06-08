import { Component, inject, input, OnInit, signal } from '@angular/core';
import { RouterLink } from "@angular/router";
import { TranslocoModule } from '@ngneat/transloco';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-interaction',
  standalone: true,
  imports: [RouterLink, TranslocoModule],
  templateUrl: './interaction.html',
})
export class Interaction implements OnInit {
  public authService = inject(AuthService);

  readonly user = signal('');

  ngOnInit(): void {
    this.user.set(localStorage.getItem('user')!);
  }

}
