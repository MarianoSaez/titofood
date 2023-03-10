import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../detalle-venta.test-samples';

import { DetalleVentaFormService } from './detalle-venta-form.service';

describe('DetalleVenta Form Service', () => {
  let service: DetalleVentaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetalleVentaFormService);
  });

  describe('Service methods', () => {
    describe('createDetalleVentaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDetalleVentaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            subtotal: expect.any(Object),
            foreignId: expect.any(Object),
            venta: expect.any(Object),
            menu: expect.any(Object),
          })
        );
      });

      it('passing IDetalleVenta should create a new form with FormGroup', () => {
        const formGroup = service.createDetalleVentaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            subtotal: expect.any(Object),
            foreignId: expect.any(Object),
            venta: expect.any(Object),
            menu: expect.any(Object),
          })
        );
      });
    });

    describe('getDetalleVenta', () => {
      it('should return NewDetalleVenta for default DetalleVenta initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDetalleVentaFormGroup(sampleWithNewData);

        const detalleVenta = service.getDetalleVenta(formGroup) as any;

        expect(detalleVenta).toMatchObject(sampleWithNewData);
      });

      it('should return NewDetalleVenta for empty DetalleVenta initial value', () => {
        const formGroup = service.createDetalleVentaFormGroup();

        const detalleVenta = service.getDetalleVenta(formGroup) as any;

        expect(detalleVenta).toMatchObject({});
      });

      it('should return IDetalleVenta', () => {
        const formGroup = service.createDetalleVentaFormGroup(sampleWithRequiredData);

        const detalleVenta = service.getDetalleVenta(formGroup) as any;

        expect(detalleVenta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDetalleVenta should not enable id FormControl', () => {
        const formGroup = service.createDetalleVentaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDetalleVenta should disable id FormControl', () => {
        const formGroup = service.createDetalleVentaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
