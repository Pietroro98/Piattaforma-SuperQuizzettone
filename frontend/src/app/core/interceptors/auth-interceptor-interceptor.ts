import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {
   const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.token();

  // Clona la richiesta e aggiunge il token se presente
  const authReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;



  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Gestione globale degli errori di autenticazione
      if (error.status === 401 || error.status === 403) {
        authService.logout();
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
  return next(req);
};
