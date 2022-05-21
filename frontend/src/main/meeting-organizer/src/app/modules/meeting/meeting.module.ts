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
import { ReactionComponent } from './reaction/reaction.component';
import { CommentComponent } from './comment/comment.component';
import { RatingComponent } from './reaction/rating/rating.component';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import { VisitorComponent } from './visitor/visitor.component';
import { AddEventAccessCodeComponent } from './add-access-code/add-access-code.component';

@NgModule({
  declarations: [ListComponent, InfoComponent, ReactionComponent, CommentComponent, RatingComponent, VisitorComponent, AddEventAccessCodeComponent],
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
    MatTabsModule,
    MatPaginatorModule,
    MatProgressSpinnerModule
  ],
  entryComponents: []
})
export class MeetingModule {
}
