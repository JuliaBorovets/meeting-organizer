import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {SharedRoutingModule} from './shared-routing.module';
import {LoginComponent} from './login/login.component';
import {RegistrationComponent} from './registration/registration.component';
import {ReactiveFormsModule} from '@angular/forms';
import {MaterialModule} from '../../services/material.module';
import {MainPageComponent} from './main-page/main-page.component';


@NgModule({
  declarations: [
    LoginComponent,
    RegistrationComponent,
    MainPageComponent
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    ReactiveFormsModule,
    MaterialModule
  ]
})
export class SharedModule { }
