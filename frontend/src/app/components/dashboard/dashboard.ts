import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { TranslocoModule } from '@ngneat/transloco';

import { ProductService } from '../../services/product';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { RouterLink } from "@angular/router";
import { AuthService } from '../../services/auth';
import { ProductResponse } from '../../models/product';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { Subject, takeUntil, timer } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [ReactiveFormsModule, TranslocoModule, CommonModule, RouterLink, FormsModule],
  templateUrl: './dashboard.html',
})
export class DashboardComponent implements OnInit {
  private readonly productService = inject(ProductService);
  public sanitizer = inject(DomSanitizer);
  readonly authService = inject(AuthService);
  protected readonly showPopup = signal(false);
  private readonly fb = inject(FormBuilder);
  private destroy$ = new Subject<void>();

  readonly pdfPreviewUrl = signal<SafeResourceUrl | null>(null);
  readonly allProduct = signal<ProductResponse[]>([]);
  readonly popupitem = signal<boolean>(false);
  readonly success = signal(false);
  readonly isPrinting = signal<boolean>(false);
  readonly nameProduct = signal('');
  readonly notification = signal('');
  readonly marque = signal('');
  readonly nameUser = signal('');
  readonly idProduct = signal<number>(0);
  readonly showDimension = signal(false);

  ngOnInit(): void {
    this.productService.getProduct(localStorage.getItem('id') ?? '').subscribe(response => {
      this.allProduct.set([...response]);
    });
    this.nameUser.set(localStorage.getItem('user')!);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  readonly formDimension = this.fb.nonNullable.group({
    column: [1, [Validators.required, Validators.min(1), Validators.max(7)]],
    rows: [1, [Validators.required, Validators.min(1), Validators.max(7)]],
    pages: [1, [Validators.required, Validators.min(1), Validators.max(100)]]
  });

  showPopupDimension(id: string) {
    localStorage.setItem('idProduct', id);
    this.showDimension.set(true);
  }

  filteredProducts = computed(() => {
    const query = this.nameProduct().toLowerCase();

    return this.allProduct().filter(item =>
      item.name.toLowerCase().includes(query)
    );
  });

  public currentPage = signal(1);
  public pageSize = signal(7);

  get start() { return (this.currentPage() - 1) * this.pageSize(); }
  get end() { return this.start + this.pageSize(); }

  changePage(delta: number) { this.currentPage.set(this.currentPage() + delta); }

  imprimerPDF() {
    const id = localStorage.getItem('idProduct');
    this.showPopup.set(true);
    this.productService.getOneProductById(Number(id)).subscribe(response => {
      this.productService
        .printProduct(response.price, response.name, localStorage.getItem('user') ?? '', response.currency, this.formDimension.value.column!, this.formDimension.value.rows!, this.formDimension.value.pages!)
        .subscribe((blob) => {
          this.showDimension.set(false);
          const blobUrl = URL.createObjectURL(blob);
          this.pdfPreviewUrl.set(this.sanitizer.bypassSecurityTrustResourceUrl(blobUrl));
        });
    });
  };

  showItems(id: string) {
    this.popupitem.set(true);
    this.idProduct.set(Number(id));
  }


  deleteDocument() {
    this.productService.removeProduct(this.idProduct()).subscribe((response) => {
      this.notification.set(response.message);
      if (response.success) {
        this.allProduct.update(products => products.filter(p => p.id !== this.idProduct()));
        this.success.set(true);
        this.popupitem.set(false);
        this.marque.set('Succès !');
      }

      if (!response.success) {
        this.success.set(false);
        this.marque.set('Erro save !');
      }

      this.authService.showToast.set(true);
      timer(5000)
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => {
          this.authService.showToast.set(false);
        });
    });
  }

}
