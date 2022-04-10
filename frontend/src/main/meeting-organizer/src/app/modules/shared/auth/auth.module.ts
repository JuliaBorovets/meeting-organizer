import {CommonModule} from '@angular/common';
import {MaterialModule} from '../../material.module';
import {AuthRoutingModule} from './auth-routing.module';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {NgModule} from '@angular/core';
import {FlexModule} from '@angular/flex-layout';
import {ReactiveFormsModule} from '@angular/forms';
import {ForgotPasswordModule} from './forgot-password/forgot-password.module';
import {RestoreRequestComponent} from './restore-request/restore-request.component';
import { AuthPageComponent } from './auth-page/auth-page.component';
import { RegistrationVerifyComponent } from './registration-verify/registration-verify.component';

@NgModule({
  declarations: [RegisterComponent, LoginComponent, RestoreRequestComponent, AuthPageComponent, RegistrationVerifyComponent],
  exports: [],
  imports: [
    CommonModule,
    AuthRoutingModule,
    MaterialModule,
    FlexModule,
    ReactiveFormsModule,
    ForgotPasswordModule
  ]
})
export class AuthModule {
}
