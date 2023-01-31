import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'menu',
        data: { pageTitle: 'franchiseApp.menu.home.title' },
        loadChildren: () => import('./menu/menu.module').then(m => m.MenuModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'franchiseApp.venta.home.title' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'detalle-venta',
        data: { pageTitle: 'franchiseApp.detalleVenta.home.title' },
        loadChildren: () => import('./detalle-venta/detalle-venta.module').then(m => m.DetalleVentaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
