import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetalleVenta } from '../detalle-venta.model';
import { DetalleVentaService } from '../service/detalle-venta.service';

@Injectable({ providedIn: 'root' })
export class DetalleVentaRoutingResolveService implements Resolve<IDetalleVenta | null> {
  constructor(protected service: DetalleVentaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDetalleVenta | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((detalleVenta: HttpResponse<IDetalleVenta>) => {
          if (detalleVenta.body) {
            return of(detalleVenta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
