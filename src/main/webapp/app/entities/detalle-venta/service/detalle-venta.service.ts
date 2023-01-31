import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetalleVenta, NewDetalleVenta } from '../detalle-venta.model';

export type PartialUpdateDetalleVenta = Partial<IDetalleVenta> & Pick<IDetalleVenta, 'id'>;

export type EntityResponseType = HttpResponse<IDetalleVenta>;
export type EntityArrayResponseType = HttpResponse<IDetalleVenta[]>;

@Injectable({ providedIn: 'root' })
export class DetalleVentaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detalle-ventas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(detalleVenta: NewDetalleVenta): Observable<EntityResponseType> {
    return this.http.post<IDetalleVenta>(this.resourceUrl, detalleVenta, { observe: 'response' });
  }

  update(detalleVenta: IDetalleVenta): Observable<EntityResponseType> {
    return this.http.put<IDetalleVenta>(`${this.resourceUrl}/${this.getDetalleVentaIdentifier(detalleVenta)}`, detalleVenta, {
      observe: 'response',
    });
  }

  partialUpdate(detalleVenta: PartialUpdateDetalleVenta): Observable<EntityResponseType> {
    return this.http.patch<IDetalleVenta>(`${this.resourceUrl}/${this.getDetalleVentaIdentifier(detalleVenta)}`, detalleVenta, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetalleVenta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetalleVenta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDetalleVentaIdentifier(detalleVenta: Pick<IDetalleVenta, 'id'>): number {
    return detalleVenta.id;
  }

  compareDetalleVenta(o1: Pick<IDetalleVenta, 'id'> | null, o2: Pick<IDetalleVenta, 'id'> | null): boolean {
    return o1 && o2 ? this.getDetalleVentaIdentifier(o1) === this.getDetalleVentaIdentifier(o2) : o1 === o2;
  }

  addDetalleVentaToCollectionIfMissing<Type extends Pick<IDetalleVenta, 'id'>>(
    detalleVentaCollection: Type[],
    ...detalleVentasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const detalleVentas: Type[] = detalleVentasToCheck.filter(isPresent);
    if (detalleVentas.length > 0) {
      const detalleVentaCollectionIdentifiers = detalleVentaCollection.map(
        detalleVentaItem => this.getDetalleVentaIdentifier(detalleVentaItem)!
      );
      const detalleVentasToAdd = detalleVentas.filter(detalleVentaItem => {
        const detalleVentaIdentifier = this.getDetalleVentaIdentifier(detalleVentaItem);
        if (detalleVentaCollectionIdentifiers.includes(detalleVentaIdentifier)) {
          return false;
        }
        detalleVentaCollectionIdentifiers.push(detalleVentaIdentifier);
        return true;
      });
      return [...detalleVentasToAdd, ...detalleVentaCollection];
    }
    return detalleVentaCollection;
  }
}
