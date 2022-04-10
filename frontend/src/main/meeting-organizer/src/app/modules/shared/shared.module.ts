import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {SharedRoutingModule} from './shared-routing.module';
import {ReactiveFormsModule} from '@angular/forms';
import {MaterialModule} from '../material.module';
import {MainPageComponent} from './main-page/main-page.component';

@NgModule({
  declarations: [
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
