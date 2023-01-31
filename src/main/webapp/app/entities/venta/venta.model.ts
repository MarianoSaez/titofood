import dayjs from 'dayjs/esm';

export interface IVenta {
  id: number;
  fecha?: dayjs.Dayjs | null;
  precio?: number | null;
  foreignId?: number | null;
}

export type NewVenta = Omit<IVenta, 'id'> & { id: null };
