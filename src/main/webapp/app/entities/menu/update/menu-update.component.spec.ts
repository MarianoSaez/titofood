import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MenuFormService } from './menu-form.service';
import { MenuService } from '../service/menu.service';
import { IMenu } from '../menu.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { MenuUpdateComponent } from './menu-update.component';

describe('Menu Management Update Component', () => {
  let comp: MenuUpdateComponent;
  let fixture: ComponentFixture<MenuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let menuFormService: MenuFormService;
  let menuService: MenuService;
  let ventaService: VentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MenuUpdateComponent],
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
      .overrideTemplate(MenuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MenuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    menuFormService = TestBed.inject(MenuFormService);
    menuService = TestBed.inject(MenuService);
    ventaService = TestBed.inject(VentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Venta query and add missing value', () => {
      const menu: IMenu = { id: 456 };
      const ventas: IVenta[] = [{ id: 69176 }];
      menu.ventas = ventas;

      const ventaCollection: IVenta[] = [{ id: 84910 }];
      jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [...ventas];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(
        ventaCollection,
        ...additionalVentas.map(expect.objectContaining)
      );
      expect(comp.ventasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const menu: IMenu = { id: 456 };
      const venta: IVenta = { id: 96538 };
      menu.ventas = [venta];

      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      expect(comp.ventasSharedCollection).toContain(venta);
      expect(comp.menu).toEqual(menu);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMenu>>();
      const menu = { id: 123 };
      jest.spyOn(menuFormService, 'getMenu').mockReturnValue(menu);
      jest.spyOn(menuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menu }));
      saveSubject.complete();

      // THEN
      expect(menuFormService.getMenu).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(menuService.update).toHaveBeenCalledWith(expect.objectContaining(menu));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMenu>>();
      const menu = { id: 123 };
      jest.spyOn(menuFormService, 'getMenu').mockReturnValue({ id: null });
      jest.spyOn(menuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menu: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menu }));
      saveSubject.complete();

      // THEN
      expect(menuFormService.getMenu).toHaveBeenCalled();
      expect(menuService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMenu>>();
      const menu = { id: 123 };
      jest.spyOn(menuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(menuService.update).toHaveBeenCalled();
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
  });
});
