import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMenu, NewMenu } from '../menu.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMenu for edit and NewMenuFormGroupInput for create.
 */
type MenuFormGroupInput = IMenu | PartialWithRequiredKeyOf<NewMenu>;

type MenuFormDefaults = Pick<NewMenu, 'id' | 'isActive' | 'ventas'>;

type MenuFormGroupContent = {
  id: FormControl<IMenu['id'] | NewMenu['id']>;
  nombre: FormControl<IMenu['nombre']>;
  precio: FormControl<IMenu['precio']>;
  descripcion: FormControl<IMenu['descripcion']>;
  urlImagen: FormControl<IMenu['urlImagen']>;
  isActive: FormControl<IMenu['isActive']>;
  foreignId: FormControl<IMenu['foreignId']>;
  creado: FormControl<IMenu['creado']>;
  actualizado: FormControl<IMenu['actualizado']>;
  ventas: FormControl<IMenu['ventas']>;
};

export type MenuFormGroup = FormGroup<MenuFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MenuFormService {
  createMenuFormGroup(menu: MenuFormGroupInput = { id: null }): MenuFormGroup {
    const menuRawValue = {
      ...this.getFormDefaults(),
      ...menu,
    };
    return new FormGroup<MenuFormGroupContent>({
      id: new FormControl(
        { value: menuRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(menuRawValue.nombre),
      precio: new FormControl(menuRawValue.precio),
      descripcion: new FormControl(menuRawValue.descripcion),
      urlImagen: new FormControl(menuRawValue.urlImagen),
      isActive: new FormControl(menuRawValue.isActive),
      foreignId: new FormControl(menuRawValue.foreignId),
      creado: new FormControl(menuRawValue.creado),
      actualizado: new FormControl(menuRawValue.actualizado),
      ventas: new FormControl(menuRawValue.ventas ?? []),
    });
  }

  getMenu(form: MenuFormGroup): IMenu | NewMenu {
    return form.getRawValue() as IMenu | NewMenu;
  }

  resetForm(form: MenuFormGroup, menu: MenuFormGroupInput): void {
    const menuRawValue = { ...this.getFormDefaults(), ...menu };
    form.reset(
      {
        ...menuRawValue,
        id: { value: menuRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MenuFormDefaults {
    return {
      id: null,
      isActive: false,
      ventas: [],
    };
  }
}
