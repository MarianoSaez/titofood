import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DetalleVentaFormService } from './detalle-venta-form.service';
import { DetalleVentaService } from '../service/detalle-venta.service';
import { IDetalleVenta } from '../detalle-venta.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

import { DetalleVentaUpdateComponent } from './detalle-venta-update.component';

describe('DetalleVenta Management Update Component', () => {
  let comp: DetalleVentaUpdateComponent;
  let fixture: ComponentFixture<DetalleVentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detalleVentaFormService: DetalleVentaFormService;
  let detalleVentaService: DetalleVentaService;
  let ventaService: VentaService;
  let menuService: MenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DetalleVentaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DetalleVentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleVentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleVentaFormService = TestBed.inject(DetalleVentaFormService);
    detalleVentaService = TestBed.inject(DetalleVentaService);
    ventaService = TestBed.inject(VentaService);
    menuService = TestBed.inject(MenuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Venta query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const venta: IVenta = { id: 78957 };
      detalleVenta.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 12675 }];
      jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(
        ventaCollection,
        ...additionalVentas.map(expect.objectContaining)
      );
      expect(comp.ventasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Menu query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const menu: IMenu = { id: 84013 };
      detalleVenta.menu = menu;

      const menuCollection: IMenu[] = [{ id: 97509 }];
      jest.spyOn(menuService, 'query').mockReturnValue(of(new HttpResponse({ body: menuCollection })));
      const additionalMenus = [menu];
      const expectedCollection: IMenu[] = [...additionalMenus, ...menuCollection];
      jest.spyOn(menuService, 'addMenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(menuService.query).toHaveBeenCalled();
      expect(menuService.addMenuToCollectionIfMissing).toHaveBeenCalledWith(
        menuCollection,
        ...additionalMenus.map(expect.objectContaining)
      );
      expect(comp.menusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const venta: IVenta = { id: 84589 };
      detalleVenta.venta = venta;
      const menu: IMenu = { id: 83517 };
      detalleVenta.menu = menu;

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(comp.ventasSharedCollection).toContain(venta);
      expect(comp.menusSharedCollection).toContain(menu);
      expect(comp.detalleVenta).toEqual(detalleVenta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleVenta>>();
      const detalleVenta = { id: 123 };
      jest.spyOn(detalleVentaFormService, 'getDetalleVenta').mockReturnValue(detalleVenta);
      jest.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleVenta }));
      saveSubject.complete();

      // THEN
      expect(detalleVentaFormService.getDetalleVenta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleVentaService.update).toHaveBeenCalledWith(expect.objectContaining(detalleVenta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleVenta>>();
      const detalleVenta = { id: 123 };
      jest.spyOn(detalleVentaFormService, 'getDetalleVenta').mockReturnValue({ id: null });
      jest.spyOn(detalleVentaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleVenta }));
      saveSubject.complete();

      // THEN
      expect(detalleVentaFormService.getDetalleVenta).toHaveBeenCalled();
      expect(detalleVentaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetalleVenta>>();
      const detalleVenta = { id: 123 };
      jest.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleVentaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVenta', () => {
      it('Should forward to ventaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ventaService, 'compareVenta');
        comp.compareVenta(entity, entity2);
        expect(ventaService.compareVenta).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMenu', () => {
      it('Should forward to menuService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(menuService, 'compareMenu');
        comp.compareMenu(entity, entity2);
        expect(menuService.compareMenu).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
