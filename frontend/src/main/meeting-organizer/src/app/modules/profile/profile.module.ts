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
import {ProfileRoutingModule} from './profile-routing.module';
import {ViewComponent} from './view/view.component';

@NgModule({
  declarations: [ViewComponent],
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
    ProfileRoutingModule
  ],
  exports: [
    ViewComponent
  ],
  entryComponents: []
})
export class ProfileModule {
}
