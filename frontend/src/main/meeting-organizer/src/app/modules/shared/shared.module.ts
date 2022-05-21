import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {SharedRoutingModule} from './shared-routing.module';
import {ReactiveFormsModule} from '@angular/forms';
import {MaterialModule} from '../material.module';
import { ContactUsComponent } from './contact-us/contact-us.component';
import {FlexModule} from '@angular/flex-layout';

@NgModule({
  declarations: [
    ContactUsComponent
  ],
  exports: [
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexModule
  ]
})
export class SharedModule { }
