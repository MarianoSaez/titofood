import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MenuFormService, MenuFormGroup } from './menu-form.service';
import { IMenu } from '../menu.model';
import { MenuService } from '../service/menu.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

@Component({
  selector: 'jhi-menu-update',
  templateUrl: './menu-update.component.html',
})
export class MenuUpdateComponent implements OnInit {
  isSaving = false;
  menu: IMenu | null = null;

  ventasSharedCollection: IVenta[] = [];

  editForm: MenuFormGroup = this.menuFormService.createMenuFormGroup();

  constructor(
    protected menuService: MenuService,
    protected menuFormService: MenuFormService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVenta = (o1: IVenta | null, o2: IVenta | null): boolean => this.ventaService.compareVenta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menu }) => {
      this.menu = menu;
      if (menu) {
        this.updateForm(menu);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const menu = this.menuFormService.getMenu(this.editForm);
    if (menu.id !== null) {
      this.subscribeToSaveResponse(this.menuService.update(menu));
    } else {
      this.subscribeToSaveResponse(this.menuService.create(menu));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenu>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(menu: IMenu): void {
    this.menu = menu;
    this.menuFormService.resetForm(this.editForm, menu);

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing<IVenta>(
      this.ventasSharedCollection,
      ...(menu.ventas ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, ...(this.menu?.ventas ?? []))))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }
}
