import {NgModule} from '@angular/core';
import { ListComponent } from './list/list.component';
import { CreateComponent } from './create/create.component';
import { UpdateComponent } from './update/update.component';
import { ViewComponent } from './list/view/view.component';
import { SearchComponent } from './list/search/search.component';
import {MatTabsModule} from '@angular/material/tabs';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {CommonModule} from '@angular/common';
import {MatPaginatorModule} from '@angular/material/paginator';
import {LibraryRoutingModule} from './library-routing.module';
import { InfoComponent } from './info/info.component';
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
import {MatTreeModule} from '@angular/material/tree';
import {MatProgressBarModule} from '@angular/material/progress-bar';

@NgModule({
  declarations: [ListComponent, CreateComponent, UpdateComponent, ViewComponent, SearchComponent, InfoComponent],
  imports: [
    MatTabsModule,
    MatProgressSpinnerModule,
    CommonModule,
    MatPaginatorModule,
    LibraryRoutingModule,
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
    MatTreeModule,
    MatProgressBarModule
  ],
  entryComponents: [CreateComponent, InfoComponent]
})
export class LibraryModule {
}
