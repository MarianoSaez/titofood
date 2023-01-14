import { IMenu, NewMenu } from './menu.model';

export const sampleWithRequiredData: IMenu = {
  id: 56082,
};

export const sampleWithPartialData: IMenu = {
  id: 546,
  nombre: 'innovate',
  precio: 10624,
  isActive: false,
};

export const sampleWithFullData: IMenu = {
  id: 39647,
  nombre: 'THX Macao',
  precio: 53902,
  descripcion: 'calculate',
  urlImagen: 'Hormigon',
  isActive: true,
  foreignId: 24075,
};

export const sampleWithNewData: NewMenu = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
