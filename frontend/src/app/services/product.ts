import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductResponse, ProductState, qrcodeResponse } from '../models/product';
import { config } from '../../environments/environment';
import { AuthResponsePassword, AuthResponseToken } from '../models/response';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly http = inject(HttpClient);

  readonly product = signal<ProductState>({});

  private readonly API = `${config.url}/product`;

  getProduct(id: string): Observable<ProductResponse[]> {
    return this.http.post<ProductResponse[]>(`${this.API}/get`, { id });
  }

  createProduct(price: number, name: string, currency: string, userId: string): Observable<qrcodeResponse> {
    return this.http.post<qrcodeResponse>(`${this.API}/create`, { price, name, currencySymbol: currency, userId: Number(userId) });
  }

  printProduct(price: number, name: string, username: string, currencySymbol: string, column: number, rows: number, pages: number) {
    return this.http.post(`${this.API}/print`,
      { price, name, currencySymbol, column, rows, username, pages },
      { responseType: 'blob' });
  }

  generateProduct(price: number, name: string, username: string, currencySymbol: string): Observable<qrcodeResponse> {
    return this.http.post<qrcodeResponse>(`${this.API}/generate-qrcode`, {
      price,
      name,
      username,
      currencySymbol,
    });
  }
  removeProduct(id: number): Observable<AuthResponsePassword> {
    return this.http.delete<AuthResponsePassword>(`${this.API}/${id}`);
  }

  getOneProductById(id: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.API}/get-product/${id}`);
  }
}
