import { IMenu, NewMenu } from './menu.model';

export const sampleWithRequiredData: IMenu = {
  id: 56082,
};

export const sampleWithPartialData: IMenu = {
  id: 39110,
  nombre: 'Barrio syndicate payment',
  precio: 54866,
  isActive: true,
};

export const sampleWithFullData: IMenu = {
  id: 53902,
  nombre: 'calculate',
  precio: 27631,
  descripcion: 'Gerente',
  urlImagen: 'Futuro Ladrillo',
  isActive: false,
  foreignId: 86630,
  creado: 'algoritmo bidireccional Won',
  actualizado: 'conjunto Directo fritas',
};

export const sampleWithNewData: NewMenu = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
