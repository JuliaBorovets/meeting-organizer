import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from './services/auth/auth.guard';

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
        path: 'main-page',
        loadChildren: 'src/app/modules/shared/shared.module#SharedModule',
      },
      // {
      //   path: 'projects-menu',
      //   loadChildren: 'src/app/modules/projects-menu/projects-menu.module#ProjectsMenuModule',
      //   canActivate: [AuthGuard],
      //   data: {
      //     roles: ['ADMIN', 'MANAGER', 'ENGINEER']
      //   }
      // },
      {
        path: '**',
        redirectTo: '404'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
