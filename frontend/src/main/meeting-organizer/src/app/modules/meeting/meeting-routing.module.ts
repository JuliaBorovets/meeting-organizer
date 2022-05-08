import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from '../../services/auth/auth.guard';
import {NgModule} from '@angular/core';
import {ListComponent} from './list/list.component';

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
        component: ListComponent,
        canActivate: [AuthGuard],
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MeetingRoutingModule {

}
