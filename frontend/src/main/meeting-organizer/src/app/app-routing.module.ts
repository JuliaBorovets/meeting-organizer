import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotFoundComponent} from './modules/shared/not-found/not-found.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'calendar',
        pathMatch: 'full'
      },
      {
        path: 'profile',
        loadChildren: () => import('src/app/modules/profile/profile.module').then(m => m.ProfileModule)
      },
      {
        path: 'favorite',
        loadChildren: () => import('src/app/modules/favorite/favorites.module').then(m => m.FavoritesModule)
      },
      {
        path: 'calendar',
        loadChildren: () => import('src/app/modules/calendar/calendar.module').then(m => m.CalendarModule)
      },
      {
        path: 'library',
        loadChildren: () => import('src/app/modules/library/library.module').then(m => m.LibraryModule)
      },
      {
        path: 'library-content',
        loadChildren: () => import('src/app/modules/library-content/content.module').then(m => m.LibraryContentModule)
      },
      {
        path: 'meeting',
        loadChildren: () => import('src/app/modules/meeting/meeting.module').then(m => m.MeetingModule)
      },
      {
        path: '404',
        component: NotFoundComponent
      },
      // {
      //   path: '**',
      //   redirectTo: '404'
      // }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
