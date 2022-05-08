import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from '../../services/auth/auth.guard';
import {NgModule} from '@angular/core';
import {ViewComponent} from './view/view.component';

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
        canActivate: [AuthGuard],
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule {

}
