import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {RegistrationComponent} from './registration/registration.component';
import {AuthGuard} from '../../services/auth/auth.guard';
import {MainPageComponent} from './main-page/main-page.component';

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Welcome Page'
    },
    children: [
      {
        path: '',
        redirectTo: 'welcome-page',
        pathMatch: 'full'
      },
      {
        path: 'welcome-page',
        data: {
          breadcrumb: 'Welcome Page'
        },
        component: MainPageComponent
      }

      // {
      //   path: '',
      //   redirectTo: 'login',
      //   pathMatch: 'full'
      // },
      // {
      //   path: 'login',
      //   data: {
      //     breadcrumb: 'Login'
      //   },
      //   component: LoginComponent
      // },
      // {
      //   path: 'registration',
      //   data: {
      //     breadcrumb: 'Registration'
      //   },
      //   component: RegistrationComponent
      // }
      // {
      //   path: 'users',
      //   data: {
      //     roles: ['ADMIN'],
      //     breadcrumb: null
      //   },
      //   loadChildren: 'src/app/modules/shared/users/users.module#UsersModule',
      //   canActivate: [AuthGuard]
      // },
      // {
      //   path: 'forgot-password',
      //   data: {
      //     breadcrumb: null
      //   },
      //   loadChildren: 'src/app/modules/shared/forgot-password/forgot-password.module#ForgotPasswordModule'
      // }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SharedRoutingModule { }
