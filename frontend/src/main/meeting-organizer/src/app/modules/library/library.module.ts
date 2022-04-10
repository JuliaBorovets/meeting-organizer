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
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {FlexModule} from "@angular/flex-layout";
import {MatButtonModule} from "@angular/material/button";

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
    MatButtonModule
  ],
  entryComponents: [CreateComponent, InfoComponent]
})
export class LibraryModule {
}
