import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoModule, TranslocoService } from '@ngneat/transloco';

@Component({
  selector: 'app-home',
  standalone:true,
  imports: [RouterLink, TranslocoModule],
  templateUrl: './home.html',
})
export class Home {
}
