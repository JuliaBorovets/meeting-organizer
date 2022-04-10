import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {CreateNewPasswordComponent} from './create-new-password/create-new-password.component';

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Forgot password'
    },
    children: [
      {
        path: '',
        redirectTo: 'create-new',
        pathMatch: 'full'
      },
      {
        path: 'create-new',
        data: {
          breadcrumb: 'Create new'
        },
        component: CreateNewPasswordComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ForgotPasswordRoutingModule {
}
