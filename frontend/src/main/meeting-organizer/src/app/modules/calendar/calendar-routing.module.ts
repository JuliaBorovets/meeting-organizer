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
        redirectTo: 'list',
        pathMatch: 'full'
      },
      {
        path: 'list',
        data: {
          breadcrumb: 'list'
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
export class CalendarRoutingModule {

}
