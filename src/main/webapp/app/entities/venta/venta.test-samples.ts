import dayjs from 'dayjs/esm';

import { IVenta, NewVenta } from './venta.model';

export const sampleWithRequiredData: IVenta = {
  id: 20448,
};

export const sampleWithPartialData: IVenta = {
  id: 11474,
  precio: 4145,
};

export const sampleWithFullData: IVenta = {
  id: 56225,
  fecha: dayjs('2023-01-14T05:20'),
  precio: 57127,
  codigoSeguimiento: '623e4a44-41be-4453-973c-18ad534a75c9',
};

export const sampleWithNewData: NewVenta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
