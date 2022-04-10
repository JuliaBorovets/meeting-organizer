import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainPageComponent} from './main-page/main-page.component';
import {AuthGuard} from '../../services/auth/auth.guard';

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Welcome Page'
    },
    children: [
      {
        path: '',
        redirectTo: 'main-page',
        pathMatch: 'full'
      },
      {
        path: 'main-page',
        data: {
          breadcrumb: 'Main Page'
        },
        component: MainPageComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'auth',
        data: {
          breadcrumb: null
        },
        loadChildren: () => import('src/app/modules/shared/auth/auth.module').then(m => m.AuthModule)
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SharedRoutingModule { }
