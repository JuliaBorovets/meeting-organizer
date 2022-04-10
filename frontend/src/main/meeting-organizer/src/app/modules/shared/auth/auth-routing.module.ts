import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {AuthPageComponent} from './auth-page/auth-page.component';
import {RegistrationVerifyComponent} from "./registration-verify/registration-verify.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'welcome',
        pathMatch: 'full'
      },
      {
        path: 'welcome',
        component: AuthPageComponent
      },
      {
        path: 'verify',
        component: RegistrationVerifyComponent
      },
      {
        path: 'forgot-password',
        data: {
          breadcrumb: null
        },
        loadChildren: () => import('src/app/modules/shared/auth/forgot-password/forgot-password.module').then(m => m.ForgotPasswordModule)
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {

}
