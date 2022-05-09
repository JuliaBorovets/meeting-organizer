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
import {ViewComponent} from './list/view/view.component';
import {CreateStreamComponent} from './create/create.component';
import {StreamRoutingModule} from './stream-routing.module';
import { StreamListComponent } from './list/stream-list.component';
import {UpdateStreamComponent} from './update/update-stream.component';
import { AddEventComponent } from './add-event/add-event.component';
import { ContentComponent } from './content/content.component';
import {EventModule} from '../../meeting/event/event.module';
import {MatChipsModule} from '@angular/material/chips';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatOptionModule} from '@angular/material/core';

@NgModule({
  declarations: [ViewComponent, CreateStreamComponent, UpdateStreamComponent, StreamListComponent, AddEventComponent, ContentComponent],
  imports: [
    MatTabsModule,
    MatProgressSpinnerModule,
    CommonModule,
    MatPaginatorModule,
    StreamRoutingModule,
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
    EventModule,
    MatChipsModule,
    MatAutocompleteModule,
    MatOptionModule
  ],
  exports: [
    CreateStreamComponent,
    ViewComponent,
    StreamListComponent,
    UpdateStreamComponent
  ],
  entryComponents: []
})
export class StreamModule {
}
