import { IDetalleVenta, NewDetalleVenta } from './detalle-venta.model';

export const sampleWithRequiredData: IDetalleVenta = {
  id: 24833,
};

export const sampleWithPartialData: IDetalleVenta = {
  id: 76695,
  cantidad: 28354,
  foreignId: 79694,
};

export const sampleWithFullData: IDetalleVenta = {
  id: 20250,
  cantidad: 55718,
  subtotal: 56386,
  foreignId: 93828,
};

export const sampleWithNewData: NewDetalleVenta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
