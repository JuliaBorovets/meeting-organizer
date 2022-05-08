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
import {MatTreeModule} from '@angular/material/tree';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {ListComponent} from './list/list.component';
import {FavoritesRoutingModule} from './favorites-routing.module';
import { LibraryComponent } from './list/library/library.component';
import { EventComponent } from './list/event/event.component';
import {LibraryFavoriteViewComponent} from './list/library/view/view.component';
import {EventViewComponent} from './list/event/view/view.component';

@NgModule({
  declarations: [ListComponent, LibraryComponent, EventComponent, LibraryFavoriteViewComponent, EventViewComponent],
  imports: [
    MatTabsModule,
    MatProgressSpinnerModule,
    CommonModule,
    MatPaginatorModule,
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
    MatProgressBarModule,
    FavoritesRoutingModule
  ],
  entryComponents: []
})
export class FavoritesModule {
}
