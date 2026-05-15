import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

 // controllo su autenticazione
  if (!authService.isLoggedIn()) {
    return router.createUrlTree(['/login'], {
      queryParams: { redirect: state.url },
    });
  }

  // Controllo ruoli
  const allowedRoles = route.data?.['roles'] as string[] || undefined;

  if (allowedRoles && allowedRoles.length > 0) {
    if (!authService.hasAnyRole(allowedRoles)) {
      return router.createUrlTree(['/home']);
    }
  }

  return true;
};
