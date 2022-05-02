import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {SharedRoutingModule} from './shared-routing.module';
import {ReactiveFormsModule} from '@angular/forms';
import {MaterialModule} from '../material.module';
import {MainPageComponent} from './main-page/main-page.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import {FlexModule} from "@angular/flex-layout";

@NgModule({
  declarations: [
    MainPageComponent,
    ContactUsComponent
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
