import { Routes } from '@angular/router';
import { adminGuard } from '../../core/guards/admin.guard';

export const EVENTS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./events-list/events-list.component').then((m) => m.EventsListComponent),
  },
  {
    path: 'new',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./events-form/events-form.component').then((m) => m.EventsFormComponent),
  },
  {
    path: ':id/edit',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./events-form/events-form.component').then((m) => m.EventsFormComponent),
  },
];
