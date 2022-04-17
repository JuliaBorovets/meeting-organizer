import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {AuthGuard} from '../../services/auth/auth.guard';
import {ViewComponent} from './view/view.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'view/:id',
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
export class LibraryContentRoutingModule {

}
