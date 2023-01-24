import { IVenta } from 'app/entities/venta/venta.model';

export interface IMenu {
  id: number;
  nombre?: string | null;
  precio?: number | null;
  descripcion?: string | null;
  urlImagen?: string | null;
  isActive?: boolean | null;
  foreignId?: number | null;
  creado?: string | null;
  actualizado?: string | null;
  ventas?: Pick<IVenta, 'id'>[] | null;
}

export type NewMenu = Omit<IMenu, 'id'> & { id: null };
