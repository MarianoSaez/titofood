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
}

export type NewMenu = Omit<IMenu, 'id'> & { id: null };
