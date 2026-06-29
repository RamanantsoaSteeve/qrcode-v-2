import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslocoModule } from '@ngneat/transloco';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

import { ProductService } from '../../services/product';
import { RouterLink } from "@angular/router";
import { AuthService } from '../../services/auth';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-qrcode',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, TranslocoModule, RouterLink],
  templateUrl: './qrcode.html',
  styleUrl: './qrcode.css',
})
export class Qrcode {
  public productService = inject(ProductService);
  public authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private sanitizer = inject(DomSanitizer);

  protected readonly qrcode = signal('');
  public readonly id = signal(0);
  protected readonly showPopup = signal(false);
  public readonly pdfPreviewUrl = signal<SafeResourceUrl | null>(null);
  protected readonly currency = signal('MGA');
  public isSave = signal(false);
  private destroy$ = new Subject<void>();


  readonly formProduct = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    price: [0, [Validators.required, Validators.min(1)]]
  });

  readonly formDimension = this.fb.nonNullable.group({
    column: [1, [Validators.required, Validators.min(1), Validators.max(7)]],
    rows: [1, [Validators.required, Validators.min(1), Validators.max(7)]],
    pages: [1, [Validators.required, Validators.min(1), Validators.max(100)]]
  });


  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  generate() {
    const productRaw = this.formProduct.getRawValue();
    this.productService.generateProduct(productRaw.price!, productRaw.name!, localStorage.getItem('user') ?? '', this.currency()).subscribe({
      next: (response) => {
        this.qrcode.set(response.qrcode);
        console.log(this.qrcode());
      }
    });
  }

  printProduct() {
    this.isSave.set(false);
    this.showPopup.set(true);
    const productRaw = this.formProduct.getRawValue();
    const dimensionRaw = this.formDimension.getRawValue();

    this.productService
      .printProduct(productRaw.price!, productRaw.name!, localStorage.getItem('user') ?? '', this.currency(), dimensionRaw.column!, dimensionRaw.rows!, dimensionRaw.pages!)
      .subscribe((blob) => {
        const blobUrl = URL.createObjectURL(blob);
        this.pdfPreviewUrl.set(this.sanitizer.bypassSecurityTrustResourceUrl(blobUrl));
      });
  }

  onSave() {
    const userId = localStorage.getItem('id');
    if (userId) {
      this.productService.createProduct(this.formProduct.value.price!, this.formProduct.value.name!, this.currency(), userId).subscribe({
        next: (response) => {
          this.isSave.set(true);
          this.generate();
        },
        error: ({ error }) => {
          alert(`⚠️ Vous avez déjà un produit nommé "${this.formProduct.getRawValue().name}". Veuillez utiliser un nom différent.`);
        }
      });
    }
  }
}
