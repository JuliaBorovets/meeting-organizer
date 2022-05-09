import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {FlexModule} from '@angular/flex-layout';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {RouterModule} from '@angular/router';
import {MeetingRoutingModule} from './meeting-routing.module';
import {ListComponent} from './list/list.component';
import {EventModule} from './event/event.module';
import {MatTabsModule} from '@angular/material/tabs';
import {InfoComponent} from './info/info.component';

@NgModule({
  declarations: [ListComponent, InfoComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    FlexModule,
    MatButtonModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    RouterModule,
    MeetingRoutingModule,
    EventModule,
    MatTabsModule
  ],
  entryComponents: []
})
export class MeetingModule {
}
