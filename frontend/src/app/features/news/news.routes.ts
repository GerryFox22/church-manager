import { Routes } from '@angular/router';
import { adminGuard } from '../../core/guards/admin.guard';

export const NEWS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./news-list/news-list.component').then((m) => m.NewsListComponent),
  },
  {
    path: 'new',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./news-form/news-form.component').then((m) => m.NewsFormComponent),
  },
  {
    path: ':id/edit',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./news-form/news-form.component').then((m) => m.NewsFormComponent),
  },
];
