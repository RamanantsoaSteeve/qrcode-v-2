import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslocoModule, TranslocoService } from '@ngneat/transloco';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TranslocoModule],
  templateUrl: './app.html',
})
export class App {

}
