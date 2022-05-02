import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {AuthGuard} from '../../services/auth/auth.guard';
import {ViewComponent} from './view/view.component';
import {ContentComponent} from "./stream/content/content.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'view/:libraryId',
        data: {
          breadcrumb: 'view'
        },
        component: ViewComponent,
        canActivate: [AuthGuard]
      },
      {
        path: ':libraryId/stream/view/:streamId',
        data: {
          breadcrumb: 'view'
        },
        component: ContentComponent,
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
