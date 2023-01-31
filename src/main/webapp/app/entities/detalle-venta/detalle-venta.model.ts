import { IVenta } from 'app/entities/venta/venta.model';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IDetalleVenta {
  id: number;
  cantidad?: number | null;
  subtotal?: number | null;
  venta?: Pick<IVenta, 'id'> | null;
  menu?: Pick<IMenu, 'id'> | null;
}

export type NewDetalleVenta = Omit<IDetalleVenta, 'id'> & { id: null };
