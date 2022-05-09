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
import {ReactiveFormsModule} from '@angular/forms';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatInputModule} from '@angular/material/input';
import {MatMenuModule} from '@angular/material/menu';
import {RouterModule} from '@angular/router';
import {LibraryContentRoutingModule} from './content-routing.module';
import {ViewComponent} from './view/view.component';
import {StreamModule} from './stream/stream.module';
import {EventModule} from '../meeting/event/event.module';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';

@NgModule({
  declarations: [ViewComponent],
  imports: [
    MatTabsModule,
    MatProgressSpinnerModule,
    CommonModule,
    MatPaginatorModule,
    LibraryContentRoutingModule,
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
    StreamModule,
    EventModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  entryComponents: []
})
export class LibraryContentModule {
}
