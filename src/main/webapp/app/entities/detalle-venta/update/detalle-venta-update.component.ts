import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DetalleVentaFormService, DetalleVentaFormGroup } from './detalle-venta-form.service';
import { IDetalleVenta } from '../detalle-venta.model';
import { DetalleVentaService } from '../service/detalle-venta.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-detalle-venta-update',
  templateUrl: './detalle-venta-update.component.html',
})
export class DetalleVentaUpdateComponent implements OnInit {
  isSaving = false;
  detalleVenta: IDetalleVenta | null = null;

  ventasSharedCollection: IVenta[] = [];
  menusSharedCollection: IMenu[] = [];

  editForm: DetalleVentaFormGroup = this.detalleVentaFormService.createDetalleVentaFormGroup();

  constructor(
    protected detalleVentaService: DetalleVentaService,
    protected detalleVentaFormService: DetalleVentaFormService,
    protected ventaService: VentaService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVenta = (o1: IVenta | null, o2: IVenta | null): boolean => this.ventaService.compareVenta(o1, o2);

  compareMenu = (o1: IMenu | null, o2: IMenu | null): boolean => this.menuService.compareMenu(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleVenta }) => {
      this.detalleVenta = detalleVenta;
      if (detalleVenta) {
        this.updateForm(detalleVenta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalleVenta = this.detalleVentaFormService.getDetalleVenta(this.editForm);
    if (detalleVenta.id !== null) {
      this.subscribeToSaveResponse(this.detalleVentaService.update(detalleVenta));
    } else {
      this.subscribeToSaveResponse(this.detalleVentaService.create(detalleVenta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalleVenta>>): void {
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

  protected updateForm(detalleVenta: IDetalleVenta): void {
    this.detalleVenta = detalleVenta;
    this.detalleVentaFormService.resetForm(this.editForm, detalleVenta);

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing<IVenta>(this.ventasSharedCollection, detalleVenta.venta);
    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing<IMenu>(this.menusSharedCollection, detalleVenta.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing<IVenta>(ventas, this.detalleVenta?.venta)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));

    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing<IMenu>(menus, this.detalleVenta?.menu)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }
}
