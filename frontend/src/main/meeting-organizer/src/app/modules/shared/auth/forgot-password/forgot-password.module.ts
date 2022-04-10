import {CreateNewPasswordComponent} from './create-new-password/create-new-password.component';
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MaterialModule} from '../../../material.module';
import {ForgotPasswordRoutingModule} from './forgot-password-routing.module';
import {WelcomePageComponent} from '../../shared-components/welcome-page/welcome-page.component';
import {FlexModule} from '@angular/flex-layout';
import { CreateComponent } from './create/create.component';
import {ReactiveFormsModule} from "@angular/forms";

@NgModule({
  declarations: [
    CreateNewPasswordComponent,
    WelcomePageComponent,
    CreateComponent
  ],
  exports: [
    WelcomePageComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ForgotPasswordRoutingModule,
    FlexModule,
    ReactiveFormsModule
  ]
})
export class ForgotPasswordModule {
}
