import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDetalleVenta, NewDetalleVenta } from '../detalle-venta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDetalleVenta for edit and NewDetalleVentaFormGroupInput for create.
 */
type DetalleVentaFormGroupInput = IDetalleVenta | PartialWithRequiredKeyOf<NewDetalleVenta>;

type DetalleVentaFormDefaults = Pick<NewDetalleVenta, 'id'>;

type DetalleVentaFormGroupContent = {
  id: FormControl<IDetalleVenta['id'] | NewDetalleVenta['id']>;
  cantidad: FormControl<IDetalleVenta['cantidad']>;
  subtotal: FormControl<IDetalleVenta['subtotal']>;
  venta: FormControl<IDetalleVenta['venta']>;
  menu: FormControl<IDetalleVenta['menu']>;
};

export type DetalleVentaFormGroup = FormGroup<DetalleVentaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DetalleVentaFormService {
  createDetalleVentaFormGroup(detalleVenta: DetalleVentaFormGroupInput = { id: null }): DetalleVentaFormGroup {
    const detalleVentaRawValue = {
      ...this.getFormDefaults(),
      ...detalleVenta,
    };
    return new FormGroup<DetalleVentaFormGroupContent>({
      id: new FormControl(
        { value: detalleVentaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cantidad: new FormControl(detalleVentaRawValue.cantidad),
      subtotal: new FormControl(detalleVentaRawValue.subtotal),
      venta: new FormControl(detalleVentaRawValue.venta),
      menu: new FormControl(detalleVentaRawValue.menu),
    });
  }

  getDetalleVenta(form: DetalleVentaFormGroup): IDetalleVenta | NewDetalleVenta {
    return form.getRawValue() as IDetalleVenta | NewDetalleVenta;
  }

  resetForm(form: DetalleVentaFormGroup, detalleVenta: DetalleVentaFormGroupInput): void {
    const detalleVentaRawValue = { ...this.getFormDefaults(), ...detalleVenta };
    form.reset(
      {
        ...detalleVentaRawValue,
        id: { value: detalleVentaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DetalleVentaFormDefaults {
    return {
      id: null,
    };
  }
}
