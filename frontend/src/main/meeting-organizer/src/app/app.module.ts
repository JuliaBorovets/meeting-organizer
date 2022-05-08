import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {SharedModule} from './modules/shared/shared.module';
import {AuthInterceptor} from './services/auth/auth.interceptor';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {MaterialModule} from './modules/material.module';
import {MatSidenavModule} from '@angular/material/sidenav';
import {NbCardModule, NbLayoutModule, NbSidebarModule, NbThemeModule} from '@nebular/theme';
import {MatDividerModule} from '@angular/material/divider';
import {MatListModule} from '@angular/material/list';
import {LibraryModule} from './modules/library/library.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import {AuthModule} from './modules/shared/auth/auth.module';
import {NotFoundComponent} from './modules/shared/not-found/not-found.component';
import {ToastrModule} from 'ngx-toastr';
import {RouterModule} from '@angular/router';
import { ConfirmComponent } from './components/confirm/confirm.component';
import {
  NgxMatDatetimePickerModule,
  NgxMatNativeDateModule,
  NgxMatTimepickerModule
} from '@angular-material-components/datetime-picker';
import {FavoritesModule} from './modules/favorites/favorites.module';

@NgModule({
  declarations: [
    AppComponent,
    NotFoundComponent,
    ConfirmComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    MaterialModule,
    HttpClientModule,
    MatSidenavModule,
    NbCardModule,
    NbLayoutModule,
    NbSidebarModule,
    NbThemeModule,
    MatDividerModule,
    MatListModule,
    LibraryModule,
    FlexLayoutModule,
    AuthModule,
    NgxMatDatetimePickerModule,
    NgxMatTimepickerModule,
    NgxMatNativeDateModule,
    FavoritesModule,

    ToastrModule.forRoot({
      timeOut: 150000, // 15 seconds
      closeButton: true,
      progressBar: true,
    }),
    RouterModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    //  { provide: NGX_MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS }
  ],
  exports: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
