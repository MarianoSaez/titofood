import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDetalleVenta } from '../detalle-venta.model';
import { DetalleVentaService } from '../service/detalle-venta.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './detalle-venta-delete-dialog.component.html',
})
export class DetalleVentaDeleteDialogComponent {
  detalleVenta?: IDetalleVenta;

  constructor(protected detalleVentaService: DetalleVentaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detalleVentaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
