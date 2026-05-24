import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  {
    path: 'login',
    canActivate: [guestGuard],
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
      },
      {
        path: 'news',
        loadChildren: () => import('./features/news/news.routes').then((m) => m.NEWS_ROUTES),
      },
      {
        path: 'events',
        loadChildren: () => import('./features/events/events.routes').then((m) => m.EVENTS_ROUTES),
      },
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];
