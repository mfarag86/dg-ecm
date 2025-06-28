import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent) },
  { 
    path: '', 
    loadComponent: () => import('./layout/layout.component').then(m => m.LayoutComponent),
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'cases', loadComponent: () => import('./cases/case-list/case-list.component').then(m => m.CaseListComponent) },
      { path: 'cases/new', loadComponent: () => import('./cases/case-form/case-form.component').then(m => m.CaseFormComponent) },
      { path: 'cases/:id', loadComponent: () => import('./cases/case-detail/case-detail.component').then(m => m.CaseDetailComponent) },
      { path: 'cases/:id/edit', loadComponent: () => import('./cases/case-form/case-form.component').then(m => m.CaseFormComponent) },
      { path: 'users', loadComponent: () => import('./users/user-list/user-list.component').then(m => m.UserListComponent) },
      { path: 'profile', loadComponent: () => import('./profile/profile.component').then(m => m.ProfileComponent) }
    ]
  },
  { path: '**', redirectTo: '/dashboard' }
];
