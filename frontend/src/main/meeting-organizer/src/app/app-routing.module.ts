import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotFoundComponent} from './modules/shared/not-found/not-found.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'main-page',
        pathMatch: 'full'
      },
      {
        path: 'library',
        loadChildren: () => import('src/app/modules/library/library.module').then(m => m.LibraryModule)
      },
      {
        path: 'main-page',
        loadChildren: () => import('src/app/modules/shared/shared.module').then(m => m.SharedModule)
      },
      {
        path: '404',
        component: NotFoundComponent
      },
      // {
      //   path: '**',
      //   redirectTo: '404'
      // }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
