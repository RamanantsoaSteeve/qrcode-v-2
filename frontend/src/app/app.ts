import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TranslocoModule, TranslocoService } from '@ngneat/transloco';
import { AuthService } from './services/auth';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TranslocoModule],
  templateUrl: './app.html',
})
export class App{
  public authService = inject(AuthService)
  private transloco = inject(TranslocoService)

  isDeconnect(){
    localStorage.removeItem('id')
    localStorage.removeItem('user')
    this.authService.logout();
  }

  get activeLang(): string {
    return this.transloco.getActiveLang();
  }
  switchLang(lang:string){
    this.transloco.setActiveLang(lang)
  }
}
