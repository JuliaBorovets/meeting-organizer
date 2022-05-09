import {NgModule} from '@angular/core';
import {MatTabsModule} from '@angular/material/tabs';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {CommonModule} from '@angular/common';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {FlexModule} from '@angular/flex-layout';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatInputModule} from '@angular/material/input';
import {MatMenuModule} from '@angular/material/menu';
import {RouterModule} from '@angular/router';
import {ViewComponent} from './list/view/view.component';
import {EventRoutingModule} from './event-routing.module';
import {CreateEventComponent} from './create/create.component';
import {UpdateEventComponent} from './update/update-event.component';
import { EventListComponent } from './list/event-list.component';
import {MatNativeDateModule} from '@angular/material/core';
import {MatDividerModule} from '@angular/material/divider';
import {MatListModule} from '@angular/material/list';
import {MatSelectModule} from '@angular/material/select';
import {NgxMatDatetimePickerModule} from '@angular-material-components/datetime-picker';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MaterialModule} from '../../material.module';

@NgModule({
  declarations: [ViewComponent, CreateEventComponent, UpdateEventComponent, EventListComponent],
  imports: [
    MatTabsModule,
    MatProgressSpinnerModule,
    CommonModule,
    MatPaginatorModule,
    EventRoutingModule,
    MatCardModule,
    MatIconModule,
    FlexModule,
    MatButtonModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    MatInputModule,
    MatMenuModule,
    RouterModule,
    MatNativeDateModule,
    MatDividerModule,
    MatListModule,
    MatSelectModule,
    FormsModule,
    NgxMatDatetimePickerModule,
    MatDatepickerModule,
    MaterialModule
  ],
  exports: [
    ViewComponent,
    EventListComponent
  ],
  entryComponents: []
})
export class EventModule {
}
