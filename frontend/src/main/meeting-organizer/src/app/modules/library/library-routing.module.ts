import {RouterModule, Routes} from '@angular/router';
import {ListComponent} from './list/list.component';
import {NgModule} from '@angular/core';
import {AuthGuard} from "../../services/auth/auth.guard";

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
          breadcrumb: 'List'
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
export class LibraryRoutingModule {

}
