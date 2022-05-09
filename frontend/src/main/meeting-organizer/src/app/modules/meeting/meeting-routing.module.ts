import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from '../../services/auth/auth.guard';
import {NgModule} from '@angular/core';
import {ListComponent} from './list/list.component';
import {InfoComponent} from './info/info.component';

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
      },
      {
        path: 'info/:id',
        data: {
          breadcrumb: 'info'
        },
        component: InfoComponent,
        canActivate: [AuthGuard]
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
