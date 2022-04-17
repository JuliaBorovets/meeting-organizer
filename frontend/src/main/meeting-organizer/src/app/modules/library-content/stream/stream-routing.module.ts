import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {ViewComponent} from './list/view/view.component';
import {AuthGuard} from '../../../services/auth/auth.guard';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'view',
        pathMatch: 'full'
      },
      {
        path: 'view',
        data: {
          breadcrumb: 'view'
        },
        component: ViewComponent,
        canActivate: [AuthGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StreamRoutingModule {

}
