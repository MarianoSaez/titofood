import dayjs from 'dayjs/esm';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IVenta {
  id: number;
  fecha?: dayjs.Dayjs | null;
  precio?: number | null;
  foreignId?: number | null;
  menus?: Pick<IMenu, 'id'>[] | null;
}

export type NewVenta = Omit<IVenta, 'id'> & { id: null };
