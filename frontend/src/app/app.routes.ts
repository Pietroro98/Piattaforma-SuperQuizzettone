import { Routes } from '@angular/router';
import { HomepageComponent } from './features/homepage/homepage.component';
import { QuestionCreatorComponent } from './features/question-creator/question-creator.component';
import { AuthComponent } from './features/authentication/auth/auth.component';
import { LoginComponent } from './features/authentication/login/login.component';
import { RegisterComponent } from './features/authentication/register/register.component';
import {  QuestionListComponent } from './features/question-list/question-list.component';

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
    path: 'question-list',
    component: QuestionListComponent
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
    path: 'question-list',
    component: QuestionListComponent, //CAMBIARE COMPONENTE CON QUELLO DELLA LISTA DI DOMANDE
  }, 
   {
    path: 'question-creation',
    component: QuestionCreatorComponent,
  },
  {
    path: '**',
    redirectTo: '/home',
    pathMatch: 'full'
  }
];
