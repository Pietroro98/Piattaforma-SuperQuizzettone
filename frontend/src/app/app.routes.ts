import { Routes } from '@angular/router';
import { LoginComponent } from './authentication/login/login.component';
import { AuthComponent } from './authentication/auth/auth.component';
import { RegisterComponent } from './authentication/register/register.component';
import { HomepageComponent } from './features/homepage/homepage.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomepageComponent,
  },
  {
    path: '',
    component: AuthComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      { path: '', redirectTo: 'login', pathMatch: 'full' }
    ]
  },
  {
    path: '**',
    redirectTo: '/home',
    pathMatch: 'full'
  },
];
