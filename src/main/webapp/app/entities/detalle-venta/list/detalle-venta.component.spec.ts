import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DetalleVentaService } from '../service/detalle-venta.service';

import { DetalleVentaComponent } from './detalle-venta.component';

describe('DetalleVenta Management Component', () => {
  let comp: DetalleVentaComponent;
  let fixture: ComponentFixture<DetalleVentaComponent>;
  let service: DetalleVentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'detalle-venta', component: DetalleVentaComponent }]), HttpClientTestingModule],
      declarations: [DetalleVentaComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(DetalleVentaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleVentaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DetalleVentaService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.detalleVentas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to detalleVentaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDetalleVentaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDetalleVentaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
