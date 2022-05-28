import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {FlexModule} from '@angular/flex-layout';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {RouterModule} from '@angular/router';
import {ProfileRoutingModule} from './profile-routing.module';
import {ViewComponent} from './view/view.component';
import { UploadUserPhotoComponent } from './upload-photo/upload-photo.component';
import {MatToolbarModule} from '@angular/material/toolbar';

@NgModule({
  declarations: [ViewComponent, UploadUserPhotoComponent],
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
    ProfileRoutingModule,
    FormsModule,
    MatToolbarModule
  ],
  exports: [
    ViewComponent
  ],
  entryComponents: []
})
export class ProfileModule {
}
