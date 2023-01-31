import { IDetalleVenta, NewDetalleVenta } from './detalle-venta.model';

export const sampleWithRequiredData: IDetalleVenta = {
  id: 24833,
};

export const sampleWithPartialData: IDetalleVenta = {
  id: 99024,
  cantidad: 76695,
};

export const sampleWithFullData: IDetalleVenta = {
  id: 28354,
  cantidad: 79694,
  subtotal: 20250,
};

export const sampleWithNewData: NewDetalleVenta = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
