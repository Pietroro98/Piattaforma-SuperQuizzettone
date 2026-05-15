import { Routes } from '@angular/router';
import { HomepageComponent } from './features/homepage/homepage.component';
import { QuestionCreatorComponent } from './features/question-creator/question-creator.component';
import { AuthComponent } from './features/authentication/auth/auth.component';
import { LoginComponent } from './features/authentication/login/login.component';
import { RegisterComponent } from './features/authentication/register/register.component';
import { QuestionListComponent } from './features/question-list/question-list.component';
import { authGuard } from './core/guards/auth-guard';
import { BackofficeComponent } from './features/backoffice/backoffice';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: HomepageComponent,
    canActivate: [authGuard],
  },
  {
    path: 'question-list',
    component: QuestionListComponent,
    canActivate: [authGuard],
  },
  {
    path: '',
    component: AuthComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      { path: '', redirectTo: 'login', pathMatch: 'full' },
    ],
  },
  {
    path: 'backoffice',
    component: BackofficeComponent,
    canActivate: [authGuard],
  },
  {
    path: 'question-list',
    component: QuestionListComponent,
    canActivate: [authGuard],
  },
  {
    path: 'question-creation',
    component: QuestionCreatorComponent,
    canActivate: [authGuard],
  },
  {
    path: '**',
    redirectTo: '/home',
    pathMatch: 'full',
  },
];
