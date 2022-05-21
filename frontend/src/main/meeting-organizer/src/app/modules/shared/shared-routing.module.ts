import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    data: {
    },
    children: [
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
