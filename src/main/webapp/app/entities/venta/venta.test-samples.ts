import dayjs from 'dayjs/esm';

import { IVenta, NewVenta } from './venta.model';

export const sampleWithRequiredData: IVenta = {
  id: 20448,
};

export const sampleWithPartialData: IVenta = {
  id: 4145,
  precio: 56225,
};

export const sampleWithFullData: IVenta = {
  id: 71903,
  fecha: dayjs('2023-01-14T08:53'),
  precio: 41728,
  foreignId: 17570,
  codigoSeguimiento: '3e4a4441-be45-4397-bc18-ad534a75c9b5',
};

export const sampleWithNewData: NewVenta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
